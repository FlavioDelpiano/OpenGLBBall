package org.lwjglb.game;

public class GameData {
    private boolean endGame = false;
    private int playerSkinIndex = 0;
    private int coinSkinIndex = 0;

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

    public int getCoinSkinIndex() {
        return coinSkinIndex;
    }

    public void setCoinSkinIndex(int coinSkinIndex) {
        this.coinSkinIndex = coinSkinIndex;
    }
}
