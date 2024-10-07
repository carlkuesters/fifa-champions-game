package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.cinematics.CinematicAppState;
import com.carlkuesters.fifachampions.game.*;
import com.carlkuesters.fifachampions.menu.*;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import lombok.Getter;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        JMonkeyUtil.disableLogger();

        Main app = new Main();
        app.setShowSettings(false);
        app.start();
    }

    public Main() {
        settings = new AppSettings(true);
        settings.setWidth(1920);
        settings.setHeight(1080);
        settings.setVSync(true);
        settings.setSamples(8);
        settings.setGammaCorrection(false);
        settings.setUseJoysticks(true);
    }
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

        gameCreationInfo = new GameCreationInfo();
        gameCreationInfo.setTeams(new InitialTeamInfo[] {
            generateInitialTeamInfo(),
            generateInitialTeamInfo()
        });

        stateManager.attach(new ControllerAppState());
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
        stateManager.attach(new ReplayMenuAppState());
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
