package com.example.dota2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {

    @JsonProperty("hero_id")
    private Integer heroId;

    @JsonProperty("rank_tier")
    private Integer rankTier;

    @JsonProperty("player_slot")
    private Integer playerSlot;

    @JsonProperty("isRadiant")
    private Boolean isRadiant;
}
