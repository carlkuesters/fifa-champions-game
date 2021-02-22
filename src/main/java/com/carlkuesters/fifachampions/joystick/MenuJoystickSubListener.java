package com.carlkuesters.fifachampions.joystick;

import com.carlkuesters.fifachampions.menu.MenuGroup;
import com.jme3.input.JoystickAxis;
import com.jme3.input.event.*;

import java.util.HashMap;
import java.util.function.Function;

public class MenuJoystickSubListener {

    public MenuJoystickSubListener(Runnable back, Function<Integer, MenuGroup> getControllerMenuGroup) {
        this.back = back;
        this.getControllerMenuGroup = getControllerMenuGroup;
    }
    private Runnable back;
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
                        menuGroup.primaryNavigateRight();
                    } else if (value < 0) {
                        menuGroup.primaryNavigateLeft();
                    }
                }

            } else if (axis == axis.getJoystick().getPovYAxis()) {
                if (value != values[1]) {
                    values[1] = value;
                    if (value > 0) {
                        menuGroup.primaryNavigateUp();
                    } else if (value < 0) {
                        menuGroup.primaryNavigateDown();
                    }
                }
            }
        }
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
        MenuGroup menuGroup = getControllerMenuGroup.apply(evt.getJoyIndex());
        if (evt.isPressed()) {
            if (menuGroup != null) {
                switch (evt.getButtonIndex()) {
                    case 0:
                    case 1:
                        menuGroup.confirm();
                        break;
                    case 4:
                        menuGroup.secondaryNavigateLeft();
                        break;
                    case 5:
                        menuGroup.secondaryNavigateRight();
                        break;
                }
            }
            switch (evt.getButtonIndex()) {
                case 2:
                case 9:
                    back.run();
                    break;
            }
        }
    }
}
