package org.lwjglb.engine.gameitems;

import org.joml.Vector3f;
import org.lwjglb.engine.graph.Mesh;

import java.awt.geom.Rectangle2D;

public class GameItem {

    protected final float boundingPad = 0.005f;

    protected Mesh[] meshes;

    protected final Vector3f position;

    protected float scaleX, scaleY, scaleZ;

    protected final Vector3f rotation;

    protected GameItem() {
        position = new Vector3f();
        scaleX = scaleY = scaleZ = 1;
        rotation = new Vector3f();
    }

    public GameItem(Mesh mesh) {
        meshes = new Mesh[]{mesh};
        position = new Vector3f();
        scaleX = scaleY = scaleZ = 1;
        rotation = new Vector3f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Vector3f vect) {
        this.position.x = vect.x;
        this.position.y = vect.y;
        this.position.z = vect.z;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void setScaleZ(float scaleZ) {
        this.scaleZ = scaleZ;
    }

    public void setScale(float scale) {
        scaleX = scaleY = scaleZ = scale;
    }

    public void setScale(float x, float y, float z) {
        scaleX = x;
        scaleY = y;
        scaleZ = z;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    public void setRotation(Vector3f vec) {
        this.rotation.x = vec.x;
        this.rotation.y = vec.y;
        this.rotation.z = vec.z;
    }

    public Rectangle2D getCollider() {
        return new Rectangle2D.Float((getMesh().getMinX()) * scaleX + position.x, (getMesh().getMaxY()) * scaleY + position.y,
                (getMesh().getMaxX() - getMesh().getMinX()) * scaleX, (getMesh().getMaxY() - getMesh().getMinY()) * scaleY);
    }

    public boolean isColliding(Rectangle2D box) {
        return getCollider().intersects(box);
    }

    public Mesh getMesh() {
        return meshes[0];
    }

    public void setMesh(Mesh mesh) {
        if(meshes == null)
            meshes = new Mesh[1];
        this.meshes[0] = mesh;
    }

    public void cleanup() {
        int numMeshes = this.meshes != null ? this.meshes.length : 0;
        for(int i=0; i<numMeshes; i++) {
            this.meshes[i].cleanUp();
        }
    }
}

