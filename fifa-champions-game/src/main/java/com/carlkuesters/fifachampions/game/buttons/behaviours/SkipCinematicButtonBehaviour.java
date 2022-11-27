package com.carlkuesters.fifachampions.game.buttons.behaviours;

public class SkipCinematicButtonBehaviour extends InstantButtonBehaviour {

    @Override
    protected void onTrigger(boolean isPressed) {
        if (isPressed) {
            controller.getGame().finishActiveCinematic();
        }
    }
}
