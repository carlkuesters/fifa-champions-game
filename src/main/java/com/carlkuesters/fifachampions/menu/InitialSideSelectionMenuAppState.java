package com.carlkuesters.fifachampions.menu;

public class InitialSideSelectionMenuAppState extends SideSelectionMenuAppState {

    @Override
    protected void back() {
        openMenu(MainMenuAppState.class);
    }

    @Override
    protected int getTeamSide(int joyId) {
        return mainApplication.getGameCreationInfo().getControllerTeamSides().get(joyId);
    }

    @Override
    protected void setTeamSide(int joyId, int teamSide) {
        mainApplication.getGameCreationInfo().getControllerTeamSides().put(joyId, teamSide);
    }

    @Override
    protected void confirm() {
        openMenu(TeamsMenuAppState.class);
    }
}
