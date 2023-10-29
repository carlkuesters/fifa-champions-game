package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.cinematics.CinematicAction;
import com.carlkuesters.fifachampions.game.PlayerAnimation;
import com.carlkuesters.fifachampions.visuals.PlayerVisual;

public class PlayerAnimationAction extends CinematicAction {

    public PlayerAnimationAction(PlayerVisual playerVisual, PlayerAnimation animation, boolean randomOffset) {
        this.playerVisual = playerVisual;
        this.animation = animation;
        this.randomOffset = randomOffset;
    }
    private PlayerVisual playerVisual;
    private PlayerAnimation animation;
    private boolean randomOffset;

    @Override
    public void trigger() {
        super.trigger();
        playerVisual.playAnimation(randomOffset ? animation.cloneWithRandomOffset() : animation);
    }
}
