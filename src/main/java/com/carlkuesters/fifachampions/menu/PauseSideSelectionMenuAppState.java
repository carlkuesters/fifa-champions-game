package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.Team;

public class PauseSideSelectionMenuAppState extends SideSelectionMenuAppState {

    @Override
    protected int getTeamSide(int joyId) {
        Team team = getController(joyId).getTeam();
        return ((team != null) ? (-1 * team.getSide()) : 0);
    }

    @Override
    protected void setTeamSide(int joyId, int teamSide) {
        Controller controller = getController(joyId);
        Team team = null;
        if (teamSide == -1) {
            team = controller.getGame().getTeams()[0];
        } else if (teamSide == 1) {
            team = controller.getGame().getTeams()[1];
        }
        controller.setTeam(team);
    }

    private Controller getController(int joyId) {
        // TODO: Why do I have to cast here?
        GameAppState gameAppState = (GameAppState) getAppState(GameAppState.class);
        return gameAppState.getControllers().get(joyId);
    }

    @Override
    protected void confirm() {
        back();
    }

    @Override
    protected void back() {
        openMenu(PauseIngameMenuAppState.class);
    }
}
