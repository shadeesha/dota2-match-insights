package com.example.dota2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicMatchDTO {

    @JsonProperty("match_id")
    private Long matchId;

    @JsonProperty("radiant_win")
    private Boolean radiantWin;

    @JsonProperty("avg_rank_tier")
    private Integer avgRankTier;
}
