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
public class FlankButtonBehaviour extends ChargedBallButtonBehaviour {

    @Override
    public void onPressed(boolean isPressed) {
        super.onPressed(isPressed);
        if (isPressed) {
            controller.getPlayerObject().setAnimation(new PlayerAnimation("run_kick_start", maxChargedDuration));
        }
    }

    @Override
    protected void onTrigger(float strength) {
        controller.getPlayerObject().setAnimation(new PlayerAnimation("run_kick_end", 1));
        controller.getPlayerObject().flank(strength);
    }
}
