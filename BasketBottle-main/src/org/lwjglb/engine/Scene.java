package org.lwjglb.engine;

import org.joml.Vector3f;
import org.lwjglb.engine.gameitems.GameItem;
import org.lwjglb.engine.gameitems.Background;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.PointLight;
import org.lwjglb.game.GameData;

import java.util.List;

public interface Scene {

    List<GameItem> getGameItems();

    void update(float interval, Window window);

    void input(Window window);

    void init(Window window, GameData data) throws Exception;

    boolean isRunning();

    void cleanup();

    Vector3f getAmbientLight();

    PointLight getPointLight();

    Background getBackground();

    IHud getHud();

    Camera getCamera();

    void stop();
}
