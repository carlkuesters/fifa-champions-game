package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.menu.MainMenuAppState;
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

        stateManager.attach(new MainMenuAppState());
    }
}
