package com.carlkuesters.fifachampions.game;

import com.carlkuesters.fifachampions.game.buttons.*;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ChargedBallButtonBehaviour;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ChargedButtonBehaviour;
import com.jme3.math.Vector2f;
import lombok.Getter;

import java.util.HashMap;

public class Controller implements GameLoopListener {

    public Controller(Game game) {
        this.game = game;
        registerButton(0, new FlankOrStraddleButton());
        registerButton(1, new PassDirectOrPressureButton());
        registerButton(2, new ShootButton());
        registerButton(3, new PassInRunOrGoalkeeperPressureButton());
        registerButton(4, new SwitchPlayerButton());
        registerButton(7, new SprintButton());
    }
    @Getter
    private Game game;
    private Team team;
    private PlayerObject playerObject;
    private Vector2f targetDirection = new Vector2f();
    private boolean isSprinting;
    private HashMap<Integer, ControllerButton> buttons = new HashMap<>();

    @Override
    public void update(float tpf) {
        if (team != null) {
            for (ControllerButton button : buttons.values()) {
                ControllerButtonBehaviour buttonBehaviour = button.getBehaviour();
                if (buttonBehaviour != null) {
                    buttonBehaviour.setController(this);
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
    }

    private void registerButton(int buttonIndex, ControllerButton button) {
        button.setController(this);
        buttons.put(buttonIndex, button);
    }

    public void setTeam(Team team) {
        this.team = team;
        if (team != null) {
            switchPlayer();
        } else {
            setPlayer(null);
        }
    }

    public Team getTeam() {
        return team;
    }
    
    public void onButtonPressed(int buttonIndex, boolean isPressed) {
        if (team != null) {
            ControllerButton button = buttons.get(buttonIndex);
            if (button != null) {
                ControllerButtonBehaviour behaviour = button.getBehaviour();
                if (behaviour != null) {
                    button.getBehaviour().onPressed(isPressed);
                }
            }
        }
    }

    public void switchPlayer() {
        game.switchToNearestSwitchablePlayer(this);
    }

    public void setPlayer(PlayerObject playerObject) {
        if (this.playerObject != null) {
            this.playerObject.setController(null);
        }
        if (playerObject != null) {
            playerObject.setController(this);
        }
        this.playerObject = playerObject;
    }

    public PlayerObject getPlayerObject() {
        return playerObject;
    }

    public void setTargetDirection(float x, float y) {
        this.targetDirection.set(x, y);
    }

    public Vector2f getTargetDirection() {
        return targetDirection;
    }

    public void setIsSprinting(boolean isSprinting) {
        this.isSprinting = isSprinting;
        playerObject.setIsSprinting(isSprinting);
    }

    public boolean isSprinting() {
        return isSprinting;
    }

    public boolean isChargingBallButton() {
        for (ControllerButton button : buttons.values()) {
            ControllerButtonBehaviour buttonBehaviour = button.getBehaviour();
            if (buttonBehaviour instanceof ChargedBallButtonBehaviour) {
                ChargedBallButtonBehaviour preChargeableBallButtonBehaviour = (ChargedBallButtonBehaviour) buttonBehaviour;
                if (preChargeableBallButtonBehaviour.isCharging()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean triggerCurrentOrRecentBallCharge() {
        for (ControllerButton button : buttons.values()) {
            ControllerButtonBehaviour buttonBehaviour = button.getBehaviour();
            if (buttonBehaviour instanceof ChargedBallButtonBehaviour) {
                ChargedBallButtonBehaviour preChargeableBallButtonBehaviour = (ChargedBallButtonBehaviour) buttonBehaviour;
                if (preChargeableBallButtonBehaviour.triggerCurrentOrRecentCharge()) {
                    return true;
                }
            }
        }
        return false;
    }

    public ChargedButtonBehaviour getChargingButtonBehaviour() {
        for (ControllerButton button : buttons.values()) {
            ControllerButtonBehaviour buttonBehaviour = button.getBehaviour();
            if (buttonBehaviour instanceof ChargedButtonBehaviour) {
                ChargedButtonBehaviour chargedButtonBehaviour = (ChargedButtonBehaviour) buttonBehaviour;
                if (chargedButtonBehaviour.isCharging()) {
                    return chargedButtonBehaviour;
                }
            }
        }
        return null;
    }
}