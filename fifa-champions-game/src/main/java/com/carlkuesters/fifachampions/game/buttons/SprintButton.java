package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.ControllerButton;
import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.SprintButtonBehaviour;

public class SprintButton extends ControllerButton {

    private SprintButtonBehaviour sprintButtonBehaviour = new SprintButtonBehaviour();

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        return sprintButtonBehaviour;
    }
}
