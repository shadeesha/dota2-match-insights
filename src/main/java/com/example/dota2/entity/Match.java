package com.example.dota2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "match")
@NoArgsConstructor
@Getter
@Setter
public class Match {

    @Id
    private Long matchId;

    private Boolean radiantWin;

    private Integer averageRankTier;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HeroPick> picks;

    public Match(Long matchId, Boolean radiantWin, Integer averageRankTier) {
        this.matchId = matchId;
        this.radiantWin = radiantWin;
        this.averageRankTier = averageRankTier;
    }
}
