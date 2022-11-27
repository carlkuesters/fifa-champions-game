package com.carlkuesters.fifachampions.cinematics;

import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.Spatial;
import lombok.Getter;

public class CinematicAction {

    protected Cinematic cinematic;
    @Getter
    private boolean cleanuped = true;

    public void reset(Cinematic cinematic) {
        this.cinematic = cinematic;
    }

    public void trigger() {
        cleanuped = false;
    }

    protected void setMotionEvent(Spatial spatial, MotionEvent newMotionEvent) {
        MotionEvent oldMotionEvent = spatial.getControl(MotionEvent.class);
        if (oldMotionEvent != null) {
            oldMotionEvent.stop();
            spatial.removeControl(oldMotionEvent);
        }
        spatial.addControl(newMotionEvent);
        newMotionEvent.play();
    }

    public void update(float lastTimePerFrame) {
        if (isFinished()) {
            cleanup();
        }
    }

    protected boolean isFinished() {
        return true;
    }

    public void cleanup() {
        cleanuped = true;
    }
}
