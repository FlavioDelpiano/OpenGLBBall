package org.lwjglb.engine.gameitems;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

import java.util.Random;

import static org.joml.Math.sin;

public class MascotteItem extends GameItem {


    private final float rotx, roty, rotz;
    private final int  xdir, ydir;
    private float time = 0;
    private static final Random random = new Random(System.currentTimeMillis());
    private static Texture texture;
    private static Mesh mesh;

    public static void init() throws Exception {
        mesh = OBJLoader.loadMesh("/models/mascotte.obj");
        texture = new Texture("./textures/mascotte.png");
    }

    public static void clear(){
        mesh = null;
        texture = null;
    }

    public MascotteItem(float x) {
        super(mesh);
        Material material = new Material(new Vector4f(1, 0, 0, 1), 0.2f);
        getMesh().setMaterial(material);
        material.setTexture(texture);
        rotx = (random.nextFloat() - 0.5f) * 360;
        roty = (random.nextFloat() - 0.5f) * 360;
        rotz = (random.nextFloat() - 0.5f) * 360;

        xdir = (random.nextFloat()) > 0.5f ? 1 : -1;
        ydir = (random.nextFloat()) > 0.5f ? 1 : -1;

        setPosition(x, 1.4f, -2);
        setScale(0.055f);
    }

    public void updateRotation(float interval) {
        Vector3f tmp = getRotation();
        tmp.x += rotx * interval;
        tmp.y += roty * interval;
        tmp.z += rotz * interval;
    }

    public void updatePosition(float interval) {
        time += interval;
        Vector3f tmp = getPosition();
        setPosition(tmp.x + sin(time) * 0.0025f * xdir, tmp.y + sin(time) * 0.0025f * ydir, -2);
    }

}
