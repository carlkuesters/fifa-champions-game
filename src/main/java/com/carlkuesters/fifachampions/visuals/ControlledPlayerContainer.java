package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ChargedButtonBehaviour;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.IconComponent;
import lombok.Getter;

public class ControlledPlayerContainer {

    public ControlledPlayerContainer() {
        node = new Node();

        optimalStrengthIndicator = new Label("");
        optimalStrengthIndicator.setBackground(new IconComponent("textures/optimal_strength_indicator.png"));
        optimalStrengthIndicator.setLocalTranslation(new Vector3f(0, 70, 2));
        optimalStrengthIndicator.setLocalScale(0.085f);
        node.attachChild(optimalStrengthIndicator);

        playerContainer = new Container();
        playerContainer.setLocalTranslation(0, 61, 0);
        playerContainer.setPreferredSize(new Vector3f(WIDTH, 20, 1));
        pbrStrength = new ProgressBar();
        playerContainer.addChild(pbrStrength);
        node.attachChild(playerContainer);
    }
    public static final int WIDTH = 200;
    @Getter
    private Node node;
    private Container playerContainer;
    private Label optimalStrengthIndicator;
    private ProgressBar pbrStrength;
    private float displayedStrength;
    private float remainingDisplayedStrengthDuration;
    private PlayerObject displayedStrengthPlayerObject;

    public void update(Controller controller, Float optimalShootStrength, float tpf) {
        if (controller != null) {
            ChargedButtonBehaviour chargingButtonBehaviour = controller.getChargingButtonBehaviour();
            if (chargingButtonBehaviour != null) {
                displayedStrength = chargingButtonBehaviour.getCurrentChargeStrength();
                remainingDisplayedStrengthDuration = 2;
                displayedStrengthPlayerObject = controller.getPlayerObject();
            } else if (remainingDisplayedStrengthDuration > 0) {
                remainingDisplayedStrengthDuration -= tpf;
                if ((remainingDisplayedStrengthDuration <= 0) || (controller.getPlayerObject() != displayedStrengthPlayerObject)) {
                    displayedStrength = 0;
                    remainingDisplayedStrengthDuration = 0;
                    displayedStrengthPlayerObject = null;
                }
            }
            pbrStrength.setProgressPercent(displayedStrength);
            playerContainer.setCullHint(Spatial.CullHint.Inherit);
        } else {
            playerContainer.setCullHint(Spatial.CullHint.Always);
        }

        if (optimalShootStrength != null) {
            float optimalStrengthIndicatorX = -9 + (optimalShootStrength * (WIDTH - 5));
            optimalStrengthIndicator.setLocalTranslation(optimalStrengthIndicator.getLocalTranslation().setX(optimalStrengthIndicatorX));
            optimalStrengthIndicator.setCullHint(Spatial.CullHint.Inherit);
        } else {
            optimalStrengthIndicator.setCullHint(Spatial.CullHint.Always);
        }
    }
}
