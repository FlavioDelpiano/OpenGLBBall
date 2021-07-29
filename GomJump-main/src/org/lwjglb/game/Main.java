package org.lwjglb.game;

import org.lwjglb.engine.GameEngine;
import org.lwjglb.engine.IGameLogic;


public class Main {

    public static void main(String[] args) {
        try {
            IGameLogic gameLogic = new BBallGame();
            GameEngine gameEng = new GameEngine("Basket Bottle", 600, 960, true, gameLogic);
            gameEng.run();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}