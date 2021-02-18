package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameCreationInfo;
import com.carlkuesters.fifachampions.game.Formation;
import com.carlkuesters.fifachampions.game.InitialTeamInfo;

public class InitialFormationMenuAppState extends FormationMenuAppState {

    @Override
    protected Formation getFormation(int joyId) {
        return getInitialTeamInfo(joyId).getFormation();
    }

    @Override
    protected void setFormation(int joyId, Formation formation) {
        getInitialTeamInfo(joyId).setFormation(formation);
    }

    private InitialTeamInfo getInitialTeamInfo(int joyId) {
        GameCreationInfo gameCreationInfo = mainApplication.getGameCreationInfo();
        Integer teamSide = gameCreationInfo.getControllerTeamSides().get(joyId);
        // TODO: Unassigned controllers
        return gameCreationInfo.getTeams()[(teamSide == 1) ? 0 : 1];
    }

    @Override
    protected void confirm() {
        openMenu(GameSettingsMenuAppState.class);
    }

    @Override
    protected void back() {
        openMenu(GameSettingsMenuAppState.class);
    }
}
