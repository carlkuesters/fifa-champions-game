package com.carlkuesters.fifachampions.game;

import com.carlkuesters.fifachampions.game.controllers.ControllerSettings;
import com.jme3.input.Joystick;
import com.jme3.math.Vector2f;
import lombok.Getter;
import lombok.Setter;

public class Controller implements GameLoopListener {

    public Controller(Joystick joystick, ControllerSettings settings) {
        this.joystick = joystick;
        this.settings = settings;
        buttons = new ControllerButtonMap(this);
        targetDirection = new Vector2f();
    }
    @Getter
    private Joystick joystick;
    @Getter
    @Setter
    private ControllerSettings settings;
    @Getter
    private ControllerButtonMap buttons;
    @Getter
    private Game game;
    @Getter
    private int teamSide;
    @Getter
    private PlayerObject playerObject;
    @Getter
    private Vector2f targetDirection;
    @Getter
    private boolean isSprinting;

    public void resetForNewGame(Game game) {
        this.game = game;
        switchOrClearPlayer();
        targetDirection.set(0, 0);
        isSprinting = false;
    }

    public void setTeamSide(int teamSide) {
        this.teamSide = teamSide;
        if (game != null) {
            switchOrClearPlayer();
        }
    }

    private void switchOrClearPlayer() {
        if (teamSide != 0) {
            switchPlayer();
        } else {
            setPlayer(null);
        }
    }

    public Team getTeam() {
        Integer teamIndex = getTeamIndex();
        return ((teamIndex != null) ? game.getTeams()[teamIndex] : null);
    }

    public Integer getTeamIndex() {
        switch (teamSide) {
            case 1: return 0;
            case -1: return 1;
            default: return null;
        }
    }

    @Override
    public void update(float tpf) {
        if (teamSide != 0) {
            buttons.update(tpf);
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

    public void setTargetDirection(float x, float y) {
        this.targetDirection.set(x, y);
    }

    public void setIsSprinting(boolean isSprinting) {
        this.isSprinting = isSprinting;
        playerObject.setIsSprinting(isSprinting);
    }
}
