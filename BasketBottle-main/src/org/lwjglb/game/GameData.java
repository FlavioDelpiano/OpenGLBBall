package org.lwjglb.game;

public class GameData {
    private boolean endGame = false;
    private int playerSkinIndex = 0;

    public boolean isEndGame() {
        return endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public int getPlayerSkinIndex() {
        return playerSkinIndex;
    }

    public void setPlayerSkinIndex(int playerSkinIndex) {
        this.playerSkinIndex = playerSkinIndex;
    }

}
