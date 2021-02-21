package com.carlkuesters.fifachampions.cinematics;

import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.Spatial;

public abstract class CinematicAction {

    public abstract void trigger(SimpleApplication simpleApplication);

    protected void setMotionEvent(Spatial spatial, MotionEvent newMotionEvent) {
        MotionEvent oldMotionEvent = spatial.getControl(MotionEvent.class);
        if (oldMotionEvent != null) {
            oldMotionEvent.stop();
            spatial.removeControl(oldMotionEvent);
        }
        spatial.addControl(newMotionEvent);
        newMotionEvent.play();
    }

    public void update(SimpleApplication simpleApplication) {
        if (isFinished()) {
            cleanup(simpleApplication);
        }
    }

    protected boolean isFinished() {
        return true;
    }

    public void cleanup(SimpleApplication simpleApplication) {

    }
}
