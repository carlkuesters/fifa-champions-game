package com.carlkuesters.fifachampions.menu;

public class CarouselUtil {

    public static void changeValue(Carousel carousel, int joyId, int direction) {
        int oldValue = carousel.getCarouselValue(joyId);
        int newValue = oldValue + direction;
        int maximumValue = carousel.getCarouselMaximumValue(joyId);
        if (newValue > maximumValue) {
            newValue = 0;
        } else if (newValue < 0) {
            newValue = maximumValue;
        }
        carousel.setCarouselValue(joyId, newValue);
    }
}
