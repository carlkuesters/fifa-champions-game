package com.carlkuesters.fifachampions.cinematics;

import lombok.Getter;

public class CinematicPart {

    public CinematicPart(CinematicPart previousPart, CinematicAction action) {
        this.startTime = Float.MAX_VALUE;
        this.previousPart = previousPart;
        this.action = action;
    }

    public CinematicPart(float startTime, CinematicAction action) {
        this.startTime = startTime;
        this.action = action;
    }
    private float startTime;
    private CinematicPart previousPart;
    @Getter
    private CinematicAction action;
    @Getter
    private boolean triggered;

    public void reset(Cinematic cinematic) {
        action.reset(cinematic);
        triggered = false;
    }

    public boolean shouldBeTriggered(float time) {
        return ((time >= startTime) || ((previousPart != null) && previousPart.isTriggered() && previousPart.getAction().isFinished()));
    }

    public void trigger() {
        action.trigger();
        triggered = true;
    }
}
