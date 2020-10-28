package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector3f;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CameraPerspective {
    private Vector3f position;
    private Vector3f direction;
}
