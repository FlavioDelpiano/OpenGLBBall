package org.lwjglb.engine.gameitems;

import org.joml.Math;
import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

import java.awt.geom.Rectangle2D;
import java.util.Random;

public class PlatformItem extends GameItem {

    private static final Random randomPlatformPosition = new Random(System.currentTimeMillis());

    private static Mesh mesh, mesh1;

    private static Texture texture, texture1;

    private float time = 0;

    public static void initPlatform() throws Exception {
        mesh = OBJLoader.loadMesh("/models/platform.obj");
        mesh1 = OBJLoader.loadMesh("/models/platform.obj");
        texture = new Texture("./textures/platform.png");
        texture1 = new Texture("./textures/platform1.png");
        Material material = new Material(new Vector4f(1.0f, 1.0f, 0.0f, 1.0f), 1f),
                material1 = new Material(new Vector4f(1.0f, 1.0f, 0.0f, 1.0f), 1f);
        mesh.setMaterial(material);
        material.setTexture(texture);

        mesh1.setMaterial(material1);
        material1.setTexture(texture1);
    }

    public static void clear(){
        mesh1 = mesh = null;
        texture = texture1 = null;
    }

    public PlatformItem(float y) {
        super(randomPlatformPosition.nextFloat() >0.2f ? mesh : mesh1);
        setScaleY(0.1f);
        setScaleZ(0.1f);
        setScaleX(randomPlatformPosition.nextFloat() * 0.19f + 0.01f);
        float x = -0.65f + randomPlatformPosition.nextFloat() * (1.3f);
        setPosition(x, y, -2);
    }

    public PlatformItem(float x, float y)  {
        super(mesh);
        setScale(0.1f);

        setPosition(x, y, -2);
    }

    public void updateRot(float interval){
        time += interval;
        var tmp = getRotation();
        tmp.y = Math.sin(time) * 60;
        setRotation(tmp);
    }

    @Override
    public Rectangle2D getCollider() {
        return new Rectangle2D.Float((mesh.getMinX()) * scaleX + position.x, (mesh.getMaxY()) * scaleY / 1.5f + position.y,
                (mesh.getMaxX() - mesh.getMinX()) * scaleX, (mesh.getMaxY() - mesh.getMinY()) * scaleY / 1.5f);
    }
}
