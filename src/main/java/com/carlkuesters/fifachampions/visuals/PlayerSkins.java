package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.game.Player;
import com.carlkuesters.fifachampions.game.content.Players;
import com.jme3.math.ColorRGBA;

import java.util.HashMap;

public class PlayerSkins {

    private static HashMap<Player, PlayerSkin> skins = new HashMap<>();
    static {
        skins.put(Players.TIMO, new PlayerSkin("timo", "general_2", new ColorRGBA(0.55f, 0.35f, 0.1f, 1)));
        skins.put(Players.CARL, new PlayerSkin("carl", null, null));
        skins.put(Players.STEFFEN, new PlayerSkin("steffen", null, null));
        skins.put(Players.FELIX, new PlayerSkin("felix", null, null));
        skins.put(Players.MARCO, new PlayerSkin("marco", "general_2", new ColorRGBA(0.1f, 0.08f, 0.06f, 1)));
        skins.put(Players.ANDI_HUG, new PlayerSkin("andi_hug", "general_2", new ColorRGBA(0.1f, 0.1f, 0.1f, 1)));
        skins.put(Players.MARKUS, new PlayerSkin("markus", "markus", new ColorRGBA(0.1f, 0.08f, 0.06f, 1)));
        skins.put(Players.FABI_HUCK, new PlayerSkin("default", null, null));
        skins.put(Players.FABIAN_KERN, new PlayerSkin("default", null, null));
        skins.put(Players.MARIA, new PlayerSkin("default", null, null));
        skins.put(Players.JANNIK, new PlayerSkin("default", null, null));
        skins.put(Players.MATZE, new PlayerSkin("default", null, null));
        skins.put(Players.STEFAN, new PlayerSkin("stefan", "general_1", new ColorRGBA(0.12f, 0.08f, 0.06f, 1)));
        skins.put(Players.ANSELM, new PlayerSkin("default", null, null));
        skins.put(Players.LUDIN, new PlayerSkin("default", null, null));
        skins.put(Players.SAMY, new PlayerSkin("default", null, null));
        skins.put(Players.DENNIS, new PlayerSkin("default", null, null));
        skins.put(Players.ALEX, new PlayerSkin("default", null, null));
        skins.put(Players.OTT, new PlayerSkin("default", null, null));
        skins.put(Players.O2, new PlayerSkin("default", null, null));
        skins.put(Players.ANDI_FRITZ, new PlayerSkin("default", null, null));
        skins.put(Players.MARTIN, new PlayerSkin("default", null, null));
        skins.put(Players.MIRCO, new PlayerSkin("default", null, null));
        skins.put(Players.SCHLATTI, new PlayerSkin("default", null, null));
        skins.put(Players.FRIEDRICH, new PlayerSkin("default", null, null));
        skins.put(Players.TOBI, new PlayerSkin("default", null, null));
        skins.put(Players.CHRIS, new PlayerSkin("default", null, null));
        skins.put(Players.FABIAN_BUNGUS, new PlayerSkin("default", null, null));
        skins.put(Players.MIMI, new PlayerSkin("default", null, null));
        skins.put(Players.JANINA, new PlayerSkin("default", null, null));
        skins.put(Players.PHILIPP, new PlayerSkin("default", null, null));
    }

    public static PlayerSkin get(Player player) {
        return skins.get(player);
    }
}
