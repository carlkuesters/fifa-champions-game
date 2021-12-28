package com.carlkuesters.fifachampions.cinematics.actions;

import com.carlkuesters.fifachampions.game.PlayerAnimation;
import com.carlkuesters.fifachampions.visuals.PlayerVisual;
import com.jme3.cinematic.events.MotionEvent;

public class PlayerMoveAction extends MoveAction {

    public PlayerMoveAction(PlayerVisual playerVisual, PlayerAnimation animation, MotionEvent motionEvent) {
        super(playerVisual.getModelNode(), motionEvent);
        this.playerVisual = playerVisual;
        this.animation = animation;
    }
    private PlayerVisual playerVisual;
    private PlayerAnimation animation;

    @Override
    public void trigger() {
        super.trigger();
        // TODO: Do the random offset here or explicitly from outside?
        playerVisual.playAnimation(animation, (float) (Math.random() * animation.getLoopDuration()));
    }

    @Override
    public void cleanup() {
        super.cleanup();
        playerVisual.playAnimation(PlayerVisual.IDLE_ANIMATION);
    }
}
