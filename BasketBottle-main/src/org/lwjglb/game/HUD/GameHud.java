package org.lwjglb.game.HUD;

import org.joml.Vector4f;
import org.lwjglb.engine.IHud;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.gameitems.GameItem;
import org.lwjglb.engine.gameitems.TextItem;
import org.lwjglb.engine.graph.FontTexture;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameHud implements IHud {

    private static final Font FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font NAMEFONT = new Font("Arial", Font.BOLD, 40);

    private static final String CHARSET = "ISO-8859-1";

    private final ArrayList<GameItem> gameItems = new ArrayList<>();

    private final TextItem scoreTextItem, specialTextItem, highScoreTextItem, nameTextItem, nameValueTextItem;

    private final StringBuilder name = new StringBuilder("");

    boolean lost = false;

    public GameHud(String statusText, String specials, String highScore) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET), nameFontTexture = new FontTexture(NAMEFONT, CHARSET);
        scoreTextItem = new TextItem(statusText, fontTexture);
        scoreTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 0));

        specialTextItem = new TextItem(statusText, fontTexture);
        specialTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 0));

        highScoreTextItem = new TextItem(statusText, fontTexture);
        highScoreTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 0));

        gameItems.add(scoreTextItem);
        gameItems.add(specialTextItem);
        gameItems.add(highScoreTextItem);

        scoreTextItem.setText(statusText);
        specialTextItem.setText(specials);
        highScoreTextItem.setText(highScore);

        nameTextItem = new TextItem("", nameFontTexture);
        nameValueTextItem = new TextItem("", nameFontTexture);
    }

    public void setStatusText(String statusText) {
        this.scoreTextItem.setText(statusText);
    }

    public void setSpecialText(String specialText) {
        this.specialTextItem.setText(specialText);
    }


    @Override
    public List<GameItem> getGameItems() {
        return gameItems;
    }

    public void updateSize(Window window) {
        scoreTextItem.setPosition(10f, window.getHeight() - 90f, 0);
        specialTextItem.setPosition(10f, window.getHeight() - 920f, 0);
        highScoreTextItem.setPosition(window.getWidth() - 200f, window.getHeight() - 920f, 0);

        if (lost) {
            nameTextItem.setPosition(165f, window.getHeight() - 450f, 0);
        }
    }

    public void gameLost(Long score, Long highScore){
        lost = true;
        nameValueTextItem.setText(name.toString());
        gameItems.add(nameValueTextItem);

        if(score > highScore){
            nameTextItem.setText("NEW RECORD!");
            nameTextItem.getMesh().getMaterial().setAmbientColour((new Vector4f(0, 1, 0, 1)));
        }

        else{
            nameTextItem.setText("GAME OVER");
            nameTextItem.getMesh().getMaterial().setAmbientColour((new Vector4f(1, 0.2f, 0, 1)));
        }

        gameItems.add(nameTextItem);
    }

}
