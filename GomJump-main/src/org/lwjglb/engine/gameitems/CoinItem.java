package org.lwjglb.engine.gameitems;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

import java.awt.geom.Rectangle2D;


public class CoinItem extends GameItem {

    static String[] skins = {"./textures/coin0.png", "./textures/coin1.png", "./textures/coin2.png", "./textures/coin3.png"};

    static Mesh mesh;

    static Material material;

    static Texture[] textures = new Texture[4];

    public static void initCoin() throws Exception {
        mesh = OBJLoader.loadMesh("/models/coin.obj");
        material = new Material(new Vector4f(1.0f, 1.0f, 0.0f, 1.0f), 1f);
        mesh.setMaterial(material);
        for (int i = 0; i < textures.length; i++) {
            textures[i] = new Texture(skins[i]);
        }

    }

    public void changeSkin(int index){
        material.setTexture(textures[index]);
    }

    public CoinItem(float x, int index){
        super(mesh);
        material.setTexture(textures[index]);
        mesh.setMaterial(material);
        setPosition(x, 1.4f, -2);

        setScale(0.06f);

    }

    public CoinItem( int index) {
        super(mesh);
        material.setTexture(textures[index]);
        mesh.setMaterial(material);
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
