package org.lwjglb.engine;

public class Timer {

    private double lastTime;

    public void init() {
        lastTime = getTime();
    }

    public double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }

    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastTime);
        lastTime = time;
        return elapsedTime;
    }

}
