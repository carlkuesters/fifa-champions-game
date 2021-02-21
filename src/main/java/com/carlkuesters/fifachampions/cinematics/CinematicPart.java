package com.carlkuesters.fifachampions.cinematics;

import lombok.Getter;
import lombok.Setter;

public class CinematicPart {

    public CinematicPart(float startTime, CinematicAction cinematicAction) {
        this.startTime = startTime;
        this.cinematicAction = cinematicAction;
    }
    @Getter
    private float startTime;
    @Getter
    private CinematicAction cinematicAction;
    @Getter
    @Setter
    private boolean triggered;
}
