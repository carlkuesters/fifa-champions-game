package com.carlkuesters.fifachampions.game.buttons.behaviours;

import com.carlkuesters.fifachampions.game.*;

public abstract class ChargedButtonBehaviour extends ControllerButtonBehaviour implements GameLoopListener {

    protected float maxChargedDuration = 1;
    private float chargedDuration;
    private boolean isCharging;
    private float lastChargeTime;
    private PlayerObject lastChargePlayerObject;
    private float lastChargeStrength;

    @Override
    public void onPressed(boolean isPressed) {
        this.isCharging = isPressed;
        if (!isPressed) {
            isTriggered = true;
        }
    }

    @Override
    public void update(float tpf) {
        if (isCharging) {
            chargedDuration += tpf;
            if (chargedDuration >= maxChargedDuration) {
                isTriggered = true;
                isCharging = false;
            }
        }
    }

    public boolean triggerCurrentOrRecentCharge() {
        if (isCharging) {
            isCharging = false;
            trigger();
            return true;
        } else if (wasRecentlyCharged()) {
            checkAndExecuteTrigger(lastChargeStrength);
            return true;
        }
        return false;
    }

    private boolean wasRecentlyCharged() {
        return (lastChargeTime != 0) && ((getCurrentTime() - lastChargeTime) < 0.5f) && (controller.getPlayerObject() == lastChargePlayerObject);
    }

    @Override
    public void trigger() {
        super.trigger();
        float strength = getCurrentChargeStrength();
        checkAndExecuteTrigger(strength);
    }

    private void checkAndExecuteTrigger(float strength) {
        chargedDuration = 0;
        if (isTriggerAllowed()) {
            onTrigger(strength);
        }
        lastChargeTime = getCurrentTime();
        lastChargePlayerObject = controller.getPlayerObject();
        lastChargeStrength = strength;
    }

    protected boolean isTriggerAllowed() {
        return true;
    }

    protected abstract void onTrigger(float strength);

    private float getCurrentTime() {
        return controller.getPlayerObject().getGame().getLogicTime();
    }

    public boolean isCharging() {
        return isCharging;
    }

    public float getCurrentChargeStrength() {
        return ((maxChargedDuration > 0) ? (chargedDuration / maxChargedDuration) : 1);
    }
}
