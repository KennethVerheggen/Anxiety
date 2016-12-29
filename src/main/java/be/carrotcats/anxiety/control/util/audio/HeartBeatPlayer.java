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

import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.model.entity.player.BasicPlayer;
import javafx.scene.media.AudioClip;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * This class generates a heartbeat in relation to the amount of enemies on
 * screen
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class HeartBeatPlayer implements Runnable {

    /**
     * The heartbeat audio path
     */
    public static final String HEARTBEAT = "/audio/heartbeat.wav";
    /**
     * The heartbeat thread
     */
    private Thread thread;
    /**
     * Boolean indicating whether the game has started
     */
    private boolean started = false;
    /**
     * The audioclip object
     */
    private AudioClip audio;
    /**
     * The maximal amount of rest
     */
    private final int SLEEP = 1000; //rest = 60 beats per minute
    /**
     * The minimal amount of rest
     */
    private final int MIN_SLEEP = 330;//this is 180 beats per minute...
    /**
     * The anxiety levels
     */
    private int anxiety;
    /**
     * The player
     */
    private final BasicPlayer player;
    /**
     * boolean indicating whether a stop has been requested
     */
    private boolean stopRequested = false;
    /**
     * The length of the sleep related to the heartrates
     */
    private int anxietySleep;
    /**
     * Object that tracks the statistics on the heart rates
     */
    private final DescriptiveStatistics heartRateStats = new DescriptiveStatistics();

    /**
     * Constructor for a heartbeat thread
     *
     * @param anxiety the anxiety game
     */
    public HeartBeatPlayer(Anxiety anxiety) {
        this.player = anxiety.getBasicPlayer();
    }

    public void setAnxiety(int anxiety) {
        this.anxiety = anxiety;
    }

    @Override
    public void run() {
        while (!stopRequested && !player.isDead()) {
            try {
                if (!player.isDead()) {
                    anxietySleep = SLEEP - (10 * anxiety);
                    heartRateStats.addValue(anxietySleep);
                    if (anxietySleep < MIN_SLEEP) {
                        anxietySleep = MIN_SLEEP;
                    }
                    Thread.sleep(anxietySleep);
                    audio = new AudioClip(getClass().getResource(HEARTBEAT).toExternalForm());
                    audio.setVolume(0.5f * anxietySleep / SLEEP);
                    audio.setCycleCount(1);
                    audio.play();
                    //get average bpm?

                } else if (player.isDead()) {
                    break;
                }
            } catch (InterruptedException ex) {
                //ignore, is normal in most situations
            }
        }
    }

    /**
     * Starts a new heartbeat
     */
    public void start() {
        if (!started) {
            thread = new Thread(this);
            thread.start();
            started = true;
        }
    }

    /**
     * Stops this running heartbeat
     */
    public void stop() {
        if (started && thread != null) {
            stopRequested = true;
            thread.interrupt();
            if (audio != null) {
                audio.stop();
            }
        }
    }

    public int getBPM() {
        return (60000 / anxietySleep);
    }

    public double getAverageBPM() {
        return Math.floor(60000 / heartRateStats.getMean());
    }

    public double getPeakBPM() {
        return Math.floor(60000 / heartRateStats.getMin());
    }

}
