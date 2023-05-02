package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public class SkipCinematicButtonBehaviour extends InstantButtonBehaviour {

    public SkipCinematicButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    protected void onTrigger(boolean isPressed) {
        if (isPressed) {
            controller.getGame().finishActiveCinematic();
        }
    }
}
