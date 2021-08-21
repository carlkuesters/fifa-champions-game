package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector3f;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OffsidePlayer {
    private PlayerObject playerObject;
    private Vector3f offsidePosition;
}
