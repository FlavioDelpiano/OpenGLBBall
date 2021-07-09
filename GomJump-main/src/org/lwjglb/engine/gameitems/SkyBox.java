package org.lwjglb.engine.gameitems;

import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

public class SkyBox extends GameItem {

    private static Mesh skyBoxMesh;

    public static void init() throws Exception {
        skyBoxMesh = OBJLoader.loadMesh("/models/bg.obj");
        Texture skyBoxtexture = new Texture("./textures/space.png");
        skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.1f));
    }

    public SkyBox(){
        super(skyBoxMesh);
        setPosition(0, 0, -190);
    }
}
