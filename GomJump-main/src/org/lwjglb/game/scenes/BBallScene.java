package org.lwjglb.game.scenes;

import java.util.ArrayList;
import org.joml.Vector3f;
import org.lwjgl.openal.AL11;
import org.lwjglb.engine.IHud;
import org.lwjglb.engine.Scene;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.gameitems.*;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.PointLight;
import org.lwjglb.engine.sound.SoundBuffer;
import org.lwjglb.engine.sound.SoundListener;
import org.lwjglb.engine.sound.SoundManager;
import org.lwjglb.engine.sound.SoundSource;
import org.lwjglb.game.GameData;
import org.lwjglb.game.HUD.GameHud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

//commit test consistenza
public class BBallScene implements Scene{
    private ArrayList<EnemyItem> enemyItems;

    private ArrayList<GameItem> platformItems;

    private ArrayList<CoinItem> ballItems;

    private PlayerCharacter character;

    private Vector3f ambientLight;

    private GameHud gameHud;

    private PointLight pointLight;

    long score = 0;

    private float previous_distance = 0, distance = 0, characterSpeed = 0;

    private static final Random randomPlatform = new Random(System.currentTimeMillis());

    private int livesCount = 3;

    private final Camera camera;

    private final GameData data;

    private int notCreated = 0;

    private final SoundManager soundManager;

    private boolean lost = false;

    private boolean start = false;

    private final PlayerCharacter[] lives = new PlayerCharacter[3]; // da creare un life item

    public BBallScene(GameData data) {
        soundManager = new SoundManager();
        camera = new Camera();
        this.data = data;
    }

    @Override
    public List<GameItem> getGameItems() {
        ArrayList<GameItem> gameItems = new ArrayList<>();
        gameItems.addAll(ballItems);
        gameItems.addAll(platformItems);
        gameItems.addAll(Arrays.asList(lives).subList(0, livesCount));
        gameItems.addAll(enemyItems);
        return gameItems;
    }

    @Override
    public void update(float interval, Window window) {
        gameHud.updateSize(window);
        if (!lost) {
            if (!start) return;
            final float horizontalSpeed = 2.0f;

            if (character != null && !lost) {
                distance += characterSpeed * interval;
                //increaseScore(distance / 0.01); metodo per incremento score
                previous_distance += distance;
                distance = distance % 0.01f;

                if (previous_distance > 0.025f) {
                    try {
                        //double prob = 0.67d - (1d / Math.log(score / 3d + 10d)); piattaforme sempre più rare man mano che vai avanti
                        double prob = 0.95;
                        if (randomPlatform.nextDouble() > prob) {
                            PlatformItem platformItem = new PlatformItem(1.7f - (previous_distance - 0.015f));
                            platformItems.add(platformItem);
                            if (randomPlatform.nextDouble() >= 0.85)
                                ballItems.add(new CoinItem(platformItem, data.getCoinSkinIndex()));
                            notCreated = 0;
                        } else {
                            notCreated++;
                        }

                    } catch (Exception ex) {
                    }
                }


            }
        }
    }

   /* private void updateItem(GameItem item, ArrayList<GameItem> list) {
        Vector3f position = item.getPosition();
        position.x -= characterSpeed;
        if (position.y <= -1.3f) {
            list.add(item);
        } else
            item.setPosition(position);
    }*/
    @Override
    public void input(Window window) {

    }

    @Override
    public void init(Window window, GameData data) throws Exception {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void cleanup() {

    }

    @Override
    public Vector3f getAmbientLight() {
        return null;
    }

    @Override
    public PointLight getPointLight() {
        return null;
    }

    @Override
    public SkyBox getSkybox() {
        return null;
    }

    @Override
    public IHud getHud() {
        return null;
    }

    @Override
    public Camera getCamera() {
        return null;
    }

    @Override
    public void stop() {

    }
}

