package com.carlkuesters.fifachampions.game;

public abstract class ControllerButton {
    
    protected Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public abstract ControllerButtonBehaviour getBehaviour();
}
