package com.example.dota2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hero_pick")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HeroPick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer playerSlot;

    private Boolean isRadiant;

    private Integer rankTier;

    private Integer pickOrder;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "hero_id", nullable = false)
    private Hero hero;


}
