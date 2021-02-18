package com.carlkuesters.fifachampions.menu;

public abstract class CarouselMenuGroup extends MenuGroup {

    public CarouselMenuGroup(Runnable back) {
        super(back);
    }

    @Override
    public void navigateLeft(int joyId) {
        super.navigateLeft(joyId);
        changeValue(joyId, 1);
    }

    @Override
    public void navigateRight(int joyId) {
        super.navigateRight(joyId);
        changeValue(joyId, -1);
    }

    private void changeValue(int joyId, int direction) {
        int oldValue = getValue(joyId);
        int newValue = oldValue + direction;
        int maximumValue = getMaximumValue(joyId);
        if (newValue > maximumValue) {
            newValue = 0;
        } else if (newValue < 0) {
            newValue = maximumValue;
        }
        setValue(joyId, newValue);
    }

    protected abstract int getValue(int joyId);

    protected abstract void setValue(int joyId, int value);

    protected abstract int getMaximumValue(int joyId);
}
