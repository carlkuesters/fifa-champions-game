package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.GameAppState;
import com.carlkuesters.fifachampions.game.Formation;
import com.carlkuesters.fifachampions.game.Team;

public class PauseFormationMenuAppState extends FormationMenuAppState {

    @Override
    protected Formation getFormation(int joyId) {
        return getTeam(joyId).getFormation();
    }

    @Override
    protected void setFormation(int joyId, Formation formation) {
        getTeam(joyId).setFormation(formation);
    }

    private Team getTeam(int joyId) {
        // TODO: Why do I have to cast here?
        GameAppState gameAppState = (GameAppState) getAppState(GameAppState.class);
        return gameAppState.getControllers().get(joyId).getTeam();
    }

    @Override
    protected void confirm() {
        openMenu(PauseIngameMenuAppState.class);
    }

    @Override
    protected void back() {
        openMenu(PauseIngameMenuAppState.class);
    }
}
