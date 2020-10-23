/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.ControllerButton;
import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Situation;

/**
 *
 * @author Carl
 */
public class DefaultBallActionButton extends ControllerButton {

    private ControllerButtonBehaviour behaviourWithOwnedBallGround;
    private ControllerButtonBehaviour behaviourWithOwnedBallHands;
    private ControllerButtonBehaviour behaviourWithUnownedBallNoSituation;
    private ControllerButtonBehaviour behaviourWithAllyOwnedBallNoSituation;
    private ControllerButtonBehaviour behaviourWithEnemyOwnedBallNoSituation;

    public void setBehaviours(
        ControllerButtonBehaviour behaviourWithOwnedBallGround,
        ControllerButtonBehaviour behaviourWithOwnedBallHands,
        ControllerButtonBehaviour behaviourWithUnownedBallNoSituation,
        ControllerButtonBehaviour behaviourWithAllyOwnedBallNoSituation,
        ControllerButtonBehaviour behaviourWithEnemyOwnedBallNoSituation
    ) {
        this.behaviourWithOwnedBallGround = behaviourWithOwnedBallGround;
        this.behaviourWithOwnedBallHands = behaviourWithOwnedBallHands;
        this.behaviourWithUnownedBallNoSituation = behaviourWithUnownedBallNoSituation;
        this.behaviourWithAllyOwnedBallNoSituation = behaviourWithAllyOwnedBallNoSituation;
        this.behaviourWithEnemyOwnedBallNoSituation = behaviourWithEnemyOwnedBallNoSituation;
    }

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        Game game = controller.getPlayerObject().getGame();
        PlayerObject ballOwner = game.getBall().getOwner();
        Situation situation = game.getSituation();
        if (ballOwner == controller.getPlayerObject()) {
            boolean isFromGroundOrHands = ((situation == null) || situation.isFromGroundOrHands());
            return (isFromGroundOrHands ? behaviourWithOwnedBallGround : behaviourWithOwnedBallHands);
        }
        if (situation == null) {
            if (ballOwner == null) {
                return behaviourWithUnownedBallNoSituation;
            } else if (ballOwner.getTeam() != controller.getTeam()) {
                return behaviourWithEnemyOwnedBallNoSituation;
            } else {
                return behaviourWithAllyOwnedBallNoSituation;
            }
        }
        return null;
    }
}
