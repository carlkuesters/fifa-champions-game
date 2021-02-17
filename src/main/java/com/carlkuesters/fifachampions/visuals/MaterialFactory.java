package com.carlkuesters.fifachampions.visuals;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture;

public class MaterialFactory {

    public static Texture loadTexture(AssetManager assetManager, String filePath) {
        return assetManager.loadTexture(new TextureKey(filePath, false));
    }
}
