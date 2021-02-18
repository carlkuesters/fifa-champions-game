package com.carlkuesters.fifachampions.menu;

public abstract class CarouselMenuGroup extends MenuGroup implements Carousel {

    public CarouselMenuGroup(Runnable back) {
        super(back);
    }

    @Override
    public void primaryNavigateLeft(int joyId) {
        super.primaryNavigateLeft(joyId);
        CarouselUtil.changeValue(this, joyId, -1);
    }

    @Override
    public void primaryNavigateRight(int joyId) {
        super.primaryNavigateRight(joyId);
        CarouselUtil.changeValue(this, joyId, 1);
    }
}
