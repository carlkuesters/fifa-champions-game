package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public class TrickShotButtonBehaviour extends InstantButtonBehaviour {

    public TrickShotButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    protected void onTrigger(boolean isPressed) {
        controller.setTrickShooting(isPressed);
    }
}
