package com.carlkuesters.fifachampions.game;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GoalkeeperJump {
    private Vector3f initialVelocity;
    private Quaternion rotation;
    private float frictionXZ_Air;
    private float jumpDuration;
}
