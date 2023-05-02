package com.carlkuesters.fifachampions.game;

public abstract class ControllerButtonBehaviour {

    public ControllerButtonBehaviour(Controller controller) {
        this.controller = controller;
    }
    protected Controller controller;
    protected boolean isTriggered;

    public abstract void onPressed(boolean isPressed);

    public boolean isTriggered() {
        return isTriggered;
    }

    public void trigger() {
        isTriggered = false;
    }
}
