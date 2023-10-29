package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public abstract class ChargedBallButtonBehaviour extends ChargedButtonBehaviour {

    public ChargedBallButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    public void onPressed(boolean isPressed) {
        super.onPressed(isPressed);
        if (isPressed && controller.getPlayerObject().isOwningBall()) {
            onBallOwnerPressed();
            controller.getGame().enableReplayRecording();
        }
    }

    protected void onBallOwnerPressed() {

    }

    @Override
    protected boolean isTriggerAllowed() {
        return controller.getPlayerObject().isOwningBall();
    }
}
