package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.*;

public abstract class InstantButtonBehaviour extends ControllerButtonBehaviour {

    public InstantButtonBehaviour(Controller controller) {
        super(controller);
    }
    private boolean isPressed;

    @Override
    public void onPressed(boolean isPressed) {
        this.isPressed = isPressed;
        isTriggered = true;
    }

    @Override
    public void trigger() {
        super.trigger();
        onTrigger(isPressed);
    }

    protected abstract void onTrigger(boolean isPressed);
}
