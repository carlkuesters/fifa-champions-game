package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.Main;
import com.carlkuesters.fifachampions.game.InitialTeamInfo;

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
        return (Main.TEAMS.length - 1);
    }
}
