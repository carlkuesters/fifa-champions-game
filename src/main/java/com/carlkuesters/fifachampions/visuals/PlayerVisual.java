package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.JMonkeyUtil;
import com.carlkuesters.fifachampions.game.PlayerAnimation;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import lombok.Getter;

public class PlayerVisual {

    public static final PlayerAnimation RUN_ANIMATION_FAST = new PlayerAnimation("run_fast", 0.7f);
    public static final PlayerAnimation RUN_ANIMATION_MEDIUM = new PlayerAnimation("run_medium", 1.17f);
    public static final PlayerAnimation RUN_ANIMATION_SLOW = new PlayerAnimation("run_slow", 1.59f);
    public static final PlayerAnimation IDLE_ANIMATION = new PlayerAnimation("idle", 4);

    public PlayerVisual(AssetManager assetManager) {
        this.assetManager = assetManager;
        Node playerModel = (Node) assetManager.loadModel("models/player/player.j3o");
        playerModel.scale(0.0106f);
        float halfPlayerModelHeight = (JMonkeyUtil.getSpatialDimension(playerModel).getY() / 2);
        // Center player model on y axis
        float playerModelOffsetForFeetsOnGround = 1.302f;
        playerModel.move(0, (playerModelOffsetForFeetsOnGround - halfPlayerModelHeight), 0);
        playerModel.rotate(0, -1 * FastMath.HALF_PI, 0);
        AnimControl animControl = playerModel.getControl(AnimControl.class);
        animChannel = animControl.createChannel();
        // Head
        Material materialHead = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialHead.setTexture("DiffuseMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/WSP_head_D.png"));
        materialHead.setTexture("NormalMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/WSP_head_N.png"));
        materialHead.setTexture("SpecularMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/WSP_head_SP.png"));
        Geometry head = (Geometry) playerModel.getChild("head");
        head.setMaterial(materialHead);
        // Body
        Geometry body = (Geometry) playerModel.getChild("body");
        materialBody = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialBody.setTexture("DiffuseMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/WSP_head_D.png"));
        materialBody.setTexture("NormalMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/WSP_body_N.png"));
        materialBody.setTexture("SpecularMap", MaterialFactory.loadTexture(assetManager,  "models/player/resources/WSP_body_SP.png"));
        body.setMaterial(materialBody);
        playerModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        // Add model to visual
        modelNode = new Node();
        modelNode.attachChild(playerModel);
        // Add visual to wrapper
        wrapperNode = new Node();
        wrapperNode.setLocalTranslation(0, halfPlayerModelHeight, 0);
        wrapperNode.attachChild(modelNode);
    }
    private AssetManager assetManager;
    private Material materialBody;
    @Getter
    private Node modelNode;
    @Getter
    private Node wrapperNode;
    private AnimChannel animChannel;

    public void setTrikot(String trikotName) {
        materialBody.setTexture("DiffuseMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/WSP_body_" + trikotName + "_D.png"));
    }

    public void playAnimation(PlayerAnimation playerAnimation) {
        if (!playerAnimation.getName().equals(animChannel.getAnimationName())) {
            animChannel.setAnim(playerAnimation.getName());
        }
        animChannel.setSpeed(animChannel.getAnimMaxTime() / playerAnimation.getLoopDuration());
        animChannel.setLoopMode(playerAnimation.getLoopMode());
    }
}
