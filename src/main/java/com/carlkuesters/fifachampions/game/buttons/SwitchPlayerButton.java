package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.ControllerButton;
import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.SwitchPlayerButtonBehaviour;

public class SwitchPlayerButton extends ControllerButton {

    private SwitchPlayerButtonBehaviour switchPlayerButtonBehaviour = new SwitchPlayerButtonBehaviour();

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        return switchPlayerButtonBehaviour;
    }
}
