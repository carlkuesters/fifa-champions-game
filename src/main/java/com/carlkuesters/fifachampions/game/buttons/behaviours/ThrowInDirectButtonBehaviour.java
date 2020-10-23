/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.PlayerAnimation;

/**
 *
 * @author Carl
 */
public class ThrowInDirectButtonBehaviour extends ChargedBallButtonBehaviour {

    @Override
    public void onPressed(boolean isPressed) {
        super.onPressed(isPressed);
        if (isPressed) {
            controller.getPlayerObject().setAnimation(new PlayerAnimation("throw_in_start", maxChargedDuration));
        }
    }

    @Override
    protected void onTrigger(float strength) {
        float freezeTime = 0.5f;
        controller.getPlayerObject().setAnimation(new PlayerAnimation("throw_in_end", freezeTime));
        controller.getPlayerObject().freeze(freezeTime);
        controller.getPlayerObject().throwInDirect(strength);
    }
}
