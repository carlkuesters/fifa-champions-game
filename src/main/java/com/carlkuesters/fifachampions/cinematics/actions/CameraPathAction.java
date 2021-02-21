package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAppState;
import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.PlayState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.CameraNode;

public class CameraPathAction extends CinematicAction {

    public CameraPathAction(MotionEvent motionEvent) {
        this.motionEvent = motionEvent;
    }
    private MotionEvent motionEvent;

    @Override
    public void trigger(SimpleApplication simpleApplication) {
        setMotionEvent(getCameraNode(simpleApplication), motionEvent);
    }

    @Override
    public void update(SimpleApplication simpleApplication) {
        super.update(simpleApplication);
        getCameraNode(simpleApplication).setEnabled(true);
    }

    @Override
    protected boolean isFinished(){
        return (motionEvent.getPlayState() == PlayState.Stopped);
    }

    @Override
    public void cleanup(SimpleApplication simpleApplication) {
        super.cleanup(simpleApplication);
        getCameraNode(simpleApplication).setEnabled(false);
        motionEvent.stop();
    }

    private CameraNode getCameraNode(SimpleApplication simpleApplication) {
        CinematicAppState cinematicAppState = simpleApplication.getStateManager().getState(CinematicAppState.class);
        return cinematicAppState.getCameraNode();
    }
}
