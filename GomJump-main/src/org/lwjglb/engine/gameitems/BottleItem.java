package org.lwjglb.engine.gameitems;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

import java.awt.geom.Rectangle2D;


public class BottleItem extends GameItem {

    static Mesh mesh;

    static Material material;

    static Texture texture;

    public static void initCoin() throws Exception {
        mesh = OBJLoader.loadMesh("/models/bottle.obj");
        material = new Material(new Vector4f(1.0f, 1.0f, 0.0f, 1.0f), 1f);
        mesh.setMaterial(material);
        texture = new Texture("./textures/bottle.png");
    }

    public BottleItem(float x){
        super(mesh);
        material.setTexture(texture);
        mesh.setMaterial(material);
        setPosition(x, 1.4f, -2);
        setScale(0.04f);
        setRotation(90, 0, 200);
    }

    public void rotate(float interval) {
        Vector3f vec = getRotation();
        float speed = 180;
        vec.y += speed * interval;
        setRotation(vec.x, vec.y, vec.z);
    }

    @Override
    public Rectangle2D getCollider() {
        return new Rectangle2D.Float((meshes[0].getMinX()) * scaleX + position.x, (meshes[0].getMaxY()) * scaleY + position.y,
                (meshes[0].getMaxX() - meshes[0].getMinX()) * scaleX, (meshes[0].getMaxY() - meshes[0].getMinY()) * scaleY * 2);
    }
}
