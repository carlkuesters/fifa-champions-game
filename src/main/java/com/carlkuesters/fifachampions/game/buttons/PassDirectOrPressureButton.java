package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.buttons.behaviours.PassDirectButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.PressureButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ThrowInDirectButtonBehaviour;

public class PassDirectOrPressureButton extends DefaultBallActionButton {

    public PassDirectOrPressureButton() {
        PassDirectButtonBehaviour passDirectButtonBehaviour = new PassDirectButtonBehaviour();
        setBehaviours(
            passDirectButtonBehaviour,
            new ThrowInDirectButtonBehaviour(),
            passDirectButtonBehaviour,
            passDirectButtonBehaviour,
            new PressureButtonBehaviour()
        );
    }
}
