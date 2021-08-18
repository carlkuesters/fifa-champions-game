package com.carlkuesters.fifachampions.cinematics;

import com.jme3.app.SimpleApplication;
import lombok.Getter;

public class Cinematic {

    public Cinematic() {
        // Used if the parts are too complex to be directly set in the super constructor
    }

    public Cinematic(boolean loop, CinematicPart[] parts) {
        this.loop = loop;
        this.parts = parts;
    }
    @Getter
    protected boolean loop;
    protected CinematicPart[] parts;
    private boolean initialized;
    private float time;

    public void reset(SimpleApplication simpleApplication) {
        if (!initialized) {
            initialize(simpleApplication);
        }
        time = 0;
        for (CinematicPart part : parts) {
            part.reset(simpleApplication);
        }
    }

    protected void initialize(SimpleApplication simpleApplication) {
        initialized = true;
    }

    public void update(float lastTimePerFrame, SimpleApplication simpleApplication) {
        time += lastTimePerFrame;
        for (CinematicPart part : parts) {
            CinematicAction action = part.getCinematicAction();
            if (part.isTriggered()) {
                if (!action.isFinished()) {
                    action.update(simpleApplication, lastTimePerFrame);
                } else if (!action.isCleanuped()) {
                    action.cleanup(simpleApplication);
                }
            } else if (time >= part.getStartTime()) {
                part.trigger(simpleApplication);
            }
        }
    }

    public void stop(SimpleApplication simpleApplication) {
        for (CinematicPart part : parts) {
            CinematicAction action = part.getCinematicAction();
            if (!action.isCleanuped()) {
                action.cleanup(simpleApplication);
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
