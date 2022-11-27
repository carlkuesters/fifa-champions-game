package com.carlkuesters.fifachampions.cinematics.actions;

import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.Spatial;

public class MoveAction extends AbstractMoveAction {

    public MoveAction(Spatial spatial, MotionEvent motionEvent) {
        super(motionEvent);
        this.spatial = spatial;
    }
    private Spatial spatial;

    @Override
    protected Spatial getSpatial() {
        return spatial;
    }
}
