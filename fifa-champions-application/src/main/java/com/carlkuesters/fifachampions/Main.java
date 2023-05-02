package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.cinematics.CinematicAppState;
import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.game.controllers.ControllerSettings;
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
import java.util.Map;

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
    private JoystickListener joystickListener;
    @Getter
    private ControllerSettings[] controllerSettings;
    @Getter
    private Map<Integer, Controller> controllers;
    @Getter
    private GameCreationInfo gameCreationInfo;

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("../assets/", FileLocator.class);

        setPauseOnLostFocus(false);
        setDisplayStatView(false);

        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        joystickListener = new JoystickListener();
        inputManager.addRawInputListener(joystickListener);

        controllerSettings = new ControllerSettings[9];
        for (int i = 0; i < controllerSettings.length; i++) {
            controllerSettings[i] = new ControllerSettings();
        }

        controllers = new HashMap<>();
        int teamSide = 1;
        for (Joystick joystick : inputManager.getJoysticks()) {
            Controller controller = new Controller(joystick, controllerSettings[0]);
            controller.setTeamSide(teamSide);
            controllers.put(joystick.getJoyId(), controller);
        }

        gameCreationInfo = new GameCreationInfo();
        gameCreationInfo.setTeams(new InitialTeamInfo[] {
            generateInitialTeamInfo(),
            generateInitialTeamInfo()
        });

        stateManager.attach(new CameraAppState());
        stateManager.attach(new PostFilterAppState());
        stateManager.attach(new CinematicAppState());
        stateManager.attach(new StadiumAppState());
        stateManager.attach(new MainSettingsMenuAppState());
        stateManager.attach(new ControllerSettingsMenuAppState());
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
