package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TeamInfo {
    private String name;
    private Player[] fieldPlayers;
    private Player[] reservePlayers;
    private Formation defaultFormation;
}
