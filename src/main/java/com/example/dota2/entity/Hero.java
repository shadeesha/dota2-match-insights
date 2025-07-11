package com.example.dota2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "hero")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Hero {

    @Id
    private Long heroId;

    private String name;

    private String localizedName;

    private String attackType;

    @Column(length = 1000)
    private String roles;

    @OneToMany(mappedBy = "hero")
    private List<HeroPick> picks;
}
