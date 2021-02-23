package com.carlkuesters.fifachampions.game.content;

import com.carlkuesters.fifachampions.game.Player;
import com.carlkuesters.fifachampions.game.TeamInfo;
import com.carlkuesters.fifachampions.game.content.formations.Formation442;

import static com.carlkuesters.fifachampions.game.content.Players.*;

public class Teams {

    public static TeamInfo FC_CHAMPIONS = new TeamInfo(
        "Fifa-Champions",
        new String[] { "amaranth", "red", "striped", "thinstripes", "yellow" },
        new Player[] {
            TIMO,
            CARL, CARL, CARL, CARL,
            STEFFEN, STEFFEN, STEFFEN, STEFFEN,
            TIMO, TIMO
        },
        new Player[] {
            TIMO, TIMO, TIMO, TIMO, TIMO,
            TIMO, TIMO, TIMO, TIMO, TIMO,
            TIMO, TIMO, TIMO, TIMO, TIMO,
            TIMO, TIMO, TIMO, TIMO, TIMO,
        },
        new Formation442()
    );

    public static TeamInfo[] TEAMS = new TeamInfo[] {
        FC_CHAMPIONS,
        FC_CHAMPIONS,
        FC_CHAMPIONS,
        FC_CHAMPIONS
    };
}
