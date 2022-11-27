package com.carlkuesters.fifachampions.menu;

public abstract class CarouselMenuGroup extends MenuGroup implements Carousel {

    @Override
    public void primaryNavigateLeft() {
        super.primaryNavigateLeft();
        CarouselUtil.changeValue(this, -1);
    }

    @Override
    public void primaryNavigateRight() {
        super.primaryNavigateRight();
        CarouselUtil.changeValue(this, 1);
    }
}
