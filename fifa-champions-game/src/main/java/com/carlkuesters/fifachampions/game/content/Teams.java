package com.carlkuesters.fifachampions.game.content;

import com.carlkuesters.fifachampions.game.Player;
import com.carlkuesters.fifachampions.game.TeamInfo;
import com.carlkuesters.fifachampions.game.content.formations.Formation442;

import static com.carlkuesters.fifachampions.game.content.Players.*;

public class Teams {

    public static TeamInfo FC_CHAMPIONS = new TeamInfo(
        "Fifa-Champions",
        "FC",
        new String[] { "amaranth", "red", "striped", "thinstripes", "yellow" },
        new Player[] {
            SCHMUCK,
            SAMY, MARCO, MARKUS, CARL,
            STEFFEN, FELIX, JANNIK, DENNIS,
            TIMO, MIRCO
        },
        new Player[] {
            ANDI_HUG, FABI_HUCK, FABIAN_KERN, MARIA, MATZE,
            STEFAN, ANSELM, LUDIN, ALEX, OTT,
            O2, ANDI_FRITZ, MARTIN, SCHLATTI, FRIEDRICH,
            TOBI, CHRIS, FABIAN_BUNGUS, MIMI, PHILIPP,
            // TODO: Karl
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
