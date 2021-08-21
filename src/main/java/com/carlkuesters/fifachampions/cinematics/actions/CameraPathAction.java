package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAppState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Spatial;

public class CameraPathAction extends MoveAction {

    public CameraPathAction(MotionEvent motionEvent) {
        super(motionEvent);
    }

    @Override
    public void update(float lastTimePerFrame) {
        super.update(lastTimePerFrame);
        getCameraNode().setEnabled(true);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        getCameraNode().setEnabled(false);
    }

    @Override
    protected Spatial getSpatial() {
        return getCameraNode();
    }

    private CameraNode getCameraNode() {
        CinematicAppState cinematicAppState = cinematic.getSimpleApplication().getStateManager().getState(CinematicAppState.class);
        return cinematicAppState.getCameraNode();
    }
}
