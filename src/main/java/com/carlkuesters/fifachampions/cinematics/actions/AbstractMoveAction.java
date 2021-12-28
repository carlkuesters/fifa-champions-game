package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.jme3.cinematic.PlayState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.Spatial;

public abstract class AbstractMoveAction extends CinematicAction {

    public AbstractMoveAction(MotionEvent motionEvent) {
        this.motionEvent = motionEvent;
    }
    private MotionEvent motionEvent;

    @Override
    public void trigger() {
        super.trigger();
        setMotionEvent(getSpatial(), motionEvent);
    }

    protected abstract Spatial getSpatial();

    @Override
    protected boolean isFinished(){
        return (motionEvent.getPlayState() == PlayState.Stopped);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        motionEvent.stop();
    }
}
