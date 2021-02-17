package com.carlkuesters.fifachampions.menu;

public class MenuGroup {

    public MenuGroup(Runnable back) {
        this.back = back;
    }
    private Runnable back;

    public void navigateLeft(int joyId) {

    }

    public void navigateRight(int joyId) {

    }

    public void navigateUp(int joyId) {

    }

    public void navigateDown(int joyId) {

    }

    public void confirm() {
        back();
    }

    public void back() {
        back.run();
    }
}
