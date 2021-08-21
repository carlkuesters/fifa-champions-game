package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.jme3.scene.Spatial;

public class DetachSpatialAction extends CinematicAction {

    public DetachSpatialAction(Spatial... spatials) {
        this.spatials = spatials;
    }
    private Spatial[] spatials;

    @Override
    public void trigger() {
        super.trigger();
        for (Spatial spatial : spatials) {
            spatial.getParent().detachChild(spatial);
        }
    }
}
