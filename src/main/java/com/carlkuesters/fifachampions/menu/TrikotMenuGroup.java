package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameCreationInfo;
import com.carlkuesters.fifachampions.Main;
import com.carlkuesters.fifachampions.game.InitialTeamInfo;
import com.carlkuesters.fifachampions.game.TeamInfo;

import java.util.function.Consumer;

public class TrikotMenuGroup extends GameCreationCarouselMenuGroup {

    public TrikotMenuGroup(Runnable back, GameCreationInfo gameCreationInfo, Consumer<Integer> updateTeam, Consumer<Integer> confirm) {
        super(back, gameCreationInfo, updateTeam, confirm);
    }

    @Override
    protected int getValue(InitialTeamInfo initialTeamInfo) {
        return initialTeamInfo.getTrikotIndex();
    }

    @Override
    protected void setValue(InitialTeamInfo initialTeamInfo, int value) {
        initialTeamInfo.setTrikotIndex(value);
    }

    @Override
    protected int getMaximumValue(InitialTeamInfo initialTeamInfo) {
        TeamInfo teamInfo = Main.TEAMS[initialTeamInfo.getTeamIndex()];
        return (teamInfo.getTrikotNames().length - 1);
    }
}
