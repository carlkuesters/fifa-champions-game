package com.carlkuesters.fifachampions.game.buttons.behaviours;

public class SwitchPlayerButtonBehaviour extends InstantButtonBehaviour {

    @Override
    protected void onTrigger(boolean isPressed) {
        if (isPressed && !controller.getPlayerObject().isOwningBall()) {
            controller.switchPlayer();
        }
    }
}
