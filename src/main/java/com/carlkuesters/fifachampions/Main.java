package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.joystick.JoystickListener;
import com.carlkuesters.fifachampions.menu.*;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.input.Joystick;
import com.jme3.math.Vector2f;
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
        gameCreationInfo.setTeams(new TeamInfo[] {
            generateTeam("T1"),
            generateTeam("T2")
        });
        HashMap<Integer, Integer> controllerTeams = new HashMap<>();
        int teamIndex = 0;
        for (Joystick joystick : inputManager.getJoysticks()) {
            controllerTeams.put(joystick.getJoyId(), teamIndex);
            teamIndex++;
        }
        gameCreationInfo.setControllerTeams(controllerTeams);

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
        stateManager.attach(new TeamsMenuAppState());
        stateManager.attach(new ShirtMenuAppState());
        stateManager.attach(new GameSettingsMenuAppState());
        stateManager.attach(new PauseIngameMenuAppState());
        stateManager.attach(new TeamSelectionMenuAppState());
        stateManager.attach(new FormationMenuAppState());
        stateManager.attach(new GameOverIngameMenuAppState());
        // Has to be attached last, so its joystick sub listener is not disabled by the others
        stateManager.attach(new MainMenuAppState());
    }

    private TeamInfo generateTeam(String teamName) {
        Player[] fieldPlayers = new Player[11];
        fieldPlayers[0] = new Goalkeeper(teamName + "-Goalie1");
        for (int i = 1; i < fieldPlayers.length; i++) {
            fieldPlayers[i] = new Player(teamName + "-Spieler" + i);
        }
        Player[] reservePlayers = new Player[20];
        reservePlayers[0] = new Goalkeeper(teamName + "-Goalie2");
        for (int i = 1; i < reservePlayers.length; i++) {
            reservePlayers[i] = new Player(teamName + "-Spieler" + (fieldPlayers.length + 1));
        }
        return new TeamInfo(teamName, fieldPlayers, reservePlayers, new Formation(new Vector2f[]{
            new Vector2f(-1, 0),

            new Vector2f(-0.7f, -0.75f),
            new Vector2f(-0.7f, -0.25f),
            new Vector2f(-0.7f, 0.25f),
            new Vector2f(-0.7f, 0.75f),

            new Vector2f(0, -0.75f),
            new Vector2f(0, -0.25f),
            new Vector2f(0, 0.25f),
            new Vector2f(0, 0.75f),

            new Vector2f(0.7f, -0.5f),
            new Vector2f(0.7f, 0.5f)
        }));
    }
}
