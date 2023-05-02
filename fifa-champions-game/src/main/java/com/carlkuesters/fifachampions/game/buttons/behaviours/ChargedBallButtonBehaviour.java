package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public abstract class ChargedBallButtonBehaviour extends ChargedButtonBehaviour {

    public ChargedBallButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    protected boolean isTriggerAllowed() {
        return controller.getPlayerObject().isOwningBall();
    }
}
