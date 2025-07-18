package com.example.dota2.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hero")
@Getter
@Setter
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer heroId;

    private String name;

    private String localizedName;

    private String attackType;

    @Column(length = 1000)
    private List<String> roles;

    @OneToMany(mappedBy = "hero")
    private List<HeroPick> picks;

    public Hero(){}

    public Hero(Integer heroId, String name, String localizedName, String attackType, List<String> roles, List<HeroPick> picks) {
        this.heroId = heroId;
        this.name = name;
        this.localizedName = localizedName;
        this.attackType = attackType;
        this.roles = roles;
        this.picks = picks;
    }

    public Hero(Integer heroId, String name, String localizedName, String attackType, List<String> roles) {
        this.heroId = heroId;
        this.name = name;
        this.localizedName = localizedName;
        this.attackType = attackType;
        this.roles = roles;
        this.picks = new ArrayList<>();
    }
}
