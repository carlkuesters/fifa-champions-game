/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.buttons.behaviours.PassDirectButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PressureButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ThrowInDirectButtonBehaviour;

/**
 *
 * @author Carl
 */
public class PassDirectOrPressureButton extends DefaultBallActionButton {

    public PassDirectOrPressureButton() {
        PassDirectButtonBehaviour passDirectButtonBehaviour = new PassDirectButtonBehaviour();
        setBehaviours(
            passDirectButtonBehaviour,
            new ThrowInDirectButtonBehaviour(),
            passDirectButtonBehaviour,
            passDirectButtonBehaviour,
            new PressureButtonBehaviour()
        );
    }
}
