package com.carlkuesters.fifachampions;

import com.carlkuesters.fifachampions.game.Game;
import com.carlkuesters.fifachampions.visuals.TimeMaterialParamControl;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowFilter;
import jme3utilities.sky.SkyControl;
import jme3utilities.sky.StarsOption;

public class StadiumAppState extends BaseDisplayAppState {

    private SkyControl skyControl;
    private Material[] audienceMaterials;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White);
        mainApplication.getRootNode().addLight(ambientLight);

        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection((new Vector3f(-1, -2.5f, -1)).normalizeLocal());
        directionalLight.setColor(ColorRGBA.White);
        mainApplication.getRootNode().addLight(directionalLight);

        PostFilterAppState postFilterAppState = getAppState(PostFilterAppState.class);
        postFilterAppState.addFilter(new SSAOFilter(10, 25, 6, 0.1f));
        DirectionalLightShadowFilter shadowFilter = new DirectionalLightShadowFilter(mainApplication.getAssetManager(), 8192, 4);
        shadowFilter.setLight(directionalLight);
        shadowFilter.setShadowIntensity(0.4f);
        postFilterAppState.addFilter(shadowFilter);

        skyControl = new SkyControl(mainApplication.getAssetManager(), mainApplication.getCamera(), 0.7f, StarsOption.Cube, true);
        skyControl.setCloudiness(0.8f);
        skyControl.getSunAndStars().setHour(12);
        mainApplication.getRootNode().addControl(skyControl);
        skyControl.setEnabled(true);

        Node stadium = (Node) mainApplication.getAssetManager().loadModel("models/stadium/stadium_fixed.j3o");
        stadium.move(12.765f, 0, -10.06f);
        stadium.rotate(0, FastMath.HALF_PI, 0);
        stadium.scale(1.1775f);
        stadium.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        mainApplication.getRootNode().attachChild(stadium);

        // The field marking is a bit above the ground in the model
        Geometry fieldMarking = (Geometry) stadium.getChild("stadium_fixed-geom-10");
        fieldMarking.setLocalTranslation(0, -0.19f, 0);
        fieldMarking.setShadowMode(RenderQueue.ShadowMode.Receive);

        String[] audienceGeometryNames = new String[] { "stadium_fixed-geom-3", "stadium_fixed-geom-4", "stadium_fixed-geom-9" };
        audienceMaterials = new Material[audienceGeometryNames.length];
        for (int i = 0; i < audienceGeometryNames.length; i++) {
            Geometry geometry = (Geometry) stadium.getChild(audienceGeometryNames[i]);
            Material oldMaterial = geometry.getMaterial();
            Material newMaterial = new Material(mainApplication.getAssetManager(), "materials/audience.j3md");
            for (MatParam matParam : oldMaterial.getParams()) {
                newMaterial.setParam(matParam.getName(), matParam.getVarType(), matParam.getValue());
            }
            newMaterial.getAdditionalRenderState().setBlendMode(oldMaterial.getAdditionalRenderState().getBlendMode());
            geometry.setMaterial(newMaterial);
            geometry.addControl(new TimeMaterialParamControl("Time"));
            audienceMaterials[i] = newMaterial;
        }
        setAudienceHyped(false);

        // Some faces (e.g. roof) should be rendered from both sides, for now we simply disable culling for all
        for (Geometry geometry : JMonkeyUtil.getAllGeometryChilds(stadium)) {
            geometry.getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        }

        // But we specifically enable culling for the flags, because they are in the way of the ingame camera (front culling because their normals are inverted)
        Geometry flags = (Geometry) stadium.getChild("stadium_fixed-geom-1");
        flags.getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Front);

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

    public void setAudienceHyped(boolean hyped) {
        float speed = (hyped ? 10 : 5);
        for (Material audienceMaterial : audienceMaterials) {
            audienceMaterial.setFloat("Speed", speed);
        }
    }
}
