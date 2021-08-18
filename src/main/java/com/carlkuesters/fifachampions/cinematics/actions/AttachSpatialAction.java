package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

public class AttachSpatialAction extends CinematicAction {

    public AttachSpatialAction(Spatial spatial) {
        this.spatial = spatial;
    }
    private Spatial spatial;

    @Override
    public void trigger(SimpleApplication simpleApplication) {
        super.trigger(simpleApplication);
        simpleApplication.getRootNode().attachChild(spatial);
    }
}
