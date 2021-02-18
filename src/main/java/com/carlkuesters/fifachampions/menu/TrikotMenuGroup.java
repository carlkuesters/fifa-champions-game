package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameCreationInfo;
import com.carlkuesters.fifachampions.game.InitialTeamInfo;

import java.util.function.Consumer;

public class TrikotMenuGroup extends CarouselMenuGroup {

    public TrikotMenuGroup(Runnable back, GameCreationInfo gameCreationInfo, Consumer<Integer> updateTeamTrikot, Consumer<Integer> confirm) {
        super(back);
        this.gameCreationInfo = gameCreationInfo;
        this.updateTeamTrikot = updateTeamTrikot;
        this.confirm = confirm;
    }
    private GameCreationInfo gameCreationInfo;
    private Consumer<Integer> updateTeamTrikot;
    private Consumer<Integer> confirm;

    @Override
    protected int getValue(int joyId) {
        return getInitialTeamInfo(joyId).getTrikotIndex();
    }

    @Override
    protected void setValue(int joyId, int value) {
        getInitialTeamInfo(joyId).setTrikotIndex(value);
        updateTeamTrikot.accept(getTeamIndex(joyId));
    }

    @Override
    protected int getMaximumValue(int joyId) {
        return (getInitialTeamInfo(joyId).getTeamInfo().getTrikotNames().length - 1);
    }

    private InitialTeamInfo getInitialTeamInfo(int joyId) {
        return gameCreationInfo.getTeams()[getTeamIndex(joyId)];
    }

    // TODO: Check if teamIndex is not null in the callers of this method (unassigned controllers)
    private int getTeamIndex(int joyId) {
        int teamSide = gameCreationInfo.getControllerTeamSides().get(joyId);
        Integer teamIndex = null;
        if (teamSide == 1) {
            teamIndex = 0;
        } else if (teamSide == -1) {
            teamIndex = 1;
        }
        return teamIndex;
    }

    @Override
    public void confirm(int joyId) {
        confirm.accept(joyId);
    }
}
