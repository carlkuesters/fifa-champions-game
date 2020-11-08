package com.carlkuesters.fifachampions.game.buttons.behaviours;

public class StraddleButtonBehaviour extends InstantButtonBehaviour {

    @Override
    protected void onTrigger(boolean isPressed) {
        controller.getPlayerObject().straddle();
    }
}
