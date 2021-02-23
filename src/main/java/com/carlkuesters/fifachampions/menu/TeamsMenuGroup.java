package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.InitialTeamInfo;
import com.carlkuesters.fifachampions.game.content.Teams;

public class TeamsMenuGroup extends GameCreationCarouselMenuGroup {

    public TeamsMenuGroup(InitialTeamInfo initialTeamInfo, Runnable updateTeam, Runnable confirm) {
        super(initialTeamInfo, updateTeam, confirm);
    }

    @Override
    public int getCarouselValue() {
        return initialTeamInfo.getTeamIndex();
    }

    @Override
    public void setCarouselValue(int value) {
        initialTeamInfo.setTeam(value);
        super.setCarouselValue(value);
    }

    @Override
    public int getCarouselMaximumValue() {
        return (Teams.TEAMS.length - 1);
    }
}
