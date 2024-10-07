package com.carlkuesters.fifachampions.joystick;

import com.carlkuesters.fifachampions.game.controllers.ControllerButtonPS5;
import com.carlkuesters.fifachampions.menu.MenuGroup;
import com.jme3.input.JoystickAxis;
import com.jme3.input.event.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class MenuJoystickSubListener {

    public MenuJoystickSubListener(Runnable back, Runnable showDetails, Function<Integer, MenuGroup> getControllerMenuGroup) {
        this.back = back;
        this.showDetails = showDetails;
        this.getControllerMenuGroup = getControllerMenuGroup;
    }
    private Runnable back;
    private Runnable showDetails;
    private Function<Integer, MenuGroup> getControllerMenuGroup;
    private HashMap<Integer, float[]> axes = new HashMap<>();
    @Getter
    @Setter
    private Consumer<Integer> buttonRecorder;

    public void onJoyAxisEvent(JoyAxisEvent evt) {

    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
        if (evt.isPressed()) {
            int buttonIndex = evt.getButtonIndex();
            if (buttonRecorder != null) {
                Consumer<Integer> tmpButtonRecorder = buttonRecorder;
                buttonRecorder = null;
                tmpButtonRecorder.accept(buttonIndex);
            } else {
                ControllerButtonPS5 buttonPS5 = ControllerButtonPS5.byButtonIndex(buttonIndex);
                if (buttonPS5 != null) {
                    MenuGroup menuGroup = getControllerMenuGroup.apply(evt.getJoyIndex());
                    if (menuGroup != null) {
                        switch (buttonPS5) {
                            case LEFT:
                                menuGroup.primaryNavigateLeft();
                                break;
                            case RIGHT:
                                menuGroup.primaryNavigateRight();
                                break;
                            case UP:
                                menuGroup.primaryNavigateUp();
                                break;
                            case DOWN:
                                menuGroup.primaryNavigateDown();
                                break;
                            case X:
                            case SQUARE:
                                menuGroup.confirm();
                                break;
                            case L1:
                            case L2:
                                menuGroup.secondaryNavigateLeft();
                                break;
                            case R1:
                            case R2:
                                menuGroup.secondaryNavigateRight();
                                break;
                        }
                    }
                    switch (buttonPS5) {
                        case CIRCLE:
                        case OPTIONS:
                            back.run();
                            break;
                        case TRIANGLE:
                            showDetails.run();
                            break;
                    }
                }
            }
        }
    }
}
