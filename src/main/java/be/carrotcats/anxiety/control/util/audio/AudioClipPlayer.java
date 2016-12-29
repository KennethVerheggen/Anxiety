/*
 * Copyright 2016 Kenneth Verheggen <kenneth.verheggen@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.carrotcats.anxiety.control.util.audio;

import java.util.Random;
import javafx.concurrent.Task;
import javafx.scene.media.AudioClip;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class AudioClipPlayer extends Task {

    public static final String EXPLODE = "/audio/explode.wav";
    //public static final String SHOT = "/audio/shot.wav";
    public static final String SPARK = "/audio/spark.wav";
    //public static final String SPAWN = "/audio/spawn.wav";
    public static final String ENEMY_DEAD_1 = "/audio/enemy1.wav";
    public static final String ENEMY_DEAD_2 = "/audio/enemy2.wav";
    public static final String ENEMY_DEAD_3 = "/audio/enemy3.wav";
    public static final String ENEMY_DEAD_4 = "/audio/enemy4.wav";
    public static final String ENEMY_DEAD_5 = "/audio/enemy5.wav";
    public static final String ENEMY_DEAD_6 = "/audio/enemy6.wav";
    public static final String ENEMY_DEAD_7 = "/audio/enemy7.wav";

    private static final String[] ENEMY_DEATH = new String[]{ENEMY_DEAD_1, ENEMY_DEAD_2, ENEMY_DEAD_3, ENEMY_DEAD_4, ENEMY_DEAD_5, ENEMY_DEAD_6, ENEMY_DEAD_7};

    private static final Random RANDOM = new Random();

    /**
     * This running thread
     */
    private Thread thread;
    /**
     * boolean indicating if the sound has started
     */
    private boolean started = false;
    /**
     * The audio clip
     */
    private AudioClip audio;
    /**
     * The currently played path
     */
    private final String path;

    public static String getRandomEnemyDeadSound() {
        return ENEMY_DEATH[Math.max(0, RANDOM.nextInt(ENEMY_DEATH.length) - 1)];
    }

    public AudioClipPlayer(String path) {
        this.path = path;
    }

    @Override
    protected Object call() throws Exception {
        audio = new AudioClip(getClass().getResource(path).toExternalForm());
        audio.setVolume(0.5f);
        audio.setCycleCount(1);
        audio.play();
        return null;
    }

    public void start() {
        if (!started) {
            thread = new Thread(this);
            thread.start();
            started = true;
        }
    }

    public void stop() {
        if (started && thread != null) {
            thread.interrupt();
            if (audio != null) {
                audio.stop();
            }
        }
    }

}
