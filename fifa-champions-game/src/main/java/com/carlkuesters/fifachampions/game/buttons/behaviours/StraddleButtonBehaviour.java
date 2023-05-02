package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.Controller;

public class StraddleButtonBehaviour extends InstantButtonBehaviour {

    public StraddleButtonBehaviour(Controller controller) {
        super(controller);
    }

    @Override
    protected void onTrigger(boolean isPressed) {
        controller.getPlayerObject().straddle();
    }
}
