package com.example.dota2.repository;

import com.example.dota2.entity.HeroPick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroPickRepository extends JpaRepository<HeroPick, Long> {
    List<HeroPick> findByMatch_MatchId(Long matchId);
}
