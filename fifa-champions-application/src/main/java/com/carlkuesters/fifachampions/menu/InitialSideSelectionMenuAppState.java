package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.cinematics.CinematicAppState;

public class InitialSideSelectionMenuAppState extends SideSelectionMenuAppState {

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            getAppState(CinematicAppState.class).stopCinematic();
        }
    }

    @Override
    protected void confirm() {
        openMenu(TeamsMenuAppState.class);
    }
}
