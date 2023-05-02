package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.ControllerAppState;
import com.carlkuesters.fifachampions.game.controllers.ControllerButtonPS5;
import com.carlkuesters.fifachampions.game.controllers.ControllerSettings;
import com.simsilica.lemur.Button;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ControllerSettingsMenuAppState extends SettingsMenuAppState {

    public ControllerSettingsMenuAppState() {
        super("Steuerung");
        mode = MenuMode.FREE_CHILD;
        updateTasks = new ArrayList<>();
    }
    private LabelCarousel<Integer> carouselSettingIndex;
    private ArrayList<Runnable> updateTasks;

    @Override
    protected void initMenu() {
        super.initMenu();
        ControllerAppState controllerAppState = getAppState(ControllerAppState.class);
        Integer[] alternativeSettingIndices = new Integer[ControllerAppState.SETTINGS_COUNT - 1];
        for (int i = 0; i < alternativeSettingIndices.length; i++) {
            alternativeSettingIndices[i] = i + 1;
        }
        carouselSettingIndex = addCarouselButton(
            alternativeSettingIndices,
            controllerAppState::getSettingsName,
            settingIndex -> update()
        );
        addRecordButton("Direktpass / Pressen", ControllerSettings::getButtonIndex_PassDirectOrPressure, ControllerSettings::setButtonIndex_PassDirectOrPressure);
        addRecordButton("Steilpass / Torwart herausholen", ControllerSettings::getButtonIndex_PassInRunOrGoalkeeperPressure, ControllerSettings::setButtonIndex_PassInRunOrGoalkeeperPressure);
        addRecordButton("Flanke / Grätsche", ControllerSettings::getButtonIndex_FlankOrStraddle, ControllerSettings::setButtonIndex_FlankOrStraddle);
        addRecordButton("Schuss", ControllerSettings::getButtonIndex_Shoot, ControllerSettings::setButtonIndex_Shoot);
        addRecordButton("Sprint", ControllerSettings::getButtonIndex_Sprint, ControllerSettings::setButtonIndex_Sprint);
        addRecordButton("Spieler wechseln", ControllerSettings::getButtonIndex_SwitchPlayer, ControllerSettings::setButtonIndex_SwitchPlayer);
        update();
    }

    private void addRecordButton(String text, Function<ControllerSettings, Integer> getButtonIndex, BiConsumer<ControllerSettings, Integer> setButtonIndex) {
        Button button = addButton("", () -> {
            menuJoystickSubListener.setButtonRecorder(buttonIndex -> {
                setButtonIndex.accept(getSelectedControllerSettings(), buttonIndex);
                update();
            });
            update();
        });
        updateTasks.add(() -> {
            String buttonText = text + ": ";
            if ((menuJoystickSubListener.getButtonRecorder() != null) && (menuGroup.getActiveElement().getPanel() == button)) {
                buttonText += "<Taste drücken>";
            } else {
                int buttonIndex = getButtonIndex.apply(getSelectedControllerSettings());
                ControllerButtonPS5 buttonPS5 = ControllerButtonPS5.byButtonIndex(buttonIndex);
                buttonText += ((buttonPS5 != null) ? buttonPS5.getName() : "<" + buttonIndex + ">");
            }
            button.setText(buttonText);
        });
    }

    private ControllerSettings getSelectedControllerSettings() {
        return getAppState(ControllerAppState.class).getSettings()[carouselSettingIndex.getValue()];
    }

    private void update() {
        for (Runnable updateTask : updateTasks) {
            updateTask.run();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            carouselSettingIndex.setValue(1);
            update();
        }
    }
}
