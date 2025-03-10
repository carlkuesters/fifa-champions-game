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
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import jme3utilities.sky.SkyControl;
import jme3utilities.sky.StarsOption;

import java.util.ArrayList;

public class StadiumAppState extends BaseDisplayAppState {

    private ArrayList<Material> audienceMaterials;

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

        SceneProcessorAppState sceneProcessorAppState = getAppState(SceneProcessorAppState.class);
        sceneProcessorAppState.addFilter(new SSAOFilter(10, 25, 6, 0.1f));

        DirectionalLightShadowRenderer directionalLightShadowRenderer = new DirectionalLightShadowRenderer(mainApplication.getAssetManager(), 8192, 4);
        directionalLightShadowRenderer.setLight(directionalLight);
        directionalLightShadowRenderer.setLambda(0.99f);
        directionalLightShadowRenderer.setShadowIntensity(0.4f);
        directionalLightShadowRenderer.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        sceneProcessorAppState.addProcessor(directionalLightShadowRenderer);

        SkyControl skyControl = new SkyControl(mainApplication.getAssetManager(), mainApplication.getCamera(), 0.7f, StarsOption.Cube, true);
        skyControl.setCloudiness(0.8f);
        skyControl.getSunAndStars().setHour(12);
        mainApplication.getRootNode().addControl(skyControl);
        skyControl.setEnabled(true);

        Node stadium = (Node) mainApplication.getAssetManager().loadModel("models/stadium/stadium.j3o");
        stadium.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        // Rotate it so that the player entrance gate is lighted
        Node wrapper = new Node();
        wrapper.attachChild(stadium);
        wrapper.rotate(0, FastMath.PI, 0);
        mainApplication.getRootNode().attachChild(wrapper);

        Geometry flags = (Geometry) stadium.getChild("flags_0");
        Geometry grass = (Geometry) stadium.getChild("grass_0");
        Geometry marking = (Geometry) stadium.getChild("marking_0");
        Geometry sideBanners = (Geometry) stadium.getChild("banners_0");
        Geometry peopleBright1 = (Geometry) stadium.getChild("people_bright_1.001_0");

        stadium.depthFirstTraversal(spatial -> {
            if (spatial instanceof Geometry geometry) {
                // Some faces (e.g. roof, goal nets, fences) should be rendered from both sides, for now we simply disable culling for all
                // But we specifically keep culling for the flags, because they are in the way of the ingame camera
                geometry.getMaterial().getAdditionalRenderState().setFaceCullMode((geometry == flags) ? RenderState.FaceCullMode.Back : RenderState.FaceCullMode.Off);

                // Alpha discard threshold is needed for multiple things:
                // - Even when splitting up the people sections into own objects, the transparent bucket sorting (based on cam distance to bounding box edge) is not perfect and objects wrongly overdraw/clear each other
                // - Without it, transparent objects cast solid shadows instead of respecting the texture (e.g. goal net casting a solid shadow box instead of a net shadow)
                if (geometry.getQueueBucket() == RenderQueue.Bucket.Transparent) {
                    // The field marking has a fading-out alpha texture (that looks ugly otherwise) and anyway doesn't have other transparent stuff behind it
                    if (geometry != marking) {
                        geometry.getMaterial().setFloat("AlphaDiscardThreshold", 0.1f);
                    }
                }

                // The dark people texture has baked shadows/darkness in it - Since we apply that dynamically on top, we always use the bright texture
                if (geometry.getName().startsWith("people_dark.")) {
                    geometry.setMaterial(peopleBright1.getMaterial());
                }
            }
        });

        // The bright and dark grass stripes are not well aligned with the field markings otherwise
        JMonkeyUtil.shiftAndScaleTextureCoordinates(grass.getMesh(), new Vector2f(0.02f, 0), new Vector2f(1.31f, 1.31f));

        // The grass is for whatever reason super bright after the export, maybe the normals are not exported properly
        JMonkeyUtil.setAmbient(grass, 0.4f);

        // The grass and marking otherwise are z-fighting, especially regarding shadows (sometimes drawn doubled otherwise)
        grass.setQueueBucket(RenderQueue.Bucket.Transparent);
        grass.setShadowMode(RenderQueue.ShadowMode.Receive);
        grass.getMaterial().getAdditionalRenderState().setDepthWrite(false);
        grass.setUserData("layer", -1);
        marking.setShadowMode(RenderQueue.ShadowMode.Off);
        marking.getMaterial().getAdditionalRenderState().setDepthWrite(false);

        // The side banners are z-fighting the gray background mesh behind them
        sideBanners.setUserData("layer", 1);

        audienceMaterials = new ArrayList<>();
        stadium.depthFirstTraversal(spatial -> {
            if (spatial instanceof Geometry geometry) {
                if (geometry.getName().startsWith("people_")) {
                    Material oldMaterial = geometry.getMaterial();
                    Material newMaterial = new Material(mainApplication.getAssetManager(), "materials/audience.j3md");
                    for (MatParam matParam : oldMaterial.getParams()) {
                        if (newMaterial.getMaterialDef().getMaterialParam(matParam.getName()) != null) {
                            newMaterial.setParam(matParam.getName(), matParam.getVarType(), matParam.getValue());
                        }
                    }
                    newMaterial.getAdditionalRenderState().setBlendMode(oldMaterial.getAdditionalRenderState().getBlendMode());
                    newMaterial.getAdditionalRenderState().setFaceCullMode(oldMaterial.getAdditionalRenderState().getFaceCullMode());
                    geometry.setMaterial(newMaterial);
                    geometry.addControl(new TimeMaterialParamControl("Time"));
                    audienceMaterials.add(newMaterial);
                }
            }
        });
        setAudienceHyped(false);

        if (false) {
            Geometry fieldTestBounds = new Geometry("", new Box(Game.FIELD_HALF_WIDTH, Game.GOAL_HEIGHT / 2, Game.FIELD_HALF_HEIGHT));
            fieldTestBounds.setLocalTranslation(0, (Game.GOAL_HEIGHT / 2), 0);
            Material fieldMat = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
            fieldMat.setBoolean("UseMaterialColors", true);
            ColorRGBA fieldColor = ColorRGBA.Green.clone().setAlpha(0.3f);
            fieldMat.setColor("Ambient", fieldColor);
            fieldMat.setColor("Diffuse", fieldColor);
            fieldMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            fieldMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
            fieldTestBounds.setMaterial(fieldMat);
            fieldTestBounds.setQueueBucket(RenderQueue.Bucket.Transparent);
            mainApplication.getRootNode().attachChild(fieldTestBounds);

            for (int i = 0; i < 2; i++) {
                Geometry goalTestBounds = new Geometry("", new Box(Game.GOAL_WIDTH / 2, Game.GOAL_HEIGHT / 2, Game.GOAL_DEPTH / 2));
                int side = ((i == 0) ? 1 : -1);
                float x = (side * (Game.FIELD_HALF_WIDTH + (Game.GOAL_WIDTH / 2)));
                goalTestBounds.setLocalTranslation(x, (Game.GOAL_HEIGHT / 2), 0);
                Material goalMat = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
                goalMat.setBoolean("UseMaterialColors", true);
                ColorRGBA goalColor = ColorRGBA.Blue.clone().setAlpha(0.3f);
                goalMat.setColor("Ambient", goalColor);
                goalMat.setColor("Diffuse", goalColor);
                goalMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                goalMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
                goalTestBounds.setMaterial(goalMat);
                goalTestBounds.setQueueBucket(RenderQueue.Bucket.Transparent);
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
