package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.game.Player;
import com.carlkuesters.fifachampions.game.content.Players;
import com.jme3.math.ColorRGBA;

import java.util.Collection;
import java.util.HashMap;

public class PlayerSkins {

    private static HashMap<Player, PlayerSkin> skins = new HashMap<>();
    static {
        skins.put(Players.TIMO, new PlayerSkin("timo", "general_2", new ColorRGBA(0.55f, 0.35f, 0.1f, 1)));
        skins.put(Players.CARL, new PlayerSkin("carl", null, null));
        skins.put(Players.STEFAN, new PlayerSkin("stefan", "general_1", new ColorRGBA(0.12f, 0.08f, 0.06f, 1)));
        skins.put(Players.MARKUS, new PlayerSkin("markus", "markus", new ColorRGBA(0.1f, 0.08f, 0.06f, 1)));
        skins.put(Players.MARCO, new PlayerSkin("marco", "general_2", new ColorRGBA(0.1f, 0.08f, 0.06f, 1)));
        skins.put(Players.SCHMUCK, new PlayerSkin("schmuck", null, null));
        skins.put(Players.SAMY, new PlayerSkin("samy", null, null));
        skins.put(Players.ORHAN, new PlayerSkin("orhan", null, null));
        skins.put(Players.ANDI_HUG, new PlayerSkin("andi_hug", "general_2", new ColorRGBA(0.1f, 0.1f, 0.1f, 1)));
        skins.put(Players.LUDIN, new PlayerSkin("ludin", "general_2", new ColorRGBA(0.65f, 0.45f, 0.2f, 1)));
        skins.put(Players.MARIA, new PlayerSkin("maria", "female_ponytail", new ColorRGBA(0.55f, 0.35f, 0.1f, 1)));
        skins.put(Players.FABIAN_KERN, new PlayerSkin("fabian_kern", "general_2", new ColorRGBA(0.25f, 0.12f, 0.12f, 1)));
        skins.put(Players.ALEX, new PlayerSkin("alex", "general_1", new ColorRGBA(0.1f, 0.08f, 0.06f, 1)));
        skins.put(Players.FRIEDRICH, new PlayerSkin("friedrich", "general_1", new ColorRGBA(0.18f, 0.11f, 0.07f, 1)));
        skins.put(Players.DENNIS, new PlayerSkin("dennis", null, null));
        skins.put(Players.ALEX_STEHLE, new PlayerSkin("alex_stehle", null, null));
        skins.put(Players.OTT,new PlayerSkin("ott", "general_2", new ColorRGBA(0.7f, 0.43f, 0.2f, 1)));
        skins.put(Players.O2, new PlayerSkin("o2", "general_2", new ColorRGBA(0.19f, 0.12f, 0.05f, 1)));
        skins.put(Players.ANDI_FRITZ, new PlayerSkin("andi_fritz", "general_1", new ColorRGBA(0.1f, 0.1f, 0.1f, 1)));
        skins.put(Players.ANSELM, new PlayerSkin("anselm", null, null));
        skins.put(Players.MIMI, new PlayerSkin("mimi", "female_ponytail", new ColorRGBA(0.57f, 0.45f, 0.1f, 1)));
        skins.put(Players.FELIX, new PlayerSkin("felix", null, null));
        skins.put(Players.RAPHI, new PlayerSkin("raphi", null, null));
        skins.put(Players.TOBI, new PlayerSkin("tobi", null, null));
        skins.put(Players.JANINA, new PlayerSkin("janina", "female_ponytail", new ColorRGBA(0.92f, 0.89f, 0.78f, 1)));
        skins.put(Players.SCHLATTI, new PlayerSkin("schlatti", "general_1", new ColorRGBA(0.48f, 0.31f, 0.1f, 1)));
        skins.put(Players.MARTIN, new PlayerSkin("martin", null, null));
        skins.put(Players.ERWIN, new PlayerSkin("erwin", null, null));
        skins.put(Players.FABI_HUCK, new PlayerSkin("fabi_huck", null, null));
        skins.put(Players.STEFFEN, new PlayerSkin("steffen", null, null));
        skins.put(Players.JANNIK, new PlayerSkin("jannik", "general_2",  new ColorRGBA(0.19f, 0.12f, 0.05f, 1)));
        skins.put(Players.MIRCO, new PlayerSkin("mirco", "general_2", new ColorRGBA(0.6f, 0.33f, 0.13f, 1)));
        skins.put(Players.JENS, new PlayerSkin("default", null, null));
        skins.put(Players.MATZE, new PlayerSkin("matze", "general_1", new ColorRGBA(0.2f, 0.15f, 0.05f, 1)));
        skins.put(Players.CHRIS, new PlayerSkin("chris", null, null));
        skins.put(Players.FABIAN_BUNGUS, new PlayerSkin("fabian_bungus", "general_1", new ColorRGBA(0.5f, 0.33f, 0.1f, 1)));
        skins.put(Players.PHILIPP, new PlayerSkin("philipp", "general_1", new ColorRGBA(0.1f, 0.1f, 0.1f, 1)));
        skins.put(Players.KARL, new PlayerSkin("karl", "general_2", new ColorRGBA(0.31f, 0.19f, 0.06f, 1)));
        skins.put(Players.SANDRO, new PlayerSkin("sandro", "general_2", new ColorRGBA(0.1f, 0.1f, 0.1f, 1)));
    }

    public static PlayerSkin get(Player player) {
        return skins.get(player);
    }

    public static Collection<PlayerSkin> getAllSkins() {
        return skins.values();
    }
}
