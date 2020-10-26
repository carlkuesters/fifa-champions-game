package com.carlkuesters.fifachampions.game.situations;

import com.carlkuesters.fifachampions.game.Situation;

public class HalftimeSituation extends Situation {

    @Override
    public void start() {
        game.startSecondHalftime();
    }
}
