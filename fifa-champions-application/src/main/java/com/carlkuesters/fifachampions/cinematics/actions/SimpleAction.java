package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;

public class SimpleAction extends CinematicAction {

    public SimpleAction(Runnable runnable) {
        this.runnable = runnable;
    }
    private Runnable runnable;

    @Override
    public void trigger() {
        super.trigger();
        runnable.run();
    }
}
