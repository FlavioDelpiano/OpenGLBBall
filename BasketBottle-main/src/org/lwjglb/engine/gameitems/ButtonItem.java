package org.lwjglb.engine.gameitems;

import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

public class ButtonItem extends GameItem {

    static final String models = "/models/button.obj";
    static final String[] skin = {"./textures/ButtonStart.png", "./textures/ButtonSkin.png", "./textures/ButtonExit.png"};
    static Mesh[] meshes = new Mesh[3];
    static Texture[] textures = new Texture[3];

    public static void init() throws Exception {
        for (int i = 0; i < textures.length; i++) {
            meshes[i] = OBJLoader.loadMesh(models);
            textures[i] = new Texture(skin[i]);
        }
    }

    public ButtonItem(int index){
        super(meshes[index]);
        Material mat = new Material();
        mat.setTexture(textures[index]);
        getMesh().setMaterial(mat);
        float[] position = {0f, -0.07f, -0.14f};
        float[] rot = {5, -3, 3};
        setPosition(0, position[index], -0.45f);
        setRotation(20, 90 - rot[index], 0);
        setScale(0.1f);
    }

    public static void clear(){
        for (int i = 0; i < textures.length; i++) {
            meshes[i] = null;
            textures[i] = null;
        }
    }
}
