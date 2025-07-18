package com.example.dota2.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchDetailsDTO {

    private Long match_id;
    private Boolean radiant_win;
    private List<PlayerDTO> players;
}
