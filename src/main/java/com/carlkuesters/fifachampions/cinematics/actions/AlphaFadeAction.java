package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;

public class AlphaFadeAction extends CinematicAction {

    public AlphaFadeAction(Material material, float startValue, float endValue, float duration) {
        this.material = material;
        this.startValue = startValue;
        this.endValue = endValue;
        this.duration = duration;
    }
    private Material material;
    private float startValue;
    private float endValue;
    private float duration;
    private float passedTime;

    @Override
    public void update(SimpleApplication simpleApplication, float lastTimePerFrame) {
        super.update(simpleApplication, lastTimePerFrame);
        passedTime += lastTimePerFrame;
        float alpha = FastMath.interpolateLinear((passedTime / duration), startValue, endValue);
        ColorRGBA color = (ColorRGBA) material.getParam("Color").getValue();
        material.setColor("Color", new ColorRGBA(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    }

    @Override
    protected boolean isFinished() {
        return (passedTime >= duration);
    }
}
