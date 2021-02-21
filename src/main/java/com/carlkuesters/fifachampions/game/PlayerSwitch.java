package com.carlkuesters.fifachampions.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerSwitch {
    private PlayerObject fieldPlayer;
    private PlayerObject reservePlayer;
}
