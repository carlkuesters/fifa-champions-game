package com.carlkuesters.fifachampions.replay;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PhysicsReplayState {
    private Vector3f position;
    private Quaternion rotation;
}
