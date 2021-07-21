package org.lwjglb.game.scenes;

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


public class GameScene implements Scene {

    private ArrayList<GameItem> platformItems;

    private ArrayList<BottleItem> bottleItems;

    private ArrayList<EnemyItem> enemyItems;

    private PlayerCharacter playerCharacter;

    private Vector3f ambientLight;

    private PointLight pointLight;

    private final float gravity = 0.04f, maxCharacterSpeed = gravity * 1.3f;

    private float characterSpeed = maxCharacterSpeed;

    private boolean left = true, right = false;

    private GameHud gameHud;

    private SkyBox skyBox;

    private final SoundManager soundManager;

    long score = 0;

    private float previous_distance = 0, distance = 0;

    private int notCreated = 0;

    private static final Random platformRandom = new Random(System.currentTimeMillis());

    private boolean running = true;

    private final Camera camera;

    private boolean start = false;

    private final GameData data;

    private int livesCount = 3, powerCount = 0, previousBonus = 0;

    private final PlatformItem[] power = new PlatformItem[2];

    private final PlayerCharacter[] lives = new PlayerCharacter[3];

    private int immunityFrames = 0;

    private boolean lost = false;


    public GameScene(GameData data) {
        soundManager = new SoundManager();
        camera = new Camera();
        this.data = data;
    }

    @Override
    public List<GameItem> getGameItems() {
        ArrayList<GameItem> gameItems = new ArrayList<>();
        if (((immunityFrames / 2) % 2) != 1)
            gameItems.add(playerCharacter);
        gameItems.addAll(platformItems);
        gameItems.addAll(bottleItems);
        gameItems.addAll(Arrays.asList(lives).subList(0, livesCount));
        gameItems.addAll(Arrays.asList(power).subList(0, powerCount));
        gameItems.addAll(enemyItems);
        return gameItems;
    }

    private void increaseScore(double value) {
        score += value;
        if (score / 200 > previousBonus && powerCount < 2) {
            previousBonus++;
            powerCount++;
        }
    }

