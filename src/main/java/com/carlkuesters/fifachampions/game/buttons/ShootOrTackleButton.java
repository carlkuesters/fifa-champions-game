/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.buttons.behaviours.ShootButtonBehaviour;

/**
 *
 * @author Carl
 */
public class ShootOrTackleButton extends DefaultBallActionButton {

    public ShootOrTackleButton() {
        ShootButtonBehaviour shootButtonBehaviour = new ShootButtonBehaviour();
        setBehaviours(
            shootButtonBehaviour,
            null,
            shootButtonBehaviour,
            shootButtonBehaviour,
            null
        );
    }
}
