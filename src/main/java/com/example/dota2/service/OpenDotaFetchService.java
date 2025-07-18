package com.example.dota2.service;

import com.example.dota2.dto.HeroDTO;
import com.example.dota2.dto.MatchDetailsDTO;
import com.example.dota2.dto.PlayerDTO;
import com.example.dota2.dto.PublicMatchDTO;
import com.example.dota2.entity.Hero;
import com.example.dota2.entity.HeroPick;
import com.example.dota2.entity.Match;
import com.example.dota2.repository.HeroPickRepository;
import com.example.dota2.repository.HeroRepository;
import com.example.dota2.repository.MatchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@EnableScheduling
public class OpenDotaFetchService {

    private static final String BASE_URL = "https://api.opendota.com/api";
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenDotaFetchService.class);

    private final WebClient webClient;
    private final MatchRepository matchRepository;
    private final HeroRepository heroRepository;
    private final HeroPickRepository heroPickRepository;
    private final List<Hero> heroList;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CACHE_FILE_PUBLIC_MATCHES = "public_matches_cache.json";
    private static final String CACHE_FILE_MATCH_DETAILS = "match_details_cache.json";

    private static int RUN_COUNT = 0;


    public OpenDotaFetchService(MatchRepository matchRepository,
                                HeroRepository heroRepository,
                                HeroPickRepository heroPickRepository,
                                WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.matchRepository = matchRepository;
        this.heroRepository = heroRepository;
        this.heroPickRepository = heroPickRepository;

        heroList = heroRepository.findAll();
    }

    @PostConstruct
    public void init() {
//        fetchAndStoreHeroData();
        LOGGER.info("Starting application");
    }

    @Scheduled(fixedRate = 120000)
    public void schedulerFetchAndSaveMatchData(){
        if(RUN_COUNT < 30) {
            LOGGER.info("Run count : {}, continuing", RUN_COUNT);
            fetchAndStoreRecentMatches(10);
        } else {
            LOGGER.info("Max run count reached. Stopping scheduled task.");
            return;
        }
        RUN_COUNT++;
    }

    public void fetchAndStoreHeroData() {
        List<HeroDTO> heroDTOList = fetchHeroDetails();

        List<Hero> heroes = heroDTOList.stream()
                .map(heroDTO -> new Hero(heroDTO.getId(),
                        heroDTO.getName(),
                        heroDTO.getLocalizedName(),
                        heroDTO.getAttackType(),
                        heroDTO.getRoles()))
                .toList();
        LOGGER.info("Obtained details of Hero and transformed them into Entity format.");
        heroRepository.saveAll(heroes);
        LOGGER.info("Hero details are saved to Database");
    }

    public void fetchAndStoreRecentMatches(Integer limit) {
        LOGGER.info("Fetching public matches");
        List<PublicMatchDTO> publicMatchDTOS = fetchRecentMatches();
        List<Long> matchIds = publicMatchDTOS.stream()
                .map(PublicMatchDTO::getMatchId).toList();

        List<MatchDetailsDTO> matchDetailsList = fetchMatchDetails(matchIds);

        for(MatchDetailsDTO matchDetailsDTO : matchDetailsList) {
            Double avgRankTier = matchDetailsDTO.getPlayers()
                    .stream()
                    .filter(playerDTO -> playerDTO.getRankTier() != null)
                    .mapToInt(PlayerDTO::getRankTier)
                    .average()
                    .orElse(0);

            LOGGER.info("Creating match Objects for each publicmatch");

            Match match = new Match(
                    matchDetailsDTO.getMatch_id(),
                    matchDetailsDTO.getRadiant_win(),
                    avgRankTier.intValue());

            List<HeroPick> heroPickList = new ArrayList<>();
            for(PlayerDTO playerDTO : matchDetailsDTO.getPlayers()) {
                HeroPick heroPick = new HeroPick(playerDTO.getPlayerSlot(),
                        playerDTO.getIsRadiant(),
                        playerDTO.getRankTier(),
                        match,
                        heroList.stream()
                                .filter(hero -> Objects.equals(playerDTO.getHeroId(), hero.getHeroId()))
                                .findFirst()
                                .orElse(null));

                heroPickList.add(heroPick);
            }

            matchRepository.save(match);
            LOGGER.info("Match details have been saved successfully.");

            heroPickRepository.saveAll(heroPickList);
            LOGGER.info("Hero Pick List has been saved successfully.");
        }
    }

    public List<HeroDTO> fetchHeroDetails() {
        return webClient.get()
                .uri("/heroes")
                .retrieve()
                .bodyToFlux(HeroDTO.class)
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(1)))
                .collectList()
                .onErrorReturn(List.of())
                .block();
    }

    public List<PublicMatchDTO> fetchRecentMatches() {

        return webClient.get()
                .uri("/publicMatches")
                .retrieve()
                .bodyToFlux(PublicMatchDTO.class)
                .collectList()
                .onErrorReturn(List.of())
                .block();
    }

    public List<MatchDetailsDTO> fetchMatchDetails(List<Long> matchIds) {

        Flux<MatchDetailsDTO> matchDetailsDTOFlux = Flux.fromIterable(matchIds)
                .delayElements(Duration.ofSeconds(1))
                .flatMap(id ->
                        webClient.get()
                                .uri("/matches/{id}", id)
                                .retrieve()
                                .bodyToMono(MatchDetailsDTO.class)
                                .doOnNext(response -> LOGGER.info("Match response: " + response))
                                .onErrorResume(e -> {
                                    LOGGER.error("Failed to fetch matches: {}", id);
                                    return Mono.empty();
                                })
                        );
        return matchDetailsDTOFlux.collectList().block();
    }
}
