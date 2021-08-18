package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Spatial;

public class CameraPathAction extends MoveAction {

    public CameraPathAction(MotionEvent motionEvent) {
        super(motionEvent);
    }

    @Override
    public void update(SimpleApplication simpleApplication, float lastTimePerFrame) {
        super.update(simpleApplication, lastTimePerFrame);
        getCameraNode(simpleApplication).setEnabled(true);
    }

    @Override
    public void cleanup(SimpleApplication simpleApplication) {
        super.cleanup(simpleApplication);
        getCameraNode(simpleApplication).setEnabled(false);
    }

    @Override
    protected Spatial getSpatial(SimpleApplication simpleApplication) {
        return getCameraNode(simpleApplication);
    }

    private CameraNode getCameraNode(SimpleApplication simpleApplication) {
        CinematicAppState cinematicAppState = simpleApplication.getStateManager().getState(CinematicAppState.class);
        return cinematicAppState.getCameraNode();
    }
}
