package org.lwjglb.engine.sound;

import org.joml.Vector3f;
import static org.lwjgl.openal.AL10.*;

public class SoundListener {

    public SoundListener(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public void setPosition(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

}
