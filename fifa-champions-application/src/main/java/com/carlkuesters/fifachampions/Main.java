package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.cinematics.CinematicAppState;
import com.carlkuesters.fifachampions.game.*;
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

    public Main() {
        settings = new AppSettings(true);
        settings.setWidth(1600);
        settings.setHeight(900);
        settings.setVSync(true);
        settings.setSamples(8);
        settings.setUseJoysticks(true);
    }
    @Getter
    private GameCreationInfo gameCreationInfo;
    @Getter
    private JoystickListener joystickListener;
    @Getter
    private boolean freeCam = false;

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("../assets/", FileLocator.class);

        setPauseOnLostFocus(false);
        setDisplayStatView(false);

        gameCreationInfo = new GameCreationInfo();
        gameCreationInfo.setTeams(new InitialTeamInfo[] {
            generateInitialTeamInfo(),
            generateInitialTeamInfo()
        });
        HashMap<Integer, Integer> controllerTeamSides = new HashMap<>();
        int teamSide = -1;
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
        flyCam.setDragToRotate(true);
        flyCam.setEnabled(freeCam);

        stateManager.attach(new PostFilterAppState());
        stateManager.attach(new CinematicAppState());
        stateManager.attach(new StadiumAppState());
        stateManager.attach(new MainSettingsMenuAppState());
        stateManager.attach(new InitialSideSelectionMenuAppState());
        stateManager.attach(new TeamsMenuAppState());
        stateManager.attach(new TrikotMenuAppState());
        stateManager.attach(new GameSettingsMenuAppState());
        stateManager.attach(new InitialFormationMenuAppState());
        stateManager.attach(new PauseIngameMenuAppState());
        stateManager.attach(new PauseSideSelectionMenuAppState());
        stateManager.attach(new PauseFormationMenuAppState());
        stateManager.attach(new GameOverIngameMenuAppState());
        // Has to be attached last, so its joystick sub listener is not disabled by the others
        stateManager.attach(new MainMenuAppState());
    }

    private InitialTeamInfo generateInitialTeamInfo() {
        InitialTeamInfo initialTeamInfo = new InitialTeamInfo();
        initialTeamInfo.setTeam(0);
        return initialTeamInfo;
    }
}
