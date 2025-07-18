package com.example.dota2.service;

import com.example.dota2.entity.Hero;
import com.example.dota2.entity.HeroPick;
import com.example.dota2.entity.Match;
import com.example.dota2.entity.TrainingData;
import com.example.dota2.repository.HeroPickRepository;
import com.example.dota2.repository.HeroRepository;
import com.example.dota2.repository.MatchRepository;
import com.example.dota2.repository.TrainingDataRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingDataService {

    private final HeroPickRepository heroPickRepository;
    private final TrainingDataRepository trainingDataRepository;
    private final MatchRepository matchRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDataService.class);

    public TrainingDataService(HeroPickRepository heroPickRepository, TrainingDataRepository trainingDataRepository, MatchRepository matchRepository) {
        this.heroPickRepository = heroPickRepository;
        this.trainingDataRepository = trainingDataRepository;
        this.matchRepository = matchRepository;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Starting application");
        generateTrainingData();
    }

    @Transactional
    public void generateTrainingData(){
        List<Match> matches = matchRepository.findAll();

        for(Match match : matches) {
            List<HeroPick> heroPickList = heroPickRepository.findByMatch_MatchId(match.getMatchId());
            List<Integer> radiantTeam = new ArrayList<>();
            List<Integer> direTeam = new ArrayList<>();

            for(HeroPick heroPick : heroPickList) {
                if(heroPick.getIsRadiant()) {
                    radiantTeam.add(heroPick.getHero().getHeroId());
                } else {
                    direTeam.add(heroPick.getHero().getHeroId());
                }
            }

            Integer targetHero = radiantTeam.get(4);
            radiantTeam.remove(4);

            if(radiantTeam.size() == 4 && direTeam.size() == 5) {
                TrainingData trainingData = new TrainingData();
                trainingData.setDireTeam(direTeam);
                trainingData.setRadiantTeam(radiantTeam);
                trainingData.setMatchId(match.getMatchId());
                trainingData.setAverageRankTier(match.getAverageRankTier());
                trainingData.setRadiantWin(match.getRadiantWin());
                trainingData.setTargetHero(targetHero);

                trainingDataRepository.save(trainingData);
            }
        }
    }
}
