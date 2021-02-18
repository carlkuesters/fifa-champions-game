package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameCreationInfo;
import com.carlkuesters.fifachampions.Main;
import com.carlkuesters.fifachampions.game.InitialTeamInfo;

import java.util.function.Consumer;

public class TeamsMenuGroup extends GameCreationCarouselMenuGroup {

    public TeamsMenuGroup(Runnable back, GameCreationInfo gameCreationInfo, Consumer<Integer> updateTeam, Consumer<Integer> confirm) {
        super(back, gameCreationInfo, updateTeam, confirm);
    }

    @Override
    protected int getValue(InitialTeamInfo initialTeamInfo) {
        return initialTeamInfo.getTeamIndex();
    }

    @Override
    protected void setValue(InitialTeamInfo initialTeamInfo, int value) {
        initialTeamInfo.setTeam(value);
    }

    @Override
    protected int getMaximumValue(InitialTeamInfo initialTeamInfo) {
        return (Main.TEAMS.length - 1);
    }
}
