package com.carlkuesters.fifachampions.menu;

public class InitialSideSelectionMenuAppState extends SideSelectionMenuAppState {

    @Override
    protected void confirm() {
        openMenu(TeamsMenuAppState.class);
    }

    @Override
    protected void back() {
        openMenu(MainMenuAppState.class);
    }
}
