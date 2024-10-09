package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.ControllerButton;
import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.SprintButtonBehaviour;

public class SprintButton extends ControllerButton {

    public SprintButton(Controller controller) {
        super(controller);
        sprintButtonBehaviour = new SprintButtonBehaviour(controller);
    }
    private SprintButtonBehaviour sprintButtonBehaviour;

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        return (controller.getGame().getActiveCinematic() == null) ? sprintButtonBehaviour : null;
    }
}
