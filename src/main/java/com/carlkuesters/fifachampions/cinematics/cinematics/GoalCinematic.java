package com.carlkuesters.fifachampions.cinematics.cinematics;

import com.carlkuesters.fifachampions.cinematics.Cinematic;
import com.carlkuesters.fifachampions.cinematics.CinematicPart;
import com.carlkuesters.fifachampions.cinematics.actions.AttachSpatialAction;
import com.carlkuesters.fifachampions.cinematics.actions.CameraPathAction;
import com.carlkuesters.fifachampions.game.Game;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;

public class GoalCinematic extends Cinematic {

    @Override
    protected void initialize(SimpleApplication simpleApplication) {
        super.initialize(simpleApplication);

        int totalWidth = simpleApplication.getContext().getSettings().getWidth();

        int containerMarginBottom = 120;
        int containerWidth = 700;
        int containerHeight = 200;
        int containerX = ((totalWidth / 2) - (containerWidth / 2));
        int containerY = containerMarginBottom + containerHeight;

        Container container = new Container();
        container.setLocalTranslation(containerX, containerY, 0);
        container.setPreferredSize(new Vector3f(containerWidth, containerHeight, 0));

        Label label = new Label("Tor!");
        label.setLocalTranslation(containerX, containerY, 0);
        label.setPreferredSize(new Vector3f(containerWidth, containerHeight, 1));
        label.setTextHAlignment(HAlignment.Center);
        label.setTextVAlignment(VAlignment.Center);
        label.setFontSize(32);
        label.setColor(ColorRGBA.White);

        addPart(new CinematicPart(0, new CameraPathAction(new MotionEvent() {{
            setPath(new MotionPath() {{
                addWayPoint(new Vector3f(-26, 5, 17));
                addWayPoint(new Vector3f(-26, 5, -17));
            }});
            setLookAt(new Vector3f(-1 * Game.FIELD_HALF_WIDTH, (Game.GOAL_HEIGHT / 2), 0), Vector3f.UNIT_Y);
            setDirectionType(Direction.LookAt);
            setSpeed(2);
        }})));
        addPart(new CinematicPart(0, new AttachSpatialAction(guiNode, container, label)));
    }
}
