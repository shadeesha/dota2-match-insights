package com.example.dota2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hero_pick")
@Getter
@Setter
public class HeroPick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer playerSlot;

    private Boolean isRadiant;

    private Integer rankTier;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "hero_id", nullable = false)
    private Hero hero;

    public HeroPick(){}

    public HeroPick(Long id, Integer playerSlot, Boolean isRadiant, Integer rankTier, Match match, Hero hero) {
        this.id = id;
        this.playerSlot = playerSlot;
        this.isRadiant = isRadiant;
        this.rankTier = rankTier;
        this.match = match;
        this.hero = hero;
    }

    public HeroPick(Integer playerSlot, Boolean isRadiant, Integer rankTier, Match match, Hero hero) {
        this.playerSlot = playerSlot;
        this.isRadiant = isRadiant;
        this.rankTier = rankTier;
        this.match = match;
        this.hero = hero;
    }
}
