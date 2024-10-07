package com.carlkuesters.fifachampions.game.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ControllerButtonPS5 {
    SQUARE(0, "Viereck"),
    X(1, "X"),
    CIRCLE(2, "Kreis"),
    TRIANGLE(3, "Dreieck"),
    L1(4, "L1"),
    R1(5, "R1"),
    L2(6, "L2"),
    R2(7, "R2"),
    SHARE(8, "Teilen"),
    OPTIONS(9, "Optionen"),
    L3(10, "L3"),
    R3(11, "R3"),
    PS(12, "PS"),
    TOUCH(13, "Touch"),
    MICROPHONE(14, "Mikrofon"),
    UP(15, "Oben"),
    RIGHT(16, "Rechts"),
    DOWN(17, "Unten"),
    LEFT(18, "Links");

    private int buttonIndex;
    private String name;

    public static ControllerButtonPS5 byButtonIndex(int buttonIndex) {
        for (ControllerButtonPS5 button : values()) {
            if (button.getButtonIndex() == buttonIndex) {
                return button;
            }
        }
        return null;
    }
}
