package com.carlkuesters.fifachampions.menu;

public class InitialSideSelectionMenuAppState extends SideSelectionMenuAppState {

    @Override
    protected void confirm() {
        openMenu(TeamsMenuAppState.class);
    }
}
