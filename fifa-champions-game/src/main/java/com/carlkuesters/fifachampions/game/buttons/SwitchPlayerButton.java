package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.ControllerButton;
import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.SwitchPlayerButtonBehaviour;

public class SwitchPlayerButton extends ControllerButton {

    public SwitchPlayerButton(Controller controller) {
        super(controller);
        switchPlayerButtonBehaviour = new SwitchPlayerButtonBehaviour(controller);
    }
    private SwitchPlayerButtonBehaviour switchPlayerButtonBehaviour;

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        return (controller.getGame().getActiveCinematic() == null) ? switchPlayerButtonBehaviour : null;
    }
}
