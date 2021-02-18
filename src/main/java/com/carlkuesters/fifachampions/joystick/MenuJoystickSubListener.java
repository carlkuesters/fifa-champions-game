package com.carlkuesters.fifachampions.joystick;

import com.carlkuesters.fifachampions.menu.MenuGroup;
import com.jme3.input.JoystickAxis;
import com.jme3.input.event.*;

import java.util.HashMap;
import java.util.function.Function;

public class MenuJoystickSubListener {

    public MenuJoystickSubListener(Function<Integer, MenuGroup> getControllerMenuGroup) {
        this.getControllerMenuGroup = getControllerMenuGroup;
    }
    private Function<Integer, MenuGroup> getControllerMenuGroup;
    private HashMap<Integer, float[]> axes = new HashMap<>();

    public void onJoyAxisEvent(JoyAxisEvent evt) {
        MenuGroup menuGroup = getControllerMenuGroup.apply(evt.getJoyIndex());
        if (menuGroup != null) {
            float[] values = axes.computeIfAbsent(evt.getJoyIndex(), ji -> new float[2]);
            JoystickAxis axis = evt.getAxis();
            float value = evt.getValue();
            if (axis == axis.getJoystick().getPovXAxis()) {
                if (value != values[0]) {
                    values[0] = value;
                    if (value > 0) {
                        menuGroup.primaryNavigateRight(evt.getJoyIndex());
                    } else if (value < 0) {
                        menuGroup.primaryNavigateLeft(evt.getJoyIndex());
                    }
                }

            } else if (axis == axis.getJoystick().getPovYAxis()) {
                if (value != values[1]) {
                    values[1] = value;
                    if (value > 0) {
                        menuGroup.primaryNavigateUp(evt.getJoyIndex());
                    } else if (value < 0) {
                        menuGroup.primaryNavigateDown(evt.getJoyIndex());
                    }
                }
            }
        }
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
        MenuGroup menuGroup = getControllerMenuGroup.apply(evt.getJoyIndex());
        if ((menuGroup != null) && evt.isPressed()) {
            switch (evt.getButtonIndex()) {
                case 0:
                case 1:
                    menuGroup.confirm(evt.getJoyIndex());
                    break;
                case 4:
                    menuGroup.secondaryNavigateLeft(evt.getJoyIndex());
                    break;
                case 5:
                    menuGroup.secondaryNavigateRight(evt.getJoyIndex());
                    break;
                case 2:
                case 9:
                    menuGroup.back();
                    break;
            }
        }
    }
}
