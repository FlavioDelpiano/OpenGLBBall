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

    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);
    private static final Font NAMEFONT = new Font("Arial", Font.PLAIN, 40);

    private static final String CHARSET = "ISO-8859-1";

    private final ArrayList<GameItem> gameItems = new ArrayList<>();

    private final TextItem statusTextItem, lifeTextItem, nameTextItem, nameValueTextItem, powerupTextItem;

    private final StringBuilder name = new StringBuilder("");

    boolean lost = false;

    public GameHud(String statusText) throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET), nameFontTexture = new FontTexture(NAMEFONT, CHARSET);
        statusTextItem = new TextItem(statusText, fontTexture);
        statusTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 0));

        lifeTextItem = new TextItem(statusText, fontTexture);
        lifeTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 0));

        powerupTextItem = new TextItem("Platform: ", fontTexture);
        gameItems.add(powerupTextItem);

        gameItems.add(statusTextItem);
        statusTextItem.setText(statusText);


        gameItems.add(lifeTextItem);
        lifeTextItem.setText("Lives: ");


        nameTextItem = new TextItem("", nameFontTexture);
        nameValueTextItem = new TextItem("", nameFontTexture);

    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }


    @Override
    public List<GameItem> getGameItems() {
        return gameItems;
    }

    public void updateSize(Window window) {

        lifeTextItem.setPosition(10f, window.getHeight() - 950f, 0);
        statusTextItem.setPosition(10f, window.getHeight() - 90f, 0);
        powerupTextItem.setPosition(280f, window.getHeight() - 950f, 0);
        if (lost) {
            nameTextItem.setPosition(125f, window.getHeight() - 450f, 0);
            nameValueTextItem.setPosition(245f, window.getHeight() - 450f, 0);
        }
    }

    public void gameLost() {
        lost = true;
        nameValueTextItem.setText(name.toString());
        gameItems.add(nameValueTextItem);
        nameTextItem.setText("Name:");
        gameItems.add(nameTextItem);

    }

    public void addChar(char c) {
        if (name.length() < 5)
            name.append(c);
        nameValueTextItem.setText(name.toString());
    }

    public void delete() {
        if (name.length() > 0) {
            name.deleteCharAt(name.length() - 1);
        }
    }

    public String getName() {
        return name.toString();
    }
}
