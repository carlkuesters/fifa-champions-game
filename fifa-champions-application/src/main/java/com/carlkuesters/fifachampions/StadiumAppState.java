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
import com.jme3.math.Vector2f;
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

        Node stadium = (Node) mainApplication.getAssetManager().loadModel("models/stadium/stadium.j3o");
        stadium.move(12.765f, 0, -10.06f);
        stadium.rotate(0, FastMath.HALF_PI, 0);
        stadium.scale(1.1775f);
        stadium.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        // Rotate it so that the player entrance gate is lighted
        Node wrapper = new Node();
        wrapper.attachChild(stadium);
        wrapper.rotate(0, FastMath.PI, 0);
        mainApplication.getRootNode().attachChild(wrapper);

        Geometry grass = (Geometry) stadium.getChild("stadium-geom-11");
        // The bright and dark grass stripes are not well aligned with the field markings otherwise
        JMonkeyUtil.shiftAndScaleTextureCoordinates(grass.getMesh(), new Vector2f(0.02f, 0), new Vector2f(1.31f, 1.31f));
        // The grass otherwise has shadow artifacts when casting shadows onto the field marking
        grass.setShadowMode(RenderQueue.ShadowMode.Receive);

        // The field marking is a bit above the ground in the model
        Geometry fieldMarking = (Geometry) stadium.getChild("stadium-geom-9");
        fieldMarking.setLocalTranslation(0, -0.18f, 0);
        fieldMarking.setShadowMode(RenderQueue.ShadowMode.Receive);

        // Some of the stadium tribunes have a darker blue background than the rest, we always want the bright color
        Geometry darkTribune = (Geometry) stadium.getChild("stadium-geom-13");
        Geometry brightTribune = (Geometry) stadium.getChild("stadium-geom-6");
        darkTribune.setMaterial(brightTribune.getMaterial());
        // The stadium tribunes sometimes have shadow artifacts when casting onto themselves
        brightTribune.setShadowMode(RenderQueue.ShadowMode.Receive);
        darkTribune.setShadowMode(RenderQueue.ShadowMode.Receive);

        // The dark people texture has baked shadows/darkness in it - Since we apply that dynamically on top, we always use the bright texture
        Geometry peopleDark = (Geometry) stadium.getChild("stadium-geom-8");
        Geometry peopleBright1 = (Geometry) stadium.getChild("stadium-geom-3");
        peopleDark.setMaterial(peopleBright1.getMaterial());

        // The side banners are z-fighting the gray background mesh behind them
        Geometry sideBanners = (Geometry) stadium.getChild("stadium-geom-1");
        sideBanners.setUserData("layer", 1);

        String[] audienceGeometryNames = new String[] { "stadium-geom-2", "stadium-geom-3", "stadium-geom-8" };
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

        // But we specifically enable culling for the flags, because they are in the way of the ingame camera
        Geometry flags = (Geometry) stadium.getChild("stadium-geom-0");
        flags.getMaterial().getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Back);

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
