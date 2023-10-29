package com.carlkuesters.fifachampions.menu;

import lombok.AllArgsConstructor;

import java.util.function.Consumer;

@AllArgsConstructor
public class ReplayMenuGroup extends MenuGroup {

    private static final float TIME_INTERVAL_SMALL = 1;
    private static final float TIME_INTERVAL_BIG = 10;

    private Consumer<Float> moveTime;
    private Runnable togglePlaying;

    @Override
    public void primaryNavigateLeft() {
        super.primaryNavigateLeft();
        moveTime.accept(-1 * TIME_INTERVAL_SMALL);
    }

    @Override
    public void primaryNavigateRight() {
        super.primaryNavigateRight();
        moveTime.accept(TIME_INTERVAL_SMALL);
    }

    @Override
    public void secondaryNavigateLeft() {
        super.secondaryNavigateLeft();
        moveTime.accept(-1 * TIME_INTERVAL_BIG);
    }

    @Override
    public void secondaryNavigateRight() {
        super.secondaryNavigateRight();
        moveTime.accept(TIME_INTERVAL_BIG);
    }

    @Override
    public void confirm() {
        togglePlaying.run();
    }
}
