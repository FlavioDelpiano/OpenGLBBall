package org.lwjglb.engine.gameitems;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;


public class PlayerCharacter extends GameItem {

    static String[] skins = {"./textures/player1.png", "./textures/player0.png", "./textures/player2.png", "./textures/player3.png"};
    static Mesh mesh;
    static Material material;
    static Texture[] textures = new Texture[4];

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

    public void rotate(float interval) {
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

    public PlayerCharacter(int index, float scale ) {
        super(mesh);
        material.setTexture(textures[index]);
        mesh.setMaterial(material);
        setRotation(0, 180, 0);
        setPosition(0, 0, -2);
        setScale(scale);
    }

    public void jumpAnimation(float speed, float maxCharSpeed) {
        float threshold = maxCharSpeed / 6;
        if (speed > maxCharSpeed - 0.003f) {
            setScaleY(0.03f);
            setScaleX(0.07f);
        } else if (speed > maxCharSpeed - threshold) {
            setScaleY(0.07f - (0.04f / (threshold - 0.003f) * (speed - maxCharSpeed + threshold)));
            setScaleX(0.03f + (0.02f / (threshold - 0.003f) * ((speed - maxCharSpeed + threshold))));
        } else if (speed > threshold) {
            setScaleY(0.07f);
            setScaleX(0.03f);
        } else if (speed > 0 && speed <= threshold) {
            setScaleY(0.07f - (0.02f / threshold * (threshold - speed)));
            setScaleX(0.05f - (0.02f / threshold * speed));
        } else
            setScale(0.05f);
    }
}
