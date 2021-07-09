package org.lwjglb.game.HUD;

import org.lwjglb.engine.IHud;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.gameitems.GameItem;
import org.lwjglb.engine.gameitems.TextItem;
import org.lwjglb.engine.graph.FontTexture;
import org.lwjglb.game.GameData;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class HighScoresHUD implements IHud {

    private static final Font FONT = new Font("Arial", Font.PLAIN, 40);

    private static final String CHARSET = "ISO-8859-1";

    TextItem[] textItems = new TextItem[10];

    public HighScoresHUD() throws Exception {
        FontTexture fontTexture = new FontTexture(FONT, CHARSET);
        for (int i = 0; i < textItems.length; i++) {
            textItems[i] = new TextItem(i + "", fontTexture);
        }
    }

    public void updateText(GameData data) {
        var list = data.getDatabase().getScores();
        for (int i = 0; i < textItems.length; i++) {
            if (i < list.size())
                textItems[i].setText((i + 1) + ". " + list.get(i).getName() + " - " + list.get(i).getScore());
            else
                textItems[i].setText((i + 1) + ". ");
        }
    }

    @Override
    public List<GameItem> getGameItems() {
        return Arrays.asList(textItems);
    }

    public void updateSize(Window window) {
        for (int i = textItems.length - 1; i >= 0; i--) {
            textItems[i].setPosition(125f, window.getHeight() - 600f + 50f * i, 0);
        }
    }

}
