package com.carlkuesters.fifachampions.menu;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class TeamSelectionMenuGroup extends MenuGroup {

    public TeamSelectionMenuGroup(Runnable back, Function<Integer, Integer> getTeamSide, BiConsumer<Integer, Integer> setTeamSide, Runnable confirm) {
        super(back);
        this.getTeamSide = getTeamSide;
        this.setTeamSide = setTeamSide;
        this.confirm = confirm;
    }
    private Function<Integer, Integer> getTeamSide;
    private BiConsumer<Integer, Integer> setTeamSide;
    private Runnable confirm;

    @Override
    public void navigateLeft(int joyId) {
        super.navigateLeft(joyId);
        switchTeam(joyId, 1);
    }

    @Override
    public void navigateRight(int joyId) {
        super.navigateRight(joyId);
        switchTeam(joyId, -1);
    }

    private void switchTeam(int joyId, int direction) {
        int oldTeamSide = getTeamSide.apply(joyId);
        int newTeamSide = Math.max(-1, Math.min(oldTeamSide + direction, 1));
        if (newTeamSide != oldTeamSide) {
            setTeamSide.accept(joyId, newTeamSide);
        }
    }

    @Override
    public void confirm() {
        confirm.run();
    }
}
