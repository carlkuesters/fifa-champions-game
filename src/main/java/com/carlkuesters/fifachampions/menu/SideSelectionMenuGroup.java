package com.carlkuesters.fifachampions.menu;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class SideSelectionMenuGroup extends MenuGroup {

    public SideSelectionMenuGroup(Runnable back, Function<Integer, Integer> getTeamSide, BiConsumer<Integer, Integer> setTeamSide, Runnable confirm) {
        super(back);
        this.getTeamSide = getTeamSide;
        this.setTeamSide = setTeamSide;
        this.confirm = confirm;
    }
    private Function<Integer, Integer> getTeamSide;
    private BiConsumer<Integer, Integer> setTeamSide;
    private Runnable confirm;

    @Override
    public void primaryNavigateLeft(int joyId) {
        super.primaryNavigateLeft(joyId);
        switchSide(joyId, 1);
    }

    @Override
    public void primaryNavigateRight(int joyId) {
        super.primaryNavigateRight(joyId);
        switchSide(joyId, -1);
    }

    private void switchSide(int joyId, int direction) {
        int oldTeamSide = getTeamSide.apply(joyId);
        int newTeamSide = Math.max(-1, Math.min(oldTeamSide + direction, 1));
        if (newTeamSide != oldTeamSide) {
            setTeamSide.accept(joyId, newTeamSide);
        }
    }

    @Override
    public void confirm(int joyId) {
        confirm.run();
    }
}
