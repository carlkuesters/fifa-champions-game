package com.carlkuesters.fifachampions.game.controllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControllerSettings {
    private int buttonIndex_FlankOrStraddle = PS5Controller.SQUARE;
    private int buttonIndex_PassDirectOrPressure = PS5Controller.CROSS;
    private int buttonIndex_Shoot = PS5Controller.CIRCLE;
    private int buttonIndex_PassInRunOrGoalkeeperPressure = PS5Controller.TRIANGLE;
    private int buttonIndex_SwitchPlayer = PS5Controller.L1;
    private int buttonIndex_Sprint = PS5Controller.R2;
}
