package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.ControllerAppState;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor
public class SideSelectionMenuGroup extends MenuGroup {

    private Supplier<Integer> getTeamSide;
    private Consumer<Integer> setTeamSide;
    private Supplier<Integer> getControllerSettingsIndex;
    private Consumer<Integer> setControllerSettingsIndex;
    private Runnable confirm;

    @Override
    public void primaryNavigateLeft() {
        super.primaryNavigateLeft();
        switchSide(1);
    }

    @Override
    public void primaryNavigateRight() {
        super.primaryNavigateRight();
        switchSide(-1);
    }

    private void switchSide(int direction) {
        int oldTeamSide = getTeamSide.get();
        int newTeamSide = Math.max(-1, Math.min(oldTeamSide + direction, 1));
        if (newTeamSide != oldTeamSide) {
            setTeamSide.accept(newTeamSide);
        }
    }

    @Override
    public void secondaryNavigateLeft() {
        super.secondaryNavigateLeft();
        switchControllerSettings(-1);
    }

    @Override
    public void secondaryNavigateRight() {
        super.secondaryNavigateRight();
        switchControllerSettings(1);
    }

    private void switchControllerSettings(int direction) {
        int oldControllerSettingsIndex = getControllerSettingsIndex.get();
        int newControllerSettingsIndex = ((oldControllerSettingsIndex + direction + ControllerAppState.SETTINGS_COUNT) % ControllerAppState.SETTINGS_COUNT);
        setControllerSettingsIndex.accept(newControllerSettingsIndex);
    }

    @Override
    public void confirm() {
        confirm.run();
    }
}
