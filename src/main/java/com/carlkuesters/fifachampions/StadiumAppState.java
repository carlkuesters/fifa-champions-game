package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.BaseDisplayAppState;
import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.game.PostFilterAppState;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowFilter;

public class StadiumAppState extends BaseDisplayAppState {

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        mainApplication.getRootNode().addLight(ambient);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-1, -1, -1)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        mainApplication.getRootNode().addLight(sun);

        DirectionalLightShadowFilter shadowFilter = new DirectionalLightShadowFilter(mainApplication.getAssetManager(), 2048, 3);
        shadowFilter.setLight(sun);
        shadowFilter.setShadowIntensity(0.4f);
        // TODO: Why do I have to cast here?
        ((PostFilterAppState) getAppState(PostFilterAppState.class)).addFilter(shadowFilter);

        Spatial stadium = mainApplication.getAssetManager().loadModel("models/stadium/stadium.j3o");
        stadium.move(12.765f, 0, -10.06f);
        stadium.rotate(0, FastMath.HALF_PI, 0);
        stadium.scale(1.1775f);
        stadium.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        mainApplication.getRootNode().attachChild(stadium);

        if (false) {
            Geometry fieldTestBounds = new Geometry("", new Box(Game.FIELD_HALF_WIDTH, Game.GOAL_HEIGHT / 2, Game.FIELD_HALF_HEIGHT));
            fieldTestBounds.setLocalTranslation(0, (Game.GOAL_HEIGHT / 2), 0);
            Material fieldMat = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
            fieldMat.setBoolean("UseMaterialColors", true);
            fieldMat.setColor("Ambient", ColorRGBA.Green);
            fieldMat.setColor("Diffuse", ColorRGBA.Green);
            fieldMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
            fieldTestBounds.setMaterial(fieldMat);
            mainApplication.getRootNode().attachChild(fieldTestBounds);

            for (int i = 0; i < 2; i++) {
                Geometry goalTestBounds = new Geometry("", new Box(Game.GOAL_WIDTH / 2, Game.GOAL_HEIGHT / 2, (Game.GOAL_Z_TOP - Game.GOAL_Z_BOTTOM) / 2));
                int side = ((i == 0) ? 1 : -1);
                float x = (side * (Game.FIELD_HALF_WIDTH + (Game.GOAL_WIDTH / 2)));
                float z = (Game.GOAL_Z_TOP - ((Game.GOAL_Z_TOP - Game.GOAL_Z_BOTTOM) / 2));
                goalTestBounds.setLocalTranslation(x, (Game.GOAL_HEIGHT / 2), z);
                Material goalMat = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
                goalMat.setBoolean("UseMaterialColors", true);
                goalMat.setColor("Ambient", ColorRGBA.Blue);
                goalMat.setColor("Diffuse", ColorRGBA.Blue);
                goalMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
                goalTestBounds.setMaterial(goalMat);
                mainApplication.getRootNode().attachChild(goalTestBounds);
            }
        }
    }
}
