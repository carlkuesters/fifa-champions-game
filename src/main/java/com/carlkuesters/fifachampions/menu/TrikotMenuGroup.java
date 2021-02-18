package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameCreationInfo;
import com.carlkuesters.fifachampions.game.InitialTeamInfo;

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
        return (initialTeamInfo.getTeamInfo().getTrikotNames().length - 1);
    }
}
