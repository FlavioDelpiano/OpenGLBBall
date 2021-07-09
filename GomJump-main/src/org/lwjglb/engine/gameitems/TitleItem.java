package org.lwjglb.engine.gameitems;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.OBJLoader;

import static org.joml.Math.sin;
public class TitleItem  extends GameItem{

    private float titleInterval = 0;

    public TitleItem() throws Exception {
        super(OBJLoader.loadMesh("/models/title.obj"));
        Material mat = new Material(new Vector4f(1f, 0.71f, 0.35f, 1f), 1f);
        getMesh().setMaterial(mat);
        setPosition(0, 0.25f, -0.45f);
        setScale(0.11f);
    }

    public void updateTitle(float interval) {
        titleInterval += interval;
        if (titleInterval >= 3.1415 * 2 * 3) {
            titleInterval -= 3.1415 * 2 * 3;
        }
        setPosition(-sin(titleInterval / 3) * 0.005f,  0.15f + sin(titleInterval) * 0.005f, -0.45f);
    }
}
