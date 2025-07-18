package com.example.dota2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "training_data")
@NoArgsConstructor
@Getter
@Setter
public class TrainingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id", nullable = false)
    private Long matchId;

    @Column(name = "radiant_team")
    private List<String> radiantTeam;

    @Column(name = "dire_team")
    private List<String> direTeam;

    @Column(name = "radiant_win", nullable = false)
    private boolean radiantWin;

    @Column(name = "average_rank_tier")
    private Integer averageRankTier;
}
