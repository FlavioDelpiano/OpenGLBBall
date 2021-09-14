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

    private final TextItem scoreTextItem, specialTextItem, nameTextItem, nameValueTextItem;

    private final StringBuilder name = new StringBuilder("");

    boolean lost = false;

    public GameHud(String statusText, String specials) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET), nameFontTexture = new FontTexture(NAMEFONT, CHARSET);
        scoreTextItem = new TextItem(statusText, fontTexture);
        scoreTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 0));

        specialTextItem = new TextItem(statusText, fontTexture);
        specialTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 0));

        gameItems.add(scoreTextItem);
        gameItems.add(specialTextItem);
        scoreTextItem.setText(statusText);
        specialTextItem.setText(specials);

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

        if (lost) {
            nameTextItem.setPosition(55f, window.getHeight() - 450f, 0);
        }
    }

    public void gameLost() {
        lost = true;
        nameValueTextItem.setText(name.toString());
        gameItems.add(nameValueTextItem);
        nameTextItem.setText("PRESS esc TO GO BACK");
        gameItems.add(nameTextItem);

    }

}
