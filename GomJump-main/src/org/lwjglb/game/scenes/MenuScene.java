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
import org.lwjglb.engine.sound.SoundManager;
import org.lwjglb.engine.sound.SoundSource;
import org.lwjglb.game.GameData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;


public class MenuScene implements Scene {
    private enum State {
        Character,
        Main,
    }

    private Vector3f ambientLight;

    private PointLight pointLight;

    private final ButtonItem[] buttons = new ButtonItem[3];

    private SkyBox skyBox;

    private final Camera camera;

    private boolean start = false;

    private int selectedButton = 0, selectedSkin = 0;

    private State state = State.Main;

    private final SoundManager soundManager;

    private TitleItem title;

    PlayerCharacter playerCharacter;

    BottleItem bottleItem;

    private GameData data;

    public MenuScene() {
        camera = new Camera();
        soundManager = new SoundManager();
    }

    @Override
    public List<GameItem> getGameItems() {
        ArrayList<GameItem> list = new ArrayList<>();
        switch (state) {
            case Character -> list.add(playerCharacter);
            case Main -> {
                list.addAll(Arrays.asList(buttons));
                list.add(title);
            }
        }
        return list;
    }


    @Override
    public void update(float interval, Window window) {
        switch (state) {
            case Main -> {
                title.updateTitle(interval);
                for (int i = 0; i < buttons.length; i++) {
                    if (i == selectedButton)
                        buttons[i].setScale(0.125f);
                    else
                        buttons[i].setScale(0.10f);
                }
            }
            case Character -> {
                playerCharacter.setPosition(0, 0, -0.1f);
                playerCharacter.rotateY(interval / 6);
                updateSkin();
            }
        }
    }

    @Override
    public void input(Window window) {
        switch (state) {
            case Main -> {
                if (window.isKeyDown(GLFW_KEY_DOWN) || window.isKeyDown(GLFW_KEY_S)) {
                    if (selectedButton < buttons.length - 1) {
                        selectedButton++;
                    } else
                        selectedButton = 0;
                    soundManager.playSoundSource("button");
                }
                if (window.isKeyDown(GLFW_KEY_UP) || window.isKeyDown(GLFW_KEY_W)) {
                    if (selectedButton > 0) {
                        selectedButton--;
                    } else
                        selectedButton = buttons.length - 1;
                    soundManager.playSoundSource("button");
                }
                if (window.isKeyDown(GLFW_KEY_ENTER) || window.isKeyDown(GLFW_KEY_SPACE)) {
                    soundManager.playSoundSource("button");
                    switch (selectedButton) {
                        case 0 -> start = true;
                        case 1 -> state = State.Character;
                        case 2 -> stopExecution();
                    }
                }
            }
            case Character -> {
                if (window.isKeyDown(GLFW_KEY_ENTER) || window.isKeyDown(GLFW_KEY_SPACE)) {
                    state = State.Main;
                    data.setPlayerSkinIndex(selectedSkin);
                    soundManager.playSoundSource("button");
                }
                if (window.isKeyDown(GLFW_KEY_LEFT) || window.isKeyDown(GLFW_KEY_A)) {
                    if (selectedSkin > 0) {
                        selectedSkin--;
                        soundManager.playSoundSource("button");
                    }
                }
                if (window.isKeyDown(GLFW_KEY_RIGHT) || window.isKeyDown(GLFW_KEY_D)) {
                    if (selectedSkin < 1) {
                        selectedSkin++;
                        soundManager.playSoundSource("button");
                    }
                }
            }
        }
        if (window.isKeyDown(GLFW_KEY_ESCAPE))
            state = State.Main;
    }

    private void stopExecution() {
        start = true;
        data.setEndGame(true);
    }

    public void updateSkin() {
        playerCharacter.changeSkin(selectedSkin);
    }


    @Override
    public void init(Window window, GameData gameData) throws Exception {
        ButtonItem.init();
        data = gameData;
        skyBox = new SkyBox();
        ambientLight = new Vector3f(0.4f, 0.4f, 0.4f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 3);
        float lightIntensity = 6.0f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        playerCharacter = createSkin(selectedSkin);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new ButtonItem(i);
        }

        title = new TitleItem();
        initSound();
    }

    public void initSound() throws Exception {
        soundManager.init();
        soundManager.setAttenuationModel(AL11.AL_LINEAR_DISTANCE);

        SoundBuffer buffMenuSoundtrack = new SoundBuffer("/sound/menu_st.ogg");
        soundManager.addSoundBuffer(buffMenuSoundtrack);
        SoundSource sourceMenuSoundtrack = new SoundSource(true, false);
        sourceMenuSoundtrack.setBuffer(buffMenuSoundtrack.getBufferId());
        soundManager.addSoundSource("menuSoundtrack", sourceMenuSoundtrack);
        sourceMenuSoundtrack.setGain(0.2f);
        sourceMenuSoundtrack.play();

        SoundBuffer buffButton = new SoundBuffer("/sound/button.ogg");
        soundManager.addSoundBuffer(buffButton);
        SoundSource sourceButton = new SoundSource(false, false);
        sourceButton.setBuffer(buffButton.getBufferId());
        soundManager.addSoundSource("button", sourceButton);
        soundManager.setVolume("button", 0.5f);

    }


    static private BottleItem createCoin(int position) {
        BottleItem skin = new BottleItem(position);
        skin.setPosition(0, 0, -0.08f);
        skin.setScale(0.04f);
        return skin;
    }

    static private PlayerCharacter createSkin(int position) {
        PlayerCharacter skin = new PlayerCharacter(position, 0.6f);
        skin.setPosition(0 + position * 0.5f, -0.015f, -0.08f);
        skin.setScale(0.01f);
        return skin;
    }


    @Override
    public boolean isRunning() {
        return !start;
    }

    @Override
    public void cleanup() {
        Arrays.asList(buttons).forEach(GameItem::cleanup);
        soundManager.cleanup();
        ButtonItem.clear();
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
        return /*state == State.HighScores ? highScoresHUD :*/ null;
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
