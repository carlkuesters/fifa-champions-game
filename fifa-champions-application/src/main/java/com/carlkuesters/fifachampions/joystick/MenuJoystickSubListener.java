package com.carlkuesters.fifachampions.joystick;

import com.carlkuesters.fifachampions.game.controllers.PS5Controller;
import com.carlkuesters.fifachampions.menu.MenuGroup;
import com.jme3.input.JoystickAxis;
import com.jme3.input.event.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenuJoystickSubListener {

    public MenuJoystickSubListener(Runnable back, Function<Integer, MenuGroup> getControllerMenuGroup) {
        this.back = back;
        this.getControllerMenuGroup = getControllerMenuGroup;
    }
    private Runnable back;
    private Function<Integer, MenuGroup> getControllerMenuGroup;
    private HashMap<Integer, float[]> axes = new HashMap<>();
    @Getter
    @Setter
    private Consumer<Integer> buttonRecorder;

    public void onJoyAxisEvent(JoyAxisEvent evt) {
        if (buttonRecorder == null) {
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
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
        if (evt.isPressed()) {
            if (buttonRecorder != null) {
                Consumer<Integer> tmpButtonRecorder = buttonRecorder;
                buttonRecorder = null;
                tmpButtonRecorder.accept(evt.getButtonIndex());
            } else {
                MenuGroup menuGroup = getControllerMenuGroup.apply(evt.getJoyIndex());
                if (menuGroup != null) {
                    switch (evt.getButtonIndex()) {
                        case PS5Controller.CROSS:
                        case PS5Controller.SQUARE:
                            menuGroup.confirm();
                            break;
                        case PS5Controller.L1:
                            menuGroup.secondaryNavigateLeft();
                            break;
                        case PS5Controller.R1:
                            menuGroup.secondaryNavigateRight();
                            break;
                    }
                }
                switch (evt.getButtonIndex()) {
                    case PS5Controller.CIRCLE:
                    case PS5Controller.OPTIONS:
                        back.run();
                        break;
                }
            }
        }
    }
}
