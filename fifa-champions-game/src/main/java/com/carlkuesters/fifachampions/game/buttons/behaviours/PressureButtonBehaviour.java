/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons.behaviours;

/**
 *
 * @author Carl
 */
public class PressureButtonBehaviour extends InstantButtonBehaviour {

    @Override
    protected void onTrigger(boolean isPressed) {
        controller.getPlayerObject().setIsPressuring(isPressed);
    }
}
