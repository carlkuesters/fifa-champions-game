package com.carlkuesters.fifachampions.game;

import com.carlkuesters.fifachampions.game.buttons.*;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ChargedBallButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ChargedButtonBehaviour;
import com.carlkuesters.fifachampions.game.controllers.ControllerSettings;

import java.util.function.Predicate;

public class ControllerButtonMap implements GameLoopListener {

    public ControllerButtonMap(Controller controller) {
        this.controller = controller;
        passDirectOrPressureButton = new PassDirectOrPressureButton(controller);
        passInRunOrGoalkeeperPressureButton = new PassInRunOrGoalkeeperPressureButton(controller);
        flankOrStraddleButton = new FlankOrStraddleButton(controller);
        shootButton = new ShootButton(controller);
        sprintButton = new SprintButton(controller);
        switchPlayerButton = new SwitchPlayerButton(controller);
        buttons = new ControllerButton[] {
            passDirectOrPressureButton,
            passInRunOrGoalkeeperPressureButton,
            flankOrStraddleButton,
            shootButton,
            sprintButton,
            switchPlayerButton,
        };
    }
    private Controller controller;
    private PassDirectOrPressureButton passDirectOrPressureButton;
    private PassInRunOrGoalkeeperPressureButton passInRunOrGoalkeeperPressureButton;
    private FlankOrStraddleButton flankOrStraddleButton;
    private ShootButton shootButton;
    private SprintButton sprintButton;
    private SwitchPlayerButton switchPlayerButton;
    private ControllerButton[] buttons;

    @Override
    public void update(float tpf) {
        for (ControllerButton button : buttons) {
            ControllerButtonBehaviour buttonBehaviour = button.getBehaviour();
            if (buttonBehaviour != null) {
                if (buttonBehaviour instanceof GameLoopListener) {
                    GameLoopListener buttonBehaviourGameLoopListener = (GameLoopListener) buttonBehaviour;
                    buttonBehaviourGameLoopListener.update(tpf);
                }
                if (buttonBehaviour.isTriggered()) {
                    buttonBehaviour.trigger();
                }
            }
        }
    }

    public void onButtonPressed(int buttonIndex, boolean isPressed) {
        ControllerButton button = getButton(buttonIndex);
        if (button != null) {
            ControllerButtonBehaviour behaviour = button.getBehaviour();
            if (behaviour != null) {
                button.getBehaviour().onPressed(isPressed);
            }
        }
    }

    private ControllerButton getButton(int buttonIndex) {
        ControllerSettings settings = controller.getSettings();
        if (buttonIndex == settings.getButtonIndex_PassDirectOrPressure()) {
            return passDirectOrPressureButton;
        } else if (buttonIndex == settings.getButtonIndex_PassInRunOrGoalkeeperPressure()) {
            return passInRunOrGoalkeeperPressureButton;
        } else if (buttonIndex == settings.getButtonIndex_FlankOrStraddle()) {
            return flankOrStraddleButton;
        } else if (buttonIndex == settings.getButtonIndex_Shoot()) {
            return shootButton;
        } else if (buttonIndex == settings.getButtonIndex_Sprint()) {
            return sprintButton;
        } else if (buttonIndex == settings.getButtonIndex_SwitchPlayer()) {
            return switchPlayerButton;
        }
        return null;
    }

    public boolean isChargingBallButton() {
        return getButtonBehaviour(ChargedBallButtonBehaviour.class, ChargedBallButtonBehaviour::isCharging) != null;
    }

    public boolean triggerCurrentOrRecentBallCharge() {
        return getButtonBehaviour(ChargedBallButtonBehaviour.class, ChargedButtonBehaviour::triggerCurrentOrRecentCharge) != null;
    }

    public ChargedButtonBehaviour getChargingButtonBehaviour() {
        return getButtonBehaviour(ChargedButtonBehaviour.class, ChargedButtonBehaviour::isCharging);
    }

    private <T extends ControllerButtonBehaviour> T getButtonBehaviour(Class<T> buttonBehaviourCLass, Predicate<T> filter) {
        for (ControllerButton button : buttons) {
            ControllerButtonBehaviour buttonBehaviour = button.getBehaviour();
            if ((buttonBehaviour != null) && buttonBehaviourCLass.isAssignableFrom(buttonBehaviour.getClass())) {
                T castedButtonBehaviour = (T) buttonBehaviour;
                if (filter.test(castedButtonBehaviour)) {
                    return castedButtonBehaviour;
                }
            }
        }
        return null;
    }
}
