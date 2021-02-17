package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InitialTeamInfo {
    private TeamInfo teamInfo;
    private String trikotName;
    private Player[] fieldPlayers;
    private Player[] reservePlayers;
    private Formation formation;
}
