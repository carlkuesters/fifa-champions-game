package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ControllerButton {
    
    protected Controller controller;

    public abstract ControllerButtonBehaviour getBehaviour();
}
