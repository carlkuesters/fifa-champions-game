/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.buttons.behaviours.GoalkeeperPressureButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PassInRunButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ThrowInInRunButtonBehaviour;

/**
 *
 * @author Carl
 */
public class PassInRunOrGoalkeeperPressureButton extends DefaultBallActionButton {

    public PassInRunOrGoalkeeperPressureButton() {
        GoalkeeperPressureButtonBehaviour goalkeeperPressureButtonBehaviour = new GoalkeeperPressureButtonBehaviour();
        setBehaviours(
            new PassInRunButtonBehaviour(),
            new ThrowInInRunButtonBehaviour(),
            goalkeeperPressureButtonBehaviour,
            goalkeeperPressureButtonBehaviour,
            goalkeeperPressureButtonBehaviour
        );
    }
}
