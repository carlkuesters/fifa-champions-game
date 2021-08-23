package com.carlkuesters.fifachampions.cinematics.cinematics;

import com.carlkuesters.fifachampions.cinematics.Cinematic;
import com.carlkuesters.fifachampions.cinematics.CinematicPart;
import com.carlkuesters.fifachampions.cinematics.actions.CameraPathAction;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class MainMenuCinematic extends Cinematic {

    public MainMenuCinematic() {
        loop = true;
        addPart(new CinematicPart(0, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-85, 25, -50));
                addWayPoint(new Vector3f(40, 17, 20));
            }});
            setDirectionType(Direction.Path);
            setSpeed(0.5f);
        }})));
        addPart(new CinematicPart(16, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(35, 6, -45));
                addWayPoint(new Vector3f(-59, 6, -6));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion().fromAngleAxis(0, Vector3f.UNIT_Y));
            setSpeed(0.5f);
        }})));
        addPart(new CinematicPart(36, new CameraPathAction(new MotionEvent() {{
                setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-50, 95, -20));
                addWayPoint(new Vector3f(50, 95, 10));
            }});
            setDirectionType(Direction.Rotation);
            setRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X));
            setSpeed(0.5f);
        }})));
    }
}
