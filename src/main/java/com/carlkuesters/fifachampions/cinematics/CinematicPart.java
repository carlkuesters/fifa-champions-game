package com.carlkuesters.fifachampions.cinematics;

import lombok.Getter;

public class CinematicPart {

    public CinematicPart(float startTime, CinematicAction action) {
        this.startTime = startTime;
        this.action = action;
    }
    @Getter
    private float startTime;
    @Getter
    private CinematicAction action;
    @Getter
    private boolean triggered;

    public void reset(Cinematic cinematic) {
        action.reset(cinematic);
        triggered = false;
    }

    public void trigger() {
        action.trigger();
        triggered = true;
    }
}
