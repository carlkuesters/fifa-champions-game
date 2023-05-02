package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public class SwitchPlayerButtonBehaviour extends InstantButtonBehaviour {

    public SwitchPlayerButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    protected void onTrigger(boolean isPressed) {
        if (isPressed && !controller.getPlayerObject().isOwningBall()) {
            controller.switchPlayer();
        }
    }
}
