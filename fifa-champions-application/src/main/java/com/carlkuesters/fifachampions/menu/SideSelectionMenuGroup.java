package com.carlkuesters.fifachampions.menu;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SideSelectionMenuGroup extends MenuGroup {

    public SideSelectionMenuGroup(Supplier<Integer> getTeamSide, Consumer<Integer> setTeamSide, Runnable confirm) {
        this.getTeamSide = getTeamSide;
        this.setTeamSide = setTeamSide;
        this.confirm = confirm;
    }
    private Supplier<Integer> getTeamSide;
    private Consumer<Integer> setTeamSide;
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
    public void confirm() {
        confirm.run();
    }
}
