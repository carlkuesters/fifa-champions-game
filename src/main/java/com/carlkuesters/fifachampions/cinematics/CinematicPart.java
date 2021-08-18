package com.carlkuesters.fifachampions.cinematics;

import com.jme3.app.SimpleApplication;
import lombok.Getter;

public class CinematicPart {

    public CinematicPart(float startTime, CinematicAction cinematicAction) {
        this.startTime = startTime;
        this.cinematicAction = cinematicAction;
    }
    @Getter
    private float startTime;
    @Getter
    private CinematicAction cinematicAction;
    @Getter
    private boolean triggered;

    public void reset(SimpleApplication simpleApplication) {
        triggered = false;
    }

    public void trigger(SimpleApplication simpleApplication) {
        cinematicAction.trigger(simpleApplication);
        triggered = true;
    }
}
