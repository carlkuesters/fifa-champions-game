package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public class SprintButtonBehaviour extends InstantButtonBehaviour {

    public SprintButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    protected void onTrigger(boolean isPressed) {
        controller.setIsSprinting(isPressed);
    }
}
