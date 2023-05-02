package com.carlkuesters.fifachampions.game.controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControllerSettings {
    private int buttonIndex_FlankOrStraddle = ControllerButtonPS5.SQUARE.getButtonIndex();
    private int buttonIndex_PassDirectOrPressure = ControllerButtonPS5.X.getButtonIndex();
    private int buttonIndex_Shoot = ControllerButtonPS5.CIRCLE.getButtonIndex();
    private int buttonIndex_PassInRunOrGoalkeeperPressure = ControllerButtonPS5.TRIANGLE.getButtonIndex();
    private int buttonIndex_SwitchPlayer = ControllerButtonPS5.L1.getButtonIndex();
    private int buttonIndex_Sprint = ControllerButtonPS5.R2.getButtonIndex();
}
