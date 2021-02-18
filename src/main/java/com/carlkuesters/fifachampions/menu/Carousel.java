package com.carlkuesters.fifachampions.menu;

public interface Carousel {

    int getCarouselValue(int joyId);

    void setCarouselValue(int joyId, int value);

    int getCarouselMaximumValue(int joyId);
}
