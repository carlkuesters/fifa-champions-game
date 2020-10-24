package com.carlkuesters.fifachampions.game;

import com.jme3.math.Vector3f;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhysicsPrecomputationResult {

    public PhysicsPrecomputationResult(Vector3f position, Vector3f direction, Vector3f velocity) {
        this.position = position;
        this.direction = direction;
        this.velocity = velocity;
    }
    private Vector3f position;
    private Vector3f direction;
    private Vector3f velocity;
    private float passedTime;
}
