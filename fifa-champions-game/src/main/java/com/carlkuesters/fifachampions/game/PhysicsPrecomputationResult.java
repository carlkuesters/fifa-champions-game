package com.carlkuesters.fifachampions.game;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhysicsPrecomputationResult {

    public PhysicsPrecomputationResult(Vector3f position, Quaternion rotation, Vector3f velocity) {
        this.position = position;
        this.rotation = rotation;
        this.velocity = velocity;
    }
    private Vector3f position;
    private Quaternion rotation;
    private Vector3f velocity;
    private float passedTime;
}
