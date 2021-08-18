package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.PlayState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.Spatial;

public abstract class MoveAction extends CinematicAction {

    public MoveAction(MotionEvent motionEvent) {
        this.motionEvent = motionEvent;
    }
    private MotionEvent motionEvent;

    @Override
    public void trigger(SimpleApplication simpleApplication) {
        super.trigger(simpleApplication);
        setMotionEvent(getSpatial(simpleApplication), motionEvent);
    }

    protected abstract Spatial getSpatial(SimpleApplication simpleApplication);

    @Override
    protected boolean isFinished(){
        return (motionEvent.getPlayState() == PlayState.Stopped);
    }

    @Override
    public void cleanup(SimpleApplication simpleApplication) {
        super.cleanup(simpleApplication);
        motionEvent.stop();
    }
}
