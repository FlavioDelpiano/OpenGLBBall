package org.lwjglb.engine;

import org.lwjglb.engine.gameitems.GameItem;

import java.util.List;

public interface IHud {

    List<GameItem> getGameItems();

    default void cleanup() {
        List<GameItem> gameItems = getGameItems();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
