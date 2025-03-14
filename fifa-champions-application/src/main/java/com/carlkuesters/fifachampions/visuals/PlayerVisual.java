package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.JMonkeyUtil;
import com.carlkuesters.fifachampions.game.PlayerAnimation;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import lombok.Getter;

public class PlayerVisual {

    public PlayerVisual(AssetManager assetManager, PlayerSkin playerSkin) {
        this.assetManager = assetManager;
        this.playerSkin = playerSkin;
        playerModel = (Node) assetManager.loadModel("models/player/player.j3o");
        float playerScale = 0.4174f;
        playerModel.scale(playerScale);
        float halfPlayerModelHeight = (JMonkeyUtil.getSpatialDimension(playerModel).getY() / 2);
        // Center player model on y axis
        float playerModelOffsetForFeetsOnGround = 0.31f;
        playerModel.move(0, (playerModelOffsetForFeetsOnGround - halfPlayerModelHeight), 0);
        AnimComposer animComposer = playerModel.getControl(AnimComposer.class);
        playerVisualAnimationControl = new PlayerVisualAnimationControl(animComposer);
        playerModel.addControl(playerVisualAnimationControl);
        // Head
        Geometry head = (Geometry) playerModel.getChild("head_0");
        Material materialHead = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialHead.setTexture("DiffuseMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/face_" + playerSkin.getFaceName() + ".png"));
        materialHead.setTexture("NormalMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/face_normal.png"));
        materialHead.setTexture("SpecularMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/face_specular.png"));
        head.setMaterial(materialHead);
        // Body
        Geometry body = (Geometry) playerModel.getChild("body_0");
        materialBody = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialBody.setTexture("DiffuseMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/body_original.png"));
        materialBody.setTexture("NormalMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/body_normal.png"));
        materialBody.setTexture("SpecularMap", MaterialFactory.loadTexture(assetManager,  "models/player/resources/body_specular.png"));
        body.setMaterial(materialBody);
        playerModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        // Add model to visual
        modelNode = new Node();
        modelNode.attachChild(playerModel);
        // Add visual to wrapper
        wrapperNode = new Node();
        wrapperNode.setLocalTranslation(0, halfPlayerModelHeight, 0);
        wrapperNode.attachChild(modelNode);

        // Hair
        if (playerSkin.getHairName() != null) {
            Geometry hairModel = (Geometry) assetManager.loadModel("models/hair/" + playerSkin.getHairName() + ".j3o");
            hairModel.setLocalScale(12.5f / playerScale);
            hairModel.rotate(new Quaternion().fromAngleAxis(1.5f * FastMath.PI, Vector3f.UNIT_Z));
            Material materialHair = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            materialHair.setBoolean("UseMaterialColors", true);
            materialHair.setColor("Diffuse", playerSkin.getHairColor());
            float ambientFactor = 0.15f;
            Vector4f ambient = playerSkin.getHairColor().toVector4f().multLocal(ambientFactor, ambientFactor, ambientFactor, 1);
            materialHair.setVector4("Ambient", ambient);
            hairModel.setMaterial(materialHair);

            Node hairWrapper = new Node();
            hairWrapper.attachChild(hairModel);
            hairWrapper.setLocalTranslation(-2.3f / playerScale, 0.5f / playerScale, 0);
            hairWrapper.rotate(new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_X));

            SkinningControl skinningControl = playerModel.getControl(SkinningControl.class);
            skinningControl.getAttachmentsNode("Bip001 Head").attachChild(hairWrapper);
        }
    }
    private AssetManager assetManager;
    private PlayerSkin playerSkin;
    @Getter
    private Node playerModel;
    private Material materialBody;
    @Getter
    private Node modelNode;
    @Getter
    private Node wrapperNode;
    private PlayerVisualAnimationControl playerVisualAnimationControl;

    public void setTrikot(String trikotName) {
        materialBody.setTexture("DiffuseMap", MaterialFactory.loadTexture(assetManager, "models/player/resources/generated/body_" + playerSkin.getFaceName() + "_" + trikotName + ".png"));
    }

    public void setAnimation(PlayerAnimation playerAnimation) {
        playerVisualAnimationControl.set(playerAnimation);
    }

    public void playAnimation(PlayerAnimation playerAnimation) {
        playerVisualAnimationControl.play(playerAnimation);
    }
}
