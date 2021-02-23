package com.carlkuesters.fifachampions.game.content.formations;

import com.carlkuesters.fifachampions.game.Formation;
import com.jme3.math.Vector2f;

public class Formation442 extends Formation {

    public Formation442() {
        super(
            new Vector2f(-1, 0),

            new Vector2f(-0.7f, -0.75f),
            new Vector2f(-0.7f, -0.25f),
            new Vector2f(-0.7f, 0.25f),
            new Vector2f(-0.7f, 0.75f),

            new Vector2f(0, -0.75f),
            new Vector2f(0, -0.25f),
            new Vector2f(0, 0.25f),
            new Vector2f(0, 0.75f),

            new Vector2f(0.7f, -0.5f),
            new Vector2f(0.7f, 0.5f)
        );
    }
}
