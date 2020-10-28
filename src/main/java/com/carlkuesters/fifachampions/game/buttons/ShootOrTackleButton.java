package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.Situation;
import com.carlkuesters.fifachampions.game.buttons.behaviours.FreeKickShootButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ShootButtonBehaviour;
import com.carlkuesters.fifachampions.game.situations.FreeKickSituation;

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
        freeKickShootButtonBehaviour = new FreeKickShootButtonBehaviour();
    }
    private FreeKickShootButtonBehaviour freeKickShootButtonBehaviour;

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        Situation situation = controller.getPlayerObject().getGame().getSituation();
        if (situation instanceof FreeKickSituation) {
            FreeKickSituation freeKickSituation = (FreeKickSituation) situation;
            if (controller.getPlayerObject() == freeKickSituation.getStartingPlayer()) {
                return freeKickShootButtonBehaviour;
            }
        }
        return super.getBehaviour();
    }
}
