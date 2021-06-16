package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.game.Controller;
import com.carlkuesters.fifachampions.game.Player;
import com.carlkuesters.fifachampions.game.PlayerObject;
import com.carlkuesters.fifachampions.game.buttons.behaviours.ChargedButtonBehaviour;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import lombok.Getter;

public class ControlledPlayerContainer {

    public ControlledPlayerContainer(boolean leftOrRight) {
        node = new Node();

        optimalStrengthIndicator = new Label("");
        optimalStrengthIndicator.setBackground(new IconComponent("textures/optimal_strength_indicator.png"));
        optimalStrengthIndicator.setLocalTranslation(new Vector3f(0, Y + 9, 2));
        optimalStrengthIndicator.setLocalScale(0.085f);
        node.attachChild(optimalStrengthIndicator);

        container = new Container();
        container.setLocalTranslation(0, Y, 0);
        container.setBackground(null);
        pbrStrength = new ProgressBar();
        pbrStrength.setPreferredSize(new Vector3f(WIDTH, 20, 1));
        container.addChild(pbrStrength);
        lblName = new Label("");
        lblName.setInsets(new Insets3f(10, 0, 0, 0));
        lblName.setFontSize(16);
        lblName.setTextHAlignment(leftOrRight ? HAlignment.Left : HAlignment.Right);
        lblName.setColor(ColorRGBA.White);
        container.addChild(lblName);
        node.attachChild(container);
    }
    public static final int Y = 71;
    public static final int WIDTH = 200;
    @Getter
    private Node node;
    private Container container;
    private Label optimalStrengthIndicator;
    private ProgressBar pbrStrength;
    private float displayedStrength;
    private float remainingDisplayedStrengthDuration;
    private PlayerObject displayedStrengthPlayerObject;
    private Label lblName;

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

            Player player = controller.getPlayerObject().getPlayer();
            lblName.setText(player.getName());

            container.setCullHint(Spatial.CullHint.Inherit);
        } else {
            container.setCullHint(Spatial.CullHint.Always);
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
