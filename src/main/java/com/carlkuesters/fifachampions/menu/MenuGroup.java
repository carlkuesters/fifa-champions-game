package com.carlkuesters.fifachampions.menu;

public class MenuGroup {

    public MenuGroup(Runnable back) {
        this.back = back;
    }
    private Runnable back;

    public void primaryNavigateLeft(int joyId) {

    }

    public void primaryNavigateRight(int joyId) {

    }

    public void primaryNavigateUp(int joyId) {

    }

    public void primaryNavigateDown(int joyId) {

    }

    public void secondaryNavigateLeft(int joyId) {

    }

    public void secondaryNavigateRight(int joyId) {

    }

    public void confirm(int joyId) {
        back();
    }

    public void back() {
        back.run();
    }
}
