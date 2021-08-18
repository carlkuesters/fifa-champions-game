package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

public class DetachSpatialAction extends CinematicAction {

    public DetachSpatialAction(Spatial spatial) {
        this.spatial = spatial;
    }
    private Spatial spatial;

    @Override
    public void trigger(SimpleApplication simpleApplication) {
        super.trigger(simpleApplication);
        simpleApplication.getRootNode().detachChild(spatial);
    }
}
