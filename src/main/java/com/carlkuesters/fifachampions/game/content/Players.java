package com.carlkuesters.fifachampions.game.content;

import com.carlkuesters.fifachampions.game.FieldPlayerSkills;
import com.carlkuesters.fifachampions.game.GoalkeeperSkills;
import com.carlkuesters.fifachampions.game.Player;

import static com.carlkuesters.fifachampions.game.PlayerPosition.*;

public class Players {

    public static final Player TIMO = new Player(
        "Timo", ST,
        new FieldPlayerSkills(85, 90, 88, 88, 94, 92, 90, 86, 83),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player CARL = new Player(
        "Carl", RV,
        new FieldPlayerSkills(90, 89, 90, 85, 82, 80, 86, 90, 81),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player STEFFEN = new Player(
        "Steffen", LM,
        new FieldPlayerSkills(84, 85, 89, 90, 88, 89, 86, 82, 83),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
}
