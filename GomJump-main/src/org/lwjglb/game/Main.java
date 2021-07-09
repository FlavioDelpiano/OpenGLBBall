package org.lwjglb.game;

import org.lwjglb.engine.GameEngine;
import org.lwjglb.engine.IGameLogic;


public class Main {

    public static void main(String[] args) {
        try {
            IGameLogic gameLogic = new GomGame();
            GameEngine gameEng = new GameEngine("Gom jump", 960, 540, true, gameLogic);
            gameEng.run();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}