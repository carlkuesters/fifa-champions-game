package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.PostFilterAppState;
import com.carlkuesters.fifachampions.joystick.JoystickListener;
import com.carlkuesters.fifachampions.menu.*;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import lombok.Getter;

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
    private JoystickListener joystickListener;

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("../assets/", FileLocator.class);

        setDisplayStatView(false);

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
}
