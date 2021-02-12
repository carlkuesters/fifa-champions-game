package com.carlkuesters.fifachampions.menu;

import com.jme3.input.JoystickAxis;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;

import java.util.HashMap;
import java.util.function.Function;

public class MenuJoystickListener implements RawInputListener {

    public MenuJoystickListener(Function<Integer, MenuGroup> getControllerMenuGroup) {
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
                        menuGroup.navigateRight();
                    } else if (value < 0) {
                        menuGroup.navigateLeft();
                    }
                }

            } else if (axis == axis.getJoystick().getPovYAxis()) {
                if (value != values[1]) {
                    values[1] = value;
                    if (value > 0) {
                        menuGroup.navigateUp();
                    } else if (value < 0) {
                        menuGroup.navigateDown();
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
                    menuGroup.confirm();
                    break;
                case 2:
                    menuGroup.back();
                    break;
            }
        }
    }

    public void beginInput() {}
    public void endInput() {}
    public void onMouseMotionEvent(MouseMotionEvent evt) {}
    public void onMouseButtonEvent(MouseButtonEvent evt) {}
    public void onKeyEvent(KeyInputEvent evt) {}
    public void onTouchEvent(TouchEvent evt) {}
}