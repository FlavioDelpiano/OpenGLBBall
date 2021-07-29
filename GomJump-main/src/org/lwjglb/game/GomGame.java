package org.lwjglb.game;

import org.lwjglb.engine.IGameLogic;
import org.lwjglb.engine.Scene;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Renderer;
import org.lwjglb.game.scenes.BBallScene;
import org.lwjglb.game.scenes.MenuScene;

@SuppressWarnings("DuplicatedCode")
public class GomGame implements IGameLogic {

    private final Renderer renderer;

    private Scene running;
    private Window window;
    private final GameData data;
    private boolean menu = true;


    public GomGame() {
        data = new GameData();
        renderer = new Renderer();
        running = new MenuScene();
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init();
        running.init(window, data);
        this.window = window;
    }


    @Override
    public void input(Window window) {
        running.input(window);
    }

    @Override
    public void update(float interval) {
        running.update(interval, window);
    }

    @Override
    public void render(Window window) {
        renderer.render(window, running);
    }

    public boolean isRunning() {
        if (!running.isRunning()) {
            if (data.isEndGame())
                return false;
            running.stop();
            running.cleanup();
            if (menu)
                running = new BBallScene(data);
            else
                running = new MenuScene();
            menu = !menu;
            try {
                running.init(window, data);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        running.cleanup();
    }
}
