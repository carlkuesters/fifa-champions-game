package com.carlkuesters.fifachampions.cinematics;

import com.jme3.app.SimpleApplication;
import lombok.Getter;

public class Cinematic {

    public Cinematic(boolean loop, CinematicPart[] parts) {
        this.loop = loop;
        this.parts = parts;
    }
    @Getter
    private boolean loop;
    private CinematicPart[] parts;
    private float time;

    public void reset() {
        time = 0;
        for (CinematicPart part : parts) {
            part.setTriggered(false);
        }
    }

    public void update(float lastTimePerFrame, SimpleApplication simpleApplication) {
        time += lastTimePerFrame;
        for (CinematicPart part : parts) {
            if (part.isTriggered()) {
                if (!part.getCinematicAction().isFinished()) {
                    part.getCinematicAction().update(simpleApplication);
                }
            } else if (time >= part.getStartTime()) {
                part.getCinematicAction().trigger(simpleApplication);
                part.setTriggered(true);
            }
        }
    }

    public void stop(SimpleApplication simpleApplication) {
        for (CinematicPart part : parts) {
            if (part.isTriggered() && (!part.getCinematicAction().isFinished())) {
                part.getCinematicAction().cleanup(simpleApplication);
            }
        }
    }

    public boolean isFinished() {
        for (CinematicPart part : parts) {
            if ((!part.isTriggered()) || (!part.getCinematicAction().isFinished())) {
                return false;
            }
        }
        return true;
    }
}
