package org.lwjglb.engine.gameitems;

import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

public class SkyBox extends GameItem {

    private static Mesh skyBoxMesh;

    public static void init() throws Exception {
        skyBoxMesh = OBJLoader.loadMesh("/models/bg.obj");
        Texture skyBoxTexture = new Texture("./textures/skybox.png");
        skyBoxMesh.setMaterial(new Material(skyBoxTexture, 0.5f));
    }

    public SkyBox(){
        super(skyBoxMesh);
        setPosition(0, 0, -190);
    }
}
