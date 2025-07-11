package com.example.dota2.service;

import com.example.dota2.repository.HeroPickRepository;
import com.example.dota2.repository.HeroRepository;
import com.example.dota2.repository.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenDotaFetchService {

    private static final String BASE_URL = "https://api.opendota.com/api";

    private final WebClient webClient;
    private final MatchRepository matchRepository;
    private final HeroRepository heroRepository;
    private final HeroPickRepository heroPickRepository;

    public OpenDotaFetchService(MatchRepository matchRepository,
                                HeroRepository heroRepository,
                                HeroPickRepository heroPickRepository,
                                WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.matchRepository = matchRepository;
        this.heroRepository = heroRepository;
        this.heroPickRepository = heroPickRepository;
    }

    public void fetchAndStoreRecentMatches(int limit) {

    }

}
