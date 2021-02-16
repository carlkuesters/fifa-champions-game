package com.carlkuesters.fifachampions.joystick;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.*;
import lombok.Setter;
public class JoystickListener implements RawInputListener {

    @Setter
    private MenuJoystickSubListener menuSubListener;
    @Setter
    private GameJoystickSubListener gameSubListener;

    @Override
    public void onJoyAxisEvent(JoyAxisEvent evt) {
        if (menuSubListener != null) {
            menuSubListener.onJoyAxisEvent(evt);
        } else if (gameSubListener != null) {
            gameSubListener.onJoyAxisEvent(evt);
        }
    }

    public void onJoyButtonEvent(JoyButtonEvent evt) {
        if (menuSubListener != null) {
            menuSubListener.onJoyButtonEvent(evt);
        } else if (gameSubListener != null) {
            gameSubListener.onJoyButtonEvent(evt);
        }
    }

    public void beginInput() {}
    public void endInput() {}
    public void onMouseMotionEvent(MouseMotionEvent evt) {}
    public void onMouseButtonEvent(MouseButtonEvent evt) {}
    public void onKeyEvent(KeyInputEvent evt) {}
    public void onTouchEvent(TouchEvent evt) {}
}