package com.carlkuesters.fifachampions.menu;

public class CarouselUtil {

    public static void changeValue(Carousel carousel, int direction) {
        int oldValue = carousel.getCarouselValue();
        int newValue = oldValue + direction;
        int maximumValue = carousel.getCarouselMaximumValue();
        if (newValue > maximumValue) {
            newValue = 0;
        } else if (newValue < 0) {
            newValue = maximumValue;
        }
        carousel.setCarouselValue(newValue);
    }
}
