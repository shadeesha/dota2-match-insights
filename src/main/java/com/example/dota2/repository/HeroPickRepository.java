package com.example.dota2.repository;

import com.example.dota2.entity.HeroPick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroPickRepository extends JpaRepository<HeroPick, Long> {
}
