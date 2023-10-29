package com.carlkuesters.fifachampions.replay;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import lombok.Getter;

@Getter
public class PlayerReplayState extends PhysicsReplayState {

    public PlayerReplayState(Vector3f position, Quaternion rotation, boolean displayed, String animationName, float animationTime) {
        super(position, rotation);
        this.displayed = displayed;
        this.animationName = animationName;
        this.animationTime = animationTime;
    }
    private boolean displayed;
    private String animationName;
    private float animationTime;
}
