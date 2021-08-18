package com.carlkuesters.fifachampions.cinematics;

import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.Spatial;
import lombok.Getter;

public class CinematicAction {

    @Getter
    private boolean cleanuped = true;

    public void trigger(SimpleApplication simpleApplication) {
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

    public void update(SimpleApplication simpleApplication, float lastTimePerFrame) {
        if (isFinished()) {
            cleanup(simpleApplication);
        }
    }

    protected boolean isFinished() {
        return true;
    }

    public void cleanup(SimpleApplication simpleApplication) {
        cleanuped = true;
    }
}
