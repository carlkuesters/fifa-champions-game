package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.ControllerButton;
import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.TrickShotButtonBehaviour;

public class TrickShotButton extends ControllerButton {

    public TrickShotButton(Controller controller) {
        super(controller);
        trickShotButtonBehaviour = new TrickShotButtonBehaviour(controller);
    }
    private TrickShotButtonBehaviour trickShotButtonBehaviour;

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        return (controller.getGame().getActiveCinematic() == null) ? trickShotButtonBehaviour : null;
    }
}
