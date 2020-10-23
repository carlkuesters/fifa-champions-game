/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.ControllerButton;
import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.SprintButtonBehaviour;

/**
 *
 * @author Carl
 */
public class SprintButton extends ControllerButton {

    private SprintButtonBehaviour sprintButtonBehaviour = new SprintButtonBehaviour();

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        return sprintButtonBehaviour;
    }
}
