package com.carlkuesters.fifachampions.cinematics.cinematics;

import com.carlkuesters.fifachampions.cinematics.Cinematic;
import com.carlkuesters.fifachampions.cinematics.CinematicPart;
import com.carlkuesters.fifachampions.cinematics.actions.CameraPathAction;
import com.carlkuesters.fifachampions.game.Game;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;

public class GoalCinematic extends Cinematic {

    public GoalCinematic() {
        super(false, new CinematicPart[] {
            new CinematicPart(0, new CameraPathAction(new MotionEvent() {{
                setPath(new MotionPath() {{
                    addWayPoint(new Vector3f(-26, 5, 17));
                    addWayPoint(new Vector3f(-26, 5, -17));
                }});
                setLookAt(new Vector3f(-1 * Game.FIELD_HALF_WIDTH, (Game.GOAL_HEIGHT / 2), 0), Vector3f.UNIT_Y);
                setDirectionType(Direction.LookAt);
                setSpeed(2);
            }})),
        });
    }
}
