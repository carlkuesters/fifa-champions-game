package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InitialTeamInfo {
    private int teamIndex;
    private int trikotIndex;
    private Player[] fieldPlayers;
    private Player[] reservePlayers;
    private Formation formation;
}
