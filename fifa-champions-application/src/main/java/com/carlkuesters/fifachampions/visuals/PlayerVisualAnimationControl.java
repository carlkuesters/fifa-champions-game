package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.JMonkeyUtil;
import com.carlkuesters.fifachampions.game.PlayerAnimation;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.AnimLayer;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.ClipAction;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class PlayerVisualAnimationControl extends AbstractControl {

    public PlayerVisualAnimationControl(AnimComposer animComposer) {
        this.animComposer = animComposer;
    }
    private AnimComposer animComposer;
    private boolean boundingBoxUpdated;

    public void set(PlayerAnimation playerAnimation) {
        AnimLayer animLayer = animComposer.getLayer(AnimComposer.DEFAULT_LAYER);
        if (!playerAnimation.getName().equals(animLayer.getCurrentActionName())) {
            Action action = animComposer.action(playerAnimation.getName());
            action.setSpeed(0);
            animLayer.setCurrentAction(playerAnimation.getName(), action);
        }
        animLayer.setTime((playerAnimation.getTime() / playerAnimation.getDuration()) * animLayer.getCurrentAction().getLength());
    }

    public void play(PlayerAnimation playerAnimation) {
        AnimLayer animLayer = animComposer.getLayer(AnimComposer.DEFAULT_LAYER);
        ClipAction clipAction = (ClipAction) animComposer.action(playerAnimation.getName());
        clipAction.setTransitionLength(0);
        clipAction.setSpeed(clipAction.getLength() / playerAnimation.getDuration());
        animLayer.setCurrentAction(clipAction);
        animLayer.setLooping(playerAnimation.isLoop());
    }

    @Override
    protected void controlUpdate(float tpf) {

    }

    @Override
    protected void controlRender(RenderManager renderManager, ViewPort viewPort) {
        if (!boundingBoxUpdated) {
            // The bounding box of the imported model is tiny (the scale is baked into the animation)
            JMonkeyUtil.updateAnimatedModelBounds(spatial, renderManager, viewPort);
            boundingBoxUpdated = true;
        }
    }
}
