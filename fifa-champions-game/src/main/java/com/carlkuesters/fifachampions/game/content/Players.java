package com.carlkuesters.fifachampions.game.content;

import com.carlkuesters.fifachampions.game.FieldPlayerSkills;
import com.carlkuesters.fifachampions.game.GoalkeeperSkills;
import com.carlkuesters.fifachampions.game.Player;

import static com.carlkuesters.fifachampions.game.PlayerPosition.*;

public class Players {

    public static final Player TIMO = new Player(
        "Timo", ST,
        new FieldPlayerSkills(85, 89, 88, 88, 94, 92, 90, 86, 83),
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
    public static final Player FELIX = new Player(
        "Felix", ZOM,
        new FieldPlayerSkills(89, 88, 84, 93, 89, 92, 90, 84, 95),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player MARCO = new Player(
        "Marco", IV,
        new FieldPlayerSkills(92, 94, 95, 92, 90, 91, 95, 90, 86),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player SCHMUCK = new Player(
        "Schmuck", TW,
        new FieldPlayerSkills(99, 99, 99, 99, 99, 99, 99, 99, 99), // TODO: Skills
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player ANDI_HUG = new Player(
        "Andi Hug", ST,
        new FieldPlayerSkills(78, 81, 71, 80, 89, 81, 82, 76, 74),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player MARKUS = new Player(
        "Markus", IV,
        new FieldPlayerSkills(89, 93, 95, 95, 90, 87, 94, 93, 91),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player FABI_HUCK = new Player(
        "Fabi Huck", LV,
        new FieldPlayerSkills(79, 82, 72, 85, 86, 82, 81, 77, 86),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player FABIAN_KERN = new Player(
        "Fabian Kern", ZDM,
        new FieldPlayerSkills(81, 84, 83, 74, 83, 72, 78, 80, 72),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player MARIA = new Player(
        "Maria", ST,
        new FieldPlayerSkills(79, 80, 82, 82, 70, 78, 72, 71, 70),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player JANNIK = new Player(
        "Jannik", ZM,
        new FieldPlayerSkills(89, 89, 93, 94, 88, 93, 89, 83, 90),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player MATZE = new Player(
        "Matze", IV,
        new FieldPlayerSkills(73, 74, 70, 86, 93, 91, 88, 74, 80),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player STEFAN = new Player(
        "Stefan", LV,
        new FieldPlayerSkills(84, 87, 86, 83, 80, 78, 90, 81, 76),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player ANSELM = new Player(
        "Anselm", ZDM,
        new FieldPlayerSkills(81, 85, 95, 84, 82, 82, 79, 74, 80),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player LUDIN = new Player(
        "Ludin", LM,
        new FieldPlayerSkills(82, 88, 79, 84, 93, 86, 89, 88, 83),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player SAMY = new Player(
        "Samy", LV,
        new FieldPlayerSkills(88, 90, 88, 86, 88, 87, 90, 86, 88),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player DENNIS = new Player(
        "Dennis", RM,
        new FieldPlayerSkills(90, 88, 88, 88, 86, 87, 86, 87, 91),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player ALEX = new Player(
        "Alex", IV,
        new FieldPlayerSkills(83, 84, 87, 80, 82, 76, 78, 79, 75),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player OTT = new Player(
        "Ott", ST,
        new FieldPlayerSkills(75, 77, 87, 70, 79, 81, 74, 79, 72),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player O2 = new Player(
        "O2", ST,
        new FieldPlayerSkills(77, 79, 78, 71, 73, 70, 73, 72, 71),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player ANDI_FRITZ = new Player(
        "Andi Fritz", ZDM,
        new FieldPlayerSkills(84, 86, 88, 81, 90, 83, 86, 82, 80),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player MARTIN = new Player(
        "Martin", RV,
        new FieldPlayerSkills(75, 80, 73, 79, 92, 77, 81, 76, 75),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player MIRCO = new Player(
        "Mirco", ST,
        new FieldPlayerSkills(85, 88, 82, 90, 90, 92, 86, 84, 93),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player SCHLATTI = new Player(
        "Schlatti", ST,
        new FieldPlayerSkills(78, 84, 81, 85, 88, 86, 82, 83, 84),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player FRIEDRICH = new Player(
        "Friedrich", RV,
        new FieldPlayerSkills(77, 81, 82, 79, 89, 79, 84, 82, 73),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player TOBI = new Player(
        "Tobi", ZDM,
        new FieldPlayerSkills(75, 79, 72, 80, 84, 75, 79, 77, 72),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player CHRIS = new Player(
        "Chris", ZDM,
        new FieldPlayerSkills(81, 83, 83, 80, 82, 81, 85, 84, 76),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player FABIAN_BUNGUS = new Player(
        "Fabian Bungus", IV,
        new FieldPlayerSkills(77, 79, 77, 79, 81, 80, 81, 79, 75),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player MIMI = new Player(
        "Mimi", ST,
        new FieldPlayerSkills(50, 50, 50, 50, 50, 50, 50, 50, 50),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player JANINA = new Player(
        "Janina", ST,
        new FieldPlayerSkills(50, 50, 50, 50, 50, 50, 50, 50, 50),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player PHILIPP = new Player(
        "Philipp", ST,
        new FieldPlayerSkills(50, 50, 50, 50, 50, 50, 50, 50, 50),
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player KARL = new Player(
        "Karl", IV,
        new FieldPlayerSkills(99, 99, 99, 99, 99, 99, 99, 99, 99), // TODO: Skills
        new GoalkeeperSkills(99, 99, 99, 99)
    );
    public static final Player RAPHI = new Player(
        "Raphi", LV, // TODO: Position
        new FieldPlayerSkills(99, 99, 99, 99, 99, 99, 99, 99, 99), // TODO: Skills
        new GoalkeeperSkills(99, 99, 99, 99)
    );
}
