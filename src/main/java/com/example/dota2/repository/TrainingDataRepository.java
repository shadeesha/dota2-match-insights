package com.example.dota2.repository;

import com.example.dota2.entity.TrainingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingDataRepository extends JpaRepository<TrainingData, Long> {
}
