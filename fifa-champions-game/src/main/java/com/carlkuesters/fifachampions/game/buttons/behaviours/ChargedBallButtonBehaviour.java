package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public abstract class ChargedBallButtonBehaviour extends ChargedButtonBehaviour {

    public ChargedBallButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    public void onPressed(boolean isPressed) {
        super.onPressed(isPressed);
        if (isPressed) {
            if (controller.getPlayerObject().isOwningBall()) {
                onBallOwnerPressed();
                controller.getGame().setReplayRecordingEnabled(true);
            } else {
                onNotBallOwnerPressed();
            }
        }
    }

    protected void onBallOwnerPressed() {

    }

    protected void onNotBallOwnerPressed() {

    }

    @Override
    protected boolean isTriggerAllowed() {
        return controller.getPlayerObject().isOwningBall();
    }
}
