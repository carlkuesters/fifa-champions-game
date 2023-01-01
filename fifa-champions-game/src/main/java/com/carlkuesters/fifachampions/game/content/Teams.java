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
            ANDI_HUG, FABI_HUCK, ALEX, MARIA, MATZE, STEFAN, ANSELM,
            LUDIN, FABIAN_KERN, OTT, ANDI_FRITZ, CHRIS, FRIEDRICH, SCHLATTI,
            FABIAN_BUNGUS, ORHAN, O2, KARL, PHILIPP, SANDRO, MARTIN,
            TOBI, ERWIN, RAPHI, ALEX_STEHLE, JENS, MIMI, JANINA
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
