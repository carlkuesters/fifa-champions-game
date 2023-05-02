package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.controllers.ControllerSettings;
import com.carlkuesters.fifachampions.joystick.JoystickListener;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.Joystick;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ControllerAppState extends BaseDisplayAppState {

    public static final int SETTINGS_COUNT = 9;

    @Getter
    private JoystickListener joystickListener;
    @Getter
    private ControllerSettings[] settings;
    @Getter
    private Map<Integer, Controller> controllers;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        joystickListener = new JoystickListener();
        mainApplication.getInputManager().addRawInputListener(joystickListener);

        settings = new ControllerSettings[SETTINGS_COUNT];
        for (int i = 0; i < settings.length; i++) {
            settings[i] = new ControllerSettings();
        }

        controllers = new HashMap<>();
        int teamSide = 1;
        for (Joystick joystick : mainApplication.getInputManager().getJoysticks()) {
            Controller controller = new Controller(joystick, settings[0]);
            controller.setTeamSide(teamSide);
            controllers.put(joystick.getJoyId(), controller);
        }
    }

    public String getSettingsName(Controller controller) {
        return getSettingsName(getSettingsIndex(controller));
    }

    public String getSettingsName(int settingsIndex) {
        return ((settingsIndex == 0) ? "Klassisch" : "Alternativ " + settingsIndex);
    }

    public int getSettingsIndex(Controller controller) {
        for (int i = 0; i < settings.length; i++) {
            if (controller.getSettings() == settings[i]) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }
}
