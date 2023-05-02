package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public class PressureButtonBehaviour extends InstantButtonBehaviour {

    public PressureButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    protected void onTrigger(boolean isPressed) {
        controller.getPlayerObject().setIsPressuring(isPressed);
    }
}
