package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class AttachSpatialAction extends CinematicAction {

    public AttachSpatialAction(Node parent, Spatial... spatials) {
        this.parent = parent;
        this.spatials = spatials;
    }
    private Node parent;
    private Spatial[] spatials;

    @Override
    public void trigger() {
        super.trigger();
        for (Spatial spatial : spatials) {
            parent.attachChild(spatial);
        }
    }
}
