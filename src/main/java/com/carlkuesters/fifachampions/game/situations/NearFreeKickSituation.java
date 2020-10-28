package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.buttons.behaviours.NearFreeKickShootButtonBehaviour;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class NearFreeKickSituation extends FreeKickSituation {

    public NearFreeKickSituation(PlayerObject startingPlayer, Vector3f ballPosition) {
        super(startingPlayer, ballPosition);
    }

    @Override
    public void start() {
        super.start();
        game.setCameraPerspective(getCameraPerspectiveTowardsEnemyGoal(4, 14), 2);
    }

    @Override
    protected Vector3f getStartingPlayerPosition(Vector3f directionToOpponentGoal) {
        // TODO: Left foots have negated angle
        float approachAngle = (1 + ((true ? -1 : 1) * 0.25f)) * FastMath.PI;
        Vector3f approachDirection = new Quaternion().fromAngleAxis(approachAngle, Vector3f.UNIT_Y).mult(directionToOpponentGoal);
        return ballPosition.add(approachDirection.multLocal(NearFreeKickShootButtonBehaviour.APPROACH_DURATION * NearFreeKickShootButtonBehaviour.APPROACH_SPEED));
    }
}
