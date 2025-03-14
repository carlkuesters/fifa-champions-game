package com.carlkuesters.fifachampions;

import com.jme3.renderer.queue.TransparentComparator;
import com.jme3.scene.Geometry;

public class LayerGeometryComparator_Transparent extends TransparentComparator {

    @Override
    public int compare(Geometry geometry1, Geometry geometry2) {
        int layer1 = getLayer(geometry1);
        int layer2 = getLayer(geometry2);
        if (layer1 != layer2) {
            return (layer1 > layer2) ? 1 : -1;
        }
        return super.compare(geometry1, geometry2);
    }

    private int getLayer(Geometry geometry) {
        Integer layer = geometry.getUserData("layer");
        return (layer != null) ? layer : 0;
    }
}