    @Override
    public void update(float interval, Window window) {
        gameHud.updateSize(window);
        if (!lost) {
            for (var po : power) {
                po.updateRot(interval);
            }
            for (int i = 0; i < livesCount; i++) {
                lives[i].rotate(interval / 2);
            }
            if (!start) return; // aspetto a far partire
            final float horizontalSpeed = 2.0f;
            final float jumpLimit = -0.15f;
            if (playerCharacter != null && characterSpeed > 0 && playerCharacter.getPosition().y >= jumpLimit) {
                distance += characterSpeed * interval;
                increaseScore(distance / 0.01);
                previous_distance += distance;
                distance = distance % 0.01f;
                if (previous_distance > 0.025f) {
                    try {
                        double prob = 0.67d - (1d / Math.log(score / 3d + 10d));
                        if (platformRandom.nextDouble() > prob || notCreated >= 2) {
                            PlatformItem platformItem = new PlatformItem(1.7f - (previous_distance - 0.015f));
                            platformItems.add(platformItem);
                            if (platformRandom.nextDouble() >= 0.85)
                                //coinItems.add(new CoinItem(platformItem, data.getCoinSkinIndex()));
                            notCreated = 0;
                        } else {
                            notCreated++;
                        }
                        var val = platformRandom.nextDouble();
                        if (val >= 0.92 && score >= 200) {
                            EnemyItem tmpItem = new EnemyItem(platformRandom.nextFloat() * 1.4f - 0.7f);
                            boolean set = true;
                            for (var plat : platformItems) {
                                if (plat.isColliding(tmpItem.getCollider())) {
                                    set = false;
                                    break;
                                }
                            }
                            if (set)
                                enemyItems.add(tmpItem);
                        }
                        previous_distance = 0;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
            characterSpeed -= gravity * interval;

            ArrayList<GameItem> toRemovePlatforms = new ArrayList<>();
            if (playerCharacter != null && characterSpeed > 0 && playerCharacter.getPosition().y > jumpLimit) {
                platformItems.forEach(gm -> updateItem(gm, toRemovePlatforms));
                platformItems.removeAll(toRemovePlatforms);
                ArrayList<GameItem> toRemoveCoins = new ArrayList<>();
                bottleItems.forEach(c -> updateItem(c, toRemoveCoins));
                bottleItems.removeAll(toRemoveCoins);
                ArrayList<GameItem> toRemoveEnemy = new ArrayList<>();
                enemyItems.forEach(enemyItem -> updateItem(enemyItem, toRemoveEnemy));
                enemyItems.removeAll(toRemoveEnemy);
            } else if (playerCharacter != null) {
                Vector3f position = playerCharacter.getPosition();
                position.y += characterSpeed;
                playerCharacter.setPosition(position);
            }
            //horizontal movement
            if (playerCharacter != null && left != right) {
                if (left) {
                    left = false;
                    Vector3f position = playerCharacter.getPosition();
                    position.x -= horizontalSpeed * interval;

                }
                if (right) {
                    right = false;
                    Vector3f position = playerCharacter.getPosition();
                    position.x += horizontalSpeed * interval;


                }
            } else {
                left = right = false;
            }

            if (playerCharacter != null && characterSpeed <= 0) {
                for (GameItem item : platformItems) {
                    if (item.isColliding(playerCharacter.getCollider())) {
                        soundManager.playSoundSource("jump");
                        characterSpeed = maxCharacterSpeed;
                    }
                }
            }

            BottleItem coin = null;
            if (playerCharacter != null) {
                for (BottleItem c : bottleItems) {
                    if (c.isColliding(playerCharacter.getCollider())) {
                        coin = c;
                        soundManager.playSoundSource("coin");
                        break;
                    }
                }
            }
            if (coin != null) {
                bottleItems.remove(coin);
                increaseScore(20);
            }

            if (immunityFrames > 0)
                immunityFrames--;
            //Enemy hit
            if (immunityFrames == 0) {
                EnemyItem enemy = null;
                if (playerCharacter != null) {
                    for (EnemyItem e : enemyItems) {
                        if (e.isColliding(playerCharacter.getCollider())) {
                            enemy = e;
                            soundManager.playSoundSource("hit");
                            break;
                        }
                    }
                }


                if (enemy != null) {
                    enemyItems.remove(enemy);
                    livesCount--;
                    if (livesCount <= 0) {
                        lost = true;
                        gameHud.setStatusText("GAME OVER");
                        gameHud.gameLost();
                        characterSpeed = 0;
                    }
                    immunityFrames = 20;
                }
            }

            //animations

            enemyItems.forEach(enemyItem -> enemyItem.updateRotation(interval));
            if (score >= 500) enemyItems.forEach(enemyItem -> enemyItem.updatePosition(interval));

            bottleItems.forEach(c -> c.rotate(interval));
            if (playerCharacter != null) {
                playerCharacter.jumpAnimation(characterSpeed, maxCharacterSpeed);
                gameHud.setStatusText(String.format("%07d", score));
            }
        } else if (playerCharacter != null) {
            var vec = playerCharacter.getPosition();
            characterSpeed -= gravity * interval;
            vec.y += characterSpeed;
            playerCharacter.setPosition(vec);
            playerCharacter.dying(interval);
        }
        if (playerCharacter == null || playerCharacter.getPosition().y < -1.4f) {
            lost = true;
            gameHud.setStatusText("GAME OVER");
            gameHud.gameLost();
            playerCharacter = null;
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

    @Override
    public void input(Window window) {
        if (!lost) {
            if ((window.isKeyPressed(GLFW_KEY_LEFT) || window.isKeyPressed(GLFW_KEY_A)) && playerCharacter != null && playerCharacter.getPosition().x > -0.6) {
                left = true;
            }

            if ((window.isKeyPressed(GLFW_KEY_RIGHT) || window.isKeyPressed(GLFW_KEY_D)) && playerCharacter != null && playerCharacter.getPosition().x < 0.6) {
                right = true;
            }

            if (!start && window.isKeyDown(GLFW_KEY_SPACE))
                start = true;
            if (window.isKeyDown(GLFW_KEY_SPACE) && powerCount > 0) {
                powerCount--;
                var vec = playerCharacter.getPosition();
                var plat = new PlatformItem(vec.x, vec.y - 0.02f >= -1.4f ? vec.y - 0.02f : -1.39f);

                platformItems.add(plat);
            }
        } else {
            var c = window.getCharKeyDown();
            c.ifPresent(character -> gameHud.addChar(character));

            if (window.isKeyDown(GLFW_KEY_BACKSPACE)) gameHud.delete();

            if ((window.isKeyDown(GLFW_KEY_ENTER) || window.isKeyDown(GLFW_KEY_KP_ENTER)) && !gameHud.getName().trim().isEmpty()) {
                String name = gameHud.getName();
                data.getDatabase().insertScore(name, score);
                running = false;
            }
        }

        if (window.isKeyPressed(GLFW_KEY_ESCAPE))
            running = false;
    }

    @Override
    public void init(Window window, GameData data) throws Exception {
        EnemyItem.init();
        PlatformItem.initPlatform();
        gameHud = new GameHud("Press enter to start");
        gameHud.updateSize(window);
        for (int i = 0; i < 3; i++) {
            lives[i] = new PlayerCharacter(data.getPlayerSkinIndex(), 0.023f);
            lives[i].setRotation(-20, 180, 0);
            lives[i].setPosition(new Vector3f(-0.33f + 0.073f * i, 0.79f, -1.5f));
        }
        for (int i = 0; i < power.length; i++) {
            power[i] = new PlatformItem(0);
            power[i].setScale(0.05f);
            power[i].setRotation(-45, 0, 0);
            power[i].setPosition(new Vector3f(+0.25f + 0.15f * i, 0.82f, -1.5f));
        }
        playerCharacter = new PlayerCharacter(data.getPlayerSkinIndex(), 0.06f);
        platformItems = new ArrayList<>();
        bottleItems = new ArrayList<>();
        enemyItems = new ArrayList<>();
        final int maxPlanes = 10;
        PlatformItem platform;
        for (int cont = 0; cont < maxPlanes; cont++) {
            platform = new PlatformItem(-1f + (3f / maxPlanes) * cont);
            platformItems.add(platform);
        }

        platform = new PlatformItem(-0.5f, -1f);
        platformItems.add(platform);

        skyBox = new SkyBox();
        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 6.0f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        initSound();
    }

    public void initSound() throws Exception {
        soundManager.init();
        soundManager.setAttenuationModel(AL11.AL_LINEAR_DISTANCE);

        SoundBuffer buffBack = new SoundBuffer("/sound/bg.ogg");
        soundManager.addSoundBuffer(buffBack);
        SoundSource sourceBack = new SoundSource(true, false);
        sourceBack.setBuffer(buffBack.getBufferId());
        soundManager.addSoundSource("background", sourceBack);

        soundManager.setListener(new SoundListener(new Vector3f()));
        soundManager.setVolume("background", 0.75f);
        soundManager.playSoundSource("background");


        SoundBuffer buffCoin = new SoundBuffer("/sound/coin.ogg");
        soundManager.addSoundBuffer(buffCoin);
        SoundSource sourceCoin = new SoundSource(false, false);
        sourceCoin.setBuffer(buffCoin.getBufferId());
        soundManager.addSoundSource("coin", sourceCoin);
        soundManager.setVolume("coin", 1);

        SoundBuffer buffJump = new SoundBuffer("/sound/jump.ogg");
        soundManager.addSoundBuffer(buffJump);
        SoundSource sourceJump = new SoundSource(false, false);
        sourceJump.setBuffer(buffJump.getBufferId());
        soundManager.addSoundSource("jump", sourceJump);
        soundManager.setVolume("jump", 0.5f);

        SoundBuffer buffHit = new SoundBuffer("/sound/hit.ogg");
        soundManager.addSoundBuffer(buffHit);
        SoundSource sourceHit = new SoundSource(false, false);
        sourceHit.setBuffer(buffHit.getBufferId());
        soundManager.addSoundSource("hit", sourceHit);
        soundManager.setVolume("hit", 2f);
    }


    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void cleanup() {
        EnemyItem.clear();
        PlatformItem.clear();
        platformItems.forEach(GameItem::cleanup);
        enemyItems.forEach(GameItem::cleanup);
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
    public SkyBox getSkybox() {
        return skyBox;
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
