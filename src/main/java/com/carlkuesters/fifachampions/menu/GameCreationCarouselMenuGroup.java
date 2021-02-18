package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameCreationInfo;
import com.carlkuesters.fifachampions.game.InitialTeamInfo;

import java.util.function.Consumer;

public abstract class GameCreationCarouselMenuGroup extends CarouselMenuGroup {

    public GameCreationCarouselMenuGroup(Runnable back, GameCreationInfo gameCreationInfo, Consumer<Integer> updateTeam, Consumer<Integer> confirm) {
        super(back);
        this.gameCreationInfo = gameCreationInfo;
        this.updateTeam = updateTeam;
        this.confirm = confirm;
    }
    private GameCreationInfo gameCreationInfo;
    private Consumer<Integer> updateTeam;
    private Consumer<Integer> confirm;

    @Override
    protected int getValue(int joyId) {
        return getValue(getInitialTeamInfo(joyId));
    }

    protected abstract int getValue(InitialTeamInfo initialTeamInfo);

    @Override
    protected void setValue(int joyId, int value) {
        InitialTeamInfo initialTeamInfo = getInitialTeamInfo(joyId);
        setValue(initialTeamInfo, value);
        updateTeam.accept(getTeamIndex(joyId));
    }

    protected abstract void setValue(InitialTeamInfo initialTeamInfo, int value);

    @Override
    protected int getMaximumValue(int joyId) {
        return getMaximumValue(getInitialTeamInfo(joyId));
    }

    protected abstract int getMaximumValue(InitialTeamInfo initialTeamInfo);

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
