package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameCreationInfo;
import com.carlkuesters.fifachampions.game.InitialTeamInfo;

import java.util.function.Consumer;

public class TrikotMenuGroup extends MenuGroup {

    public TrikotMenuGroup(Runnable back, GameCreationInfo gameCreationInfo, Consumer<Integer> updateTeamTrikot, Runnable confirm) {
        super(back);
        this.gameCreationInfo = gameCreationInfo;
        this.updateTeamTrikot = updateTeamTrikot;
        this.confirm = confirm;
    }
    private GameCreationInfo gameCreationInfo;
    private Consumer<Integer> updateTeamTrikot;
    private Runnable confirm;

    @Override
    public void navigateLeft(int joyId) {
        super.navigateLeft(joyId);
        switchTrikot(joyId, 1);
    }

    @Override
    public void navigateRight(int joyId) {
        super.navigateRight(joyId);
        switchTrikot(joyId, -1);
    }

    private void switchTrikot(int joyId, int direction) {
        int teamSide = gameCreationInfo.getControllerTeamSides().get(joyId);
        Integer teamIndex = null;
        if (teamSide == 1) {
            teamIndex = 0;
        } else if (teamSide == -1) {
            teamIndex = 1;
        }
        if (teamIndex != null) {
            InitialTeamInfo initialTeamInfo = gameCreationInfo.getTeams()[teamIndex];
            int oldTrikotIndex = initialTeamInfo.getTrikotIndex();
            int newTrikotIndex = oldTrikotIndex + direction;
            int trikotsCount = initialTeamInfo.getTeamInfo().getTrikotNames().length;
            if (newTrikotIndex >= trikotsCount) {
                newTrikotIndex = 0;
            } else if (newTrikotIndex < 0) {
                newTrikotIndex = (trikotsCount - 1);
            }
            initialTeamInfo.setTrikotIndex(newTrikotIndex);
            updateTeamTrikot.accept(teamIndex);
        }
    }

    @Override
    public void confirm() {
        confirm.run();
    }
}
