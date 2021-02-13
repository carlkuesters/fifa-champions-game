package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.PostFilterAppState;
import com.carlkuesters.fifachampions.menu.*;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

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

    @Override
    public void simpleInitApp() {
        assetManager.registerLocator("../assets/", FileLocator.class);

        setDisplayStatView(false);

        GuiGlobals.initialize(this);
        BaseStyles.loadGlassStyle();
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        cam.setFrustumPerspective(45, (float) cam.getWidth() / cam.getHeight(), 0.01f, 1000);
        flyCam.setMoveSpeed(100);
        flyCam.setEnabled(false);

        stateManager.attach(new PostFilterAppState());
        stateManager.attach(new StadiumAppState());
        stateManager.attach(new MainMenuAppState());
        stateManager.attach(new TeamsMenuAppState());
        stateManager.attach(new ShirtMenuAppState());
        stateManager.attach(new GameSettingsMenuAppState());
        stateManager.attach(new IngameMenuAppState());
        stateManager.attach(new TeamSelectionMenuAppState());
    }
}
