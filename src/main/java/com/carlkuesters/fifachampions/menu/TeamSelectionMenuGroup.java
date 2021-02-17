package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.Team;

import java.util.function.Function;

public class TeamSelectionMenuGroup extends MenuGroup {

    public TeamSelectionMenuGroup(Runnable back, Function<Integer, Controller> getControllerByJoyId) {
        super(back);
        this.getControllerByJoyId = getControllerByJoyId;
    }
    private Function<Integer, Controller> getControllerByJoyId;

    @Override
    public void navigateLeft(int joyId) {
        super.navigateLeft(joyId);
        switchTeam(joyId, 1);
    }

    @Override
    public void navigateRight(int joyId) {
        super.navigateRight(joyId);
        switchTeam(joyId, -1);
    }

    private void switchTeam(int joyId, int direction) {
        Controller controller = getControllerByJoyId.apply(joyId);
        Team oldTeam = controller.getTeam();
        int oldTeamSide = ((oldTeam != null) ? oldTeam.getSide() : 0);
        int newTeamSide = Math.max(-1, Math.min(oldTeamSide + direction, 1));
        if (newTeamSide != oldTeamSide) {
            Team newTeam = null;
            if (newTeamSide == 1) {
                newTeam = controller.getGame().getTeams()[0];
            } else if (newTeamSide == -1) {
                newTeam = controller.getGame().getTeams()[1];
            }
            controller.setTeam(newTeam);
        }
    }
}
