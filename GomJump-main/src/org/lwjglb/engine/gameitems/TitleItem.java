package org.lwjglb.engine.gameitems;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.OBJLoader;

import static org.joml.Math.sin;
public class TitleItem  extends GameItem{

    private float titleInt = 0;

    public TitleItem() throws Exception {
        super(OBJLoader.loadMesh("/models/title.obj"));
        Material mat = new Material(new Vector4f(0.9f, 0.5f, 0.2f, 1f), 0f);
        getMesh().setMaterial(mat);
        setPosition(0, 0.25f, -0.45f);
        setScale(0.1f);
    }

    public void updateTitle(float interval) {
        titleInt += interval;
        if (titleInt >= 3.1415 * 2 * 3) {
            titleInt -= 3.1415 * 2 * 3;
        }
        setPosition(sin(titleInt / 3) * 0.001f,  0.15f + sin(titleInt) * 0.001f, -0.5f);
    }
}
