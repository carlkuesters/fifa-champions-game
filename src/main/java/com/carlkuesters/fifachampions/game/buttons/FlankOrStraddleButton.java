/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.buttons.behaviours.FlankButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.StraddleButtonBehaviour;

/**
 *
 * @author Carl
 */
public class FlankOrStraddleButton extends DefaultBallActionButton {

    public FlankOrStraddleButton() {
        StraddleButtonBehaviour straddleButtonBehaviour = new StraddleButtonBehaviour();
        setBehaviours(
            new FlankButtonBehaviour(),
            null,
            straddleButtonBehaviour,
            straddleButtonBehaviour,
            straddleButtonBehaviour
        );
    }
}
