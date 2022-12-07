package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.game.PlayerAnimation;
import com.jme3.animation.AnimChannel;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class PlayerVisualAnimationControl extends AbstractControl {

    public PlayerVisualAnimationControl(AnimChannel animChannel) {
        this.animChannel = animChannel;
    }
    private static final float BLEND_TIME = 0.15f;
    private AnimChannel animChannel;
    private float remainingBlendTime;
    private PlayerAnimation queuedPlayerAnimation;
    private float queuedStartTime;

    public void playAnimation(PlayerAnimation playerAnimation, float startTime) {
        if (!playerAnimation.getName().equals(animChannel.getAnimationName())) {
            if (remainingBlendTime > 0) {
                queuedPlayerAnimation = playerAnimation;
                queuedStartTime = startTime;
                return;
            }
            animChannel.setAnim(playerAnimation.getName(), BLEND_TIME);
            animChannel.setTime(startTime);
            remainingBlendTime = BLEND_TIME;
        }
        animChannel.setSpeed(animChannel.getAnimMaxTime() / playerAnimation.getLoopDuration());
        animChannel.setLoopMode(playerAnimation.getLoopMode());
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (remainingBlendTime > 0) {
            remainingBlendTime -= tpf;
            if (remainingBlendTime <= 0) {
                remainingBlendTime = 0;
                if (queuedPlayerAnimation != null) {
                    playAnimation(queuedPlayerAnimation, queuedStartTime);
                    queuedPlayerAnimation = null;
                    queuedStartTime = 0;
                }
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
