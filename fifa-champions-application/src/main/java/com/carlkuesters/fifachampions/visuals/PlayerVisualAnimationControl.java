package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.game.PlayerAnimation;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.LoopMode;
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

    public void set(PlayerAnimation playerAnimation) {
        if (!playerAnimation.getName().equals(animChannel.getAnimationName())) {
            if (remainingBlendTime > 0) {
                return;
            }
            animChannel.setAnim(playerAnimation.getName(), BLEND_TIME);
            animChannel.setSpeed(0);
            remainingBlendTime = BLEND_TIME;
        }
        animChannel.setTime((playerAnimation.getTime() / playerAnimation.getDuration()) * animChannel.getAnimMaxTime());
    }

    public void play(PlayerAnimation playerAnimation) {
        animChannel.setAnim(playerAnimation.getName(), 0);
        animChannel.setSpeed(animChannel.getAnimMaxTime() / playerAnimation.getDuration());
        animChannel.setLoopMode(playerAnimation.isLoop() ? LoopMode.Loop : LoopMode.DontLoop);
    }

    @Override
    protected void controlUpdate(float tpf) {
        remainingBlendTime -= tpf;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
