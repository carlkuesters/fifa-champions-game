package com.carlkuesters.fifachampions.visuals;

import com.carlkuesters.fifachampions.JMonkeyUtil;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import lombok.Getter;

public class BallVisual {

    public BallVisual(AssetManager assetManager) {
        ballNode = new Node();
        ballModel = assetManager.loadModel("models/ball/ball.j3o");
        float targetCircumference = 0.69f;
        float targetRadius = targetCircumference / FastMath.TWO_PI;
        float targetDiameter = 2 * targetRadius;
        float currentDiameter = JMonkeyUtil.getSpatialDimension(ballModel).getX();
        ballModel.setLocalScale(targetDiameter / currentDiameter);
        ballModel.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        Material materialBall = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        materialBall.setTexture("DiffuseMap", MaterialFactory.loadTexture(assetManager, "models/ball/resources/ball_D.png"));
        materialBall.setTexture("NormalMap", MaterialFactory.loadTexture(assetManager, "models/ball/resources/ball_N.png"));
        materialBall.setTexture("SpecularMap", MaterialFactory.loadTexture(assetManager, "models/ball/resources/ball_SP.png"));
        ballNode.setLocalTranslation(0, 0.432f, 0);
        ballNode.attachChild(ballModel);
        Geometry ballGeometry = (Geometry) ballNode.getChild("ball");
        ballGeometry.setMaterial(materialBall);
    }
    @Getter
    private Node ballNode;
    @Getter
    private Spatial ballModel;
}
