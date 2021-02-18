package com.carlkuesters.fifachampions.game;

import com.carlkuesters.fifachampions.Main;
import lombok.Getter;
import lombok.Setter;

@Getter
public class InitialTeamInfo {

    private int teamIndex;
    @Setter
    private int trikotIndex;
    private Player[] fieldPlayers;
    private Player[] reservePlayers;
    @Setter
    private Formation formation;

    public void setTeam(int teamIndex) {
        this.teamIndex = teamIndex;
        trikotIndex = 0;
        TeamInfo teamInfo = getTeamInfo();
        this.fieldPlayers = teamInfo.getFieldPlayers();
        this.reservePlayers = teamInfo.getReservePlayers();
        this.formation = teamInfo.getDefaultFormation();
    }

    public TeamInfo getTeamInfo() {
        return Main.TEAMS[teamIndex];
    }
}
