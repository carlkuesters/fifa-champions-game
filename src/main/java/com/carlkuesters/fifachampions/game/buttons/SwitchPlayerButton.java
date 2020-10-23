/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.ControllerButton;
import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.SwitchPlayerButtonBehaviour;

/**
 *
 * @author Carl
 */
public class SwitchPlayerButton extends ControllerButton {

    private SwitchPlayerButtonBehaviour switchPlayerButtonBehaviour = new SwitchPlayerButtonBehaviour();

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        return switchPlayerButtonBehaviour;
    }
}
