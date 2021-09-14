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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class BBallScene implements Scene{
    private ArrayList<MascotteItem> mascotteItems;

    private ArrayList<BottleItem> bottleItems;

    private PlayerBall character;

    private Vector3f ambientLight;

    private GameHud gameHud;

    private PointLight pointLight;

    long score = 0;

    private Background background;

    private boolean hit = false, left = true, right = false, running = true;

    private float previous_distance = 0, distance = 0, characterSpeed = 0;

    private static final Random itemsRandom = new Random(System.currentTimeMillis());

    private final Camera camera;

    private final GameData data;

    private final SoundManager soundManager;

    private boolean lost = false;

    private boolean start = false;

    private float interval;

    private int special;

    private int safeFrames;

    public BBallScene(GameData data) {
        soundManager = new SoundManager();
        camera = new Camera();
        this.data = data;
    }

    @Override
    public List<GameItem> getGameItems() {
        ArrayList<GameItem> gameItems = new ArrayList<>();
        gameItems.add(character);
        gameItems.addAll(bottleItems);
        gameItems.addAll(mascotteItems);
        return gameItems;
    }

    @Override
    public void update(float interval, Window window) {

        this.interval = interval;
        gameHud.updateSize(window);
        if (!lost) {
            if (!start) return;
            final float verticalSpeed = 0.002f, horizontalSpeed = 1.2f;

            if (character != null && !hit) {
                distance += characterSpeed * interval;
                increaseScore(distance / 0.01);
                previous_distance += distance;
                distance = distance % 0.01f;

                character.rotateX(interval);

                if (previous_distance > 0.0275f) {
                    try {
                        var val = itemsRandom.nextDouble();

                            if (val >= 0.96) {
                                MascotteItem enemy = new MascotteItem(itemsRandom.nextFloat() * 1.4f - 0.7f);
                                if (val <= 0.98) {
                                    bottleItems.add(new BottleItem(itemsRandom.nextFloat() * 1.4f - 0.7f));// cambiare meto
                                }

                                boolean set = true;
                                for (var b : bottleItems) {
                                    if (b.isColliding(enemy.getCollider())) {
                                        set = false;
                                        break;
                                    }
                                }
                                if (set)
                                    mascotteItems.add(enemy);
                            }

                        previous_distance = 0;

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            characterSpeed += Math.log(1 + interval * verticalSpeed/4);

            if (character != null && !hit) {
                ArrayList<GameItem> toRemoveBottles = new ArrayList<>();
                bottleItems.forEach(c -> updateItem(c, toRemoveBottles));
                bottleItems.removeAll(toRemoveBottles);
                ArrayList<GameItem> toRemoveEnemy = new ArrayList<>();
                mascotteItems.forEach(mascotteItem -> updateItem(mascotteItem, toRemoveEnemy));
                mascotteItems.removeAll(toRemoveEnemy);
            }

            if (character != null && left != right) {
                if (left) {
                    left = false;
                    Vector3f position = character.getPosition();
                    position.x -= horizontalSpeed * interval;
                }
                if (right) {
                    right = false;
                    Vector3f position = character.getPosition();
                    position.x += horizontalSpeed * interval;
                }
            } else {
                left = right = false;
            }

            BottleItem bottle = null;
            if (character != null) {
                for (BottleItem b : bottleItems) {
                    if (b.isColliding(character.getCollider())) {
                        bottle = b;
                        soundManager.playSoundSource("bottle");
                        break;
                    }
                }
            }
            if (bottle != null) {
                bottleItems.remove(bottle);
                increaseScore(30);
            }


            if (character != null) {
                if(safeFrames > 0){
                    safeFrames--;
                }
                else{
                    for (MascotteItem e : mascotteItems) {
                        if (e.isColliding(character.getCollider())) {
                            soundManager.playSoundSource("crash");
                            lost = true;
                            gameHud.setStatusText("GAME OVER");
                            gameHud.gameLost();
                            characterSpeed = 0;
                            break;
                        }
                    }
                }

            }

            if (score >= 200) mascotteItems.forEach(mascotteItem -> mascotteItem.updatePosition(interval));

            if (character != null) {
                gameHud.setStatusText(String.format("%06d", score));
            }


        } else if (character != null) {
            var vec = character.getPosition();
            character.setPosition(vec);
            character.dying(interval);
        }

        if (character == null || hit) {
            lost = true;
            gameHud.setStatusText("GAME OVER");
            gameHud.gameLost();
            character = null;
        }

    }

    private void updateItem(GameItem item, ArrayList<GameItem> list) {
        Vector3f position = item.getPosition();
        position.y -= characterSpeed;
        if (position.y <= -1.3f) {
            list.add(item);
        } else
            item.setPosition(position);
    }

    private void increaseScore(double value) {
        score += value;
    }

    @Override
    public void input(Window window) {

        if (!lost) {
            if ((window.isKeyPressed(GLFW_KEY_LEFT) || window.isKeyPressed(GLFW_KEY_A)) && character != null && character.getPosition().x > -0.6) {
                left = true;
            }

            if ((window.isKeyPressed(GLFW_KEY_RIGHT) || window.isKeyPressed(GLFW_KEY_D)) && character != null && character.getPosition().x < 0.6) {
                right = true;
            }

            if (window.isKeyDown(GLFW_KEY_SPACE) && character != null && special > 0) {
                safeFrames = 120;
                special--;
                gameHud.setSpecialText(String.format("Special Saves: %1d", special));
            }

            if (!start && window.isKeyDown(GLFW_KEY_ENTER))
                start = true;
        }
        if (window.isKeyPressed(GLFW_KEY_ESCAPE))
            running = false;
    }

    @Override
    public void init(Window window, GameData data) throws Exception {
        MascotteItem.init();
        gameHud = new GameHud("Press enter to start", "Special Saves: 3");
        gameHud.updateSize(window);

        special = 3;
        safeFrames = 0;

        character = new PlayerBall(data.getPlayerSkinIndex(), 0.06f);
        Vector3f position = character.getPosition();
        position.y -= 0.6f;
        character.setPosition(position);
        bottleItems = new ArrayList<>();
        mascotteItems = new ArrayList<>();

        background = new Background();
        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 9.0f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        initSound();
    }

    public void initSound() throws Exception {
        soundManager.init();
        soundManager.setAttenuationModel(AL11.AL_LINEAR_DISTANCE);

        SoundBuffer buffSoundtrack = new SoundBuffer("/sound/game_st.ogg");
        soundManager.addSoundBuffer(buffSoundtrack);
        SoundSource sourceSoundtrack = new SoundSource(true, false);
        sourceSoundtrack.setBuffer(buffSoundtrack.getBufferId());
        soundManager.addSoundSource("soundtrack", sourceSoundtrack);

        soundManager.setListener(new SoundListener(new Vector3f()));
        soundManager.setVolume("soundtrack", 0.25f);
        soundManager.playSoundSource("soundtrack");

        SoundBuffer buffBottle = new SoundBuffer("/sound/bottle.ogg");
        soundManager.addSoundBuffer(buffBottle);
        SoundSource sourceBottle = new SoundSource(false, false);
        sourceBottle.setBuffer(buffBottle.getBufferId());
        soundManager.addSoundSource("bottle", sourceBottle);
        soundManager.setVolume("bottle", 1.5f);

        SoundBuffer buffCrash = new SoundBuffer("/sound/crash.ogg");
        soundManager.addSoundBuffer(buffCrash);
        SoundSource sourceHit = new SoundSource(false, false);
        sourceHit.setBuffer(buffCrash.getBufferId());
        soundManager.addSoundSource("crash", sourceHit);
        soundManager.setVolume("crash", 1f);
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void cleanup() {
        MascotteItem.clear();
        mascotteItems.forEach(GameItem::cleanup);
        soundManager.cleanup();
        gameHud.cleanup();
    }

    @Override
    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    @Override
    public PointLight getPointLight() {
        return pointLight;
    }

    @Override
    public Background getBackground() {
        return background;
    }

    @Override
    public IHud getHud() {
        return gameHud;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public void stop() {
        soundManager.getAllSources().forEach(SoundSource::stop);
    }
}

