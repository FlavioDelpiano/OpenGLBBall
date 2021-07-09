package org.lwjglb.engine;


public class GameEngine implements Runnable {


    public static final int TARGET_UPS = 60;

    static Window window;

    private final Timer timer;

    private final IGameLogic gameLogic;

    private boolean wind = false;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) {
        if (window == null)
            window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (
                Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }

    }

    protected void init() throws Exception {
        if (!wind) {
            window.init();
            wind = true;
        }
        Utils.initItems();
        timer.init();
        gameLogic.init(window);
    }

    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        while (gameLogic.isRunning() && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

        }
    }

    protected void cleanup() {
        gameLogic.cleanup();
    }


    protected void input() {
        gameLogic.input(window);
    }

    protected void update(float interval) {
        gameLogic.update(interval);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
