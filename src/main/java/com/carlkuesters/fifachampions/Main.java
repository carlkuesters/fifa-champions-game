package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.formations.Formation433;
import com.carlkuesters.fifachampions.game.formations.Formation442;
import com.carlkuesters.fifachampions.joystick.JoystickListener;
import com.carlkuesters.fifachampions.menu.*;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.Joystick;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import lombok.Getter;

import java.util.HashMap;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.setShowSettings(false);
        app.start();
    }

    public static final TeamInfo[] TEAMS = new TeamInfo[] {
        getDefaultTeam("FC-C1"),
        getDefaultTeam("FC-C2"),
        getDefaultTeam("FC-C3"),
        getDefaultTeam("FC-C4"),
    };
    public static final Formation[] FORMATIONS = new Formation[] {
        new Formation442(),
        new Formation433(),
    };

    public Main() {
        settings = new AppSettings(true);
        settings.setWidth(1600);
        settings.setHeight(900);
        settings.setVSync(true);
        settings.setUseJoysticks(true);
    }
    @Getter
    private GameCreationInfo gameCreationInfo;
    @Getter
    private JoystickListener joystickListener;

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("../assets/", FileLocator.class);

        setDisplayStatView(false);

        gameCreationInfo = new GameCreationInfo();
        gameCreationInfo.setTeams(new InitialTeamInfo[] {
            generateInitialTeamInfo(0),
            generateInitialTeamInfo(1)
        });
        HashMap<Integer, Integer> controllerTeamSides = new HashMap<>();
        int teamSide = 1;
        for (Joystick joystick : inputManager.getJoysticks()) {
            controllerTeamSides.put(joystick.getJoyId(), teamSide);
            teamSide *= -1;
        }
        gameCreationInfo.setControllerTeamSides(controllerTeamSides);

        joystickListener = new JoystickListener();
        inputManager.addRawInputListener(joystickListener);

        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        cam.setFrustumPerspective(45, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000);
        flyCam.setMoveSpeed(100);
        flyCam.setEnabled(false);

        stateManager.attach(new PostFilterAppState());
        stateManager.attach(new StadiumAppState());
        stateManager.attach(new InitialSideSelectionMenuAppState());
        stateManager.attach(new TeamsMenuAppState());
        stateManager.attach(new TrikotMenuAppState());
        stateManager.attach(new GameSettingsMenuAppState());
        stateManager.attach(new PauseIngameMenuAppState());
        stateManager.attach(new PauseSideSelectionMenuAppState());
        stateManager.attach(new FormationMenuAppState());
        stateManager.attach(new GameOverIngameMenuAppState());
        // Has to be attached last, so its joystick sub listener is not disabled by the others
        stateManager.attach(new MainMenuAppState());
    }

    private static TeamInfo getDefaultTeam(String teamName) {
        String[] trikotNames = new String[] { "amaranth", "red", "striped", "thinstripes", "yellow" };
        Player[] fieldPlayers = new Player[11];
        fieldPlayers[0] = new Goalkeeper(teamName + "-Goalie1");
        for (int i = 1; i < fieldPlayers.length; i++) {
            fieldPlayers[i] = new Player(teamName + "-Spieler" + i);
        }
        Player[] reservePlayers = new Player[20];
        reservePlayers[0] = new Goalkeeper(teamName + "-Goalie2");
        for (int i = 1; i < reservePlayers.length; i++) {
            reservePlayers[i] = new Player(teamName + "-Spieler" + (fieldPlayers.length + i));
        }
        return new TeamInfo(teamName, trikotNames, fieldPlayers, reservePlayers, new Formation442());
    }

    private InitialTeamInfo generateInitialTeamInfo(int teamIndex) {
        InitialTeamInfo initialTeamInfo = new InitialTeamInfo();
        initialTeamInfo.setTeam(teamIndex);
        return initialTeamInfo;
    }
}
