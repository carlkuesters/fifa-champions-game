package com.carlkuesters.fifachampions.menu;

import com.carlkuesters.fifachampions.ArrayUtil;
import com.simsilica.lemur.Label;
import lombok.Getter;

import java.util.function.Function;

public class LabelCarousel<T> implements Carousel {

    public LabelCarousel(Label label, T[] values, Function<T, String> getText) {
        this.label = label;
        this.values = values;
        this.getText = getText;
    }
    private Label label;
    @Getter
    private T[] values;
    private Function<T, String> getText;
    private int currentValueIndex;

    @Override
    public int getCarouselValue() {
        return currentValueIndex;
    }

    @Override
    public void setCarouselValue(int value) {
        setValueByIndex(value);
    }

    @Override
    public int getCarouselMaximumValue() {
        return (values.length - 1);
    }

    public T getValue() {
        return values[currentValueIndex];
    }

    public void setValue(T value) {
        setValueByIndex(ArrayUtil.getIndex(values, value));
    }

    private void setValueByIndex(int index) {
        currentValueIndex = index;
        label.setText(getText.apply(getValue()));
    }
}
