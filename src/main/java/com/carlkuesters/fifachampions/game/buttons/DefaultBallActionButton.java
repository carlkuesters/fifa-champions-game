package com.carlkuesters.fifachampions.game.buttons;

import com.carlkuesters.fifachampions.game.ControllerButton;
import com.carlkuesters.fifachampions.game.ControllerButtonBehaviour;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.Situation;
import com.carlkuesters.fifachampions.game.buttons.behaviours.SkipCinematicButtonBehaviour;

import java.util.HashMap;

public class DefaultBallActionButton extends ControllerButton {

    private ControllerButtonBehaviour behaviourSkipCinematic = new SkipCinematicButtonBehaviour();
    private HashMap<Class<? extends Situation>, ControllerButtonBehaviour> behavioursWithOwnedBallSituation = new HashMap<>();
    private ControllerButtonBehaviour behaviourWithUnownedBallNoSituation;
    private ControllerButtonBehaviour behaviourWithSelfOwnedBallNoSituation;
    private ControllerButtonBehaviour behaviourWithAllyOwnedBallNoSituation;
    private ControllerButtonBehaviour behaviourWithEnemyOwnedBallNoSituation;

    protected void setNoSituationBehaviours(
        ControllerButtonBehaviour behaviourWithUnownedBallNoSituation,
        ControllerButtonBehaviour behaviourWithSelfOwnedBallNoSituation,
        ControllerButtonBehaviour behaviourWithAllyOwnedBallNoSituation,
        ControllerButtonBehaviour behaviourWithEnemyOwnedBallNoSituation
    ) {
        this.behaviourWithUnownedBallNoSituation = behaviourWithUnownedBallNoSituation;
        this.behaviourWithSelfOwnedBallNoSituation = behaviourWithSelfOwnedBallNoSituation;
        this.behaviourWithAllyOwnedBallNoSituation = behaviourWithAllyOwnedBallNoSituation;
        this.behaviourWithEnemyOwnedBallNoSituation = behaviourWithEnemyOwnedBallNoSituation;
    }

    protected void setBehaviourWithOwnedBall(Class<? extends Situation> situationClass, ControllerButtonBehaviour behaviour) {
        behavioursWithOwnedBallSituation.put(situationClass, behaviour);
    }

    @Override
    public ControllerButtonBehaviour getBehaviour() {
        Game game = controller.getPlayerObject().getGame();
        PlayerObject ballOwner = game.getBall().getOwner();
        Situation situation = game.getSituation();
        if (game.getActiveCinematic() != null) {
            return behaviourSkipCinematic;
        } else if (situation == null) {
            if (ballOwner == null) {
                return behaviourWithUnownedBallNoSituation;
            } else if (ballOwner == controller.getPlayerObject()) {
                return behaviourWithSelfOwnedBallNoSituation;
            } else if (ballOwner.getTeam() == controller.getTeam()) {
                return behaviourWithAllyOwnedBallNoSituation;
            } else {
                return behaviourWithEnemyOwnedBallNoSituation;
            }
        } else {
            return behavioursWithOwnedBallSituation.get(situation.getClass());
        }
    }
}
