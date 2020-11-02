package com.carlkuesters.fifachampions.game.buttons.behaviours;

public abstract class ChargedBallButtonBehaviour extends ChargedButtonBehaviour {

    @Override
    protected boolean isTriggerAllowed() {
        return controller.getPlayerObject().isOwningBall();
    }
}
