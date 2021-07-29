package org.lwjglb.engine.gameitems;

import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

public class Background extends GameItem {

    private static Mesh bgMesh;

    public static void init() throws Exception {
        bgMesh = OBJLoader.loadMesh("/models/bg.obj");
        Texture skyBoxTexture = new Texture("./textures/skybox.png");
        bgMesh.setMaterial(new Material(skyBoxTexture, 0.5f));
    }

    public Background(){
        super(bgMesh);
        setPosition(0, 0, -190);
    }
}
