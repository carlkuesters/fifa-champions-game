package com.carlkuesters.fifachampions.menu;

public class PauseSideSelectionMenuAppState extends SideSelectionMenuAppState {

    @Override
    protected void confirm() {
        back();
    }

    @Override
    protected void back() {
        openMenu(PauseIngameMenuAppState.class);
    }
}
