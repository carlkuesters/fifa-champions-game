/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.*;

/**
 *
 * @author Carl
 */
public abstract class InstantButtonBehaviour extends ControllerButtonBehaviour {

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
