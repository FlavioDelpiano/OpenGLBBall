package org.lwjglb.engine.gameitems;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;


public class PlayerBall extends GameItem {

    static String[] skins = {"./textures/player1.png", "./textures/player0.png"};
    static Mesh mesh;
    static Material material;
    static Texture[] textures = new Texture[2];

    public static void init() throws Exception {
        mesh = OBJLoader.loadMesh("/models/player.obj");
        material = new Material(new Vector4f(1.0f, 1.0f, 0.0f, 1.0f), 1f);
        for (int i = 0; i < textures.length; i++) {
            textures[i] = new Texture(skins[i]);
            mesh.setMaterial(material);
        }
        material.setTexture(textures[0]);

    }

    public void changeSkin(int index){
        material.setTexture(textures[index]);
    }

    public void rotateX(float interval) {
        Vector3f vec = getRotation();
        float speed = 400;
        vec.x += speed * interval;
        setRotation(vec.x, vec.y, vec.z);
    }

    public void rotateY(float interval) {
        Vector3f vec = getRotation();
        float speed = 180;
        vec.y += speed * interval;
        setRotation(vec.x, vec.y, vec.z);
    }

    public void dying(float interval) {
        Vector3f vec = getRotation();
        float speed = 360;
        vec.z += speed * interval;
        setRotation(vec.x, vec.y, vec.z);
    }

    public PlayerBall(int index, float scale ) {
        super(mesh);
        material.setTexture(textures[index]);
        mesh.setMaterial(material);
        setRotation(0, 90, 0);
        setPosition(0, 0, -2);
        setScale(scale);
    }

}
