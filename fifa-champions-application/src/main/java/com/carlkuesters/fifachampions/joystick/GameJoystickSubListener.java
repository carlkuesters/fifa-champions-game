package com.carlkuesters.fifachampions.joystick;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.controllers.PS5Controller;
import com.jme3.input.JoystickAxis;
import com.jme3.input.event.*;
import com.jme3.math.FastMath;

import java.util.HashMap;
import java.util.Map;

public class GameJoystickSubListener {

    public GameJoystickSubListener(Map<Integer, Controller> controllers, Runnable openPauseMenu) {
        this.controllers = controllers;
        this.openPauseMenu = openPauseMenu;
        axes = new HashMap<>();
    }
    private Map<Integer, Controller> controllers;
    private Runnable openPauseMenu;
    private HashMap<Integer, float[]> axes;

    public void onJoyAxisEvent(JoyAxisEvent evt) {
        float[] values = axes.computeIfAbsent(evt.getJoyIndex(), ji -> new float[2]);
        JoystickAxis axis = evt.getAxis();
        float value = evt.getValue();
        if (axis == axis.getJoystick().getXAxis()) {
            values[0] = value;
        } else if (axis == axis.getJoystick().getYAxis()) {
            values[1] = value;
        }
        float x = 0;
        float y = 0;
        float axisX = values[0];
        float axisY = values[1];
        float minimumAxisValue = 0.0001f; // Old controller
        minimumAxisValue = 0.1f; // PS5 controller
        if ((FastMath.abs(axisX) > minimumAxisValue) || (FastMath.abs(axisY) > minimumAxisValue)) {
            float squareToCircleFactor = FastMath.sqrt((axisX * axisX) + (axisY * axisY) - (axisX * axisX * axisY * axisY)) / FastMath.sqrt((axisX * axisX) + (axisY * axisY));
            x = axisX * squareToCircleFactor;
            y = axisY * squareToCircleFactor;
        }
        Controller controller = controllers.get(evt.getJoyIndex());
        controller.setTargetDirection(x, y);
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
        if ((evt.getButtonIndex() == PS5Controller.OPTIONS) && evt.isPressed()) {
            openPauseMenu.run();
        } else {
            Controller controller = controllers.get(evt.getJoyIndex());
            controller.getButtons().onButtonPressed(evt.getButtonIndex(), evt.isPressed());
        }
    }
}