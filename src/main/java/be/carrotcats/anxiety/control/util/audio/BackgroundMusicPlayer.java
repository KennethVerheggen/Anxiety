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

import be.carrotcats.anxiety.view.AbstractGame;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class BackgroundMusicPlayer {

    public static final String BACKGROUND_MUSIC = "/audio/bensound-relaxing.wav";
    public static final String BACKGROUND_RAIN = "/audio/rain.wav";

    /**
     * media object
     */
    private Media media;
    /**
     * Media player
     */
    private MediaPlayer mediaPlayer;

    /**
     * Constructor for a bgm player
     *
     * @param game the active game
     * @param path the relative path to the sound file. These are static paths
     */
    public BackgroundMusicPlayer(AbstractGame game, String path) {
        media = new Media(getClass().getResource(path).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        MediaView mediaView = new MediaView(mediaPlayer);
        game.getRoot().getChildren().add(mediaView);
    }

    public void reset() {
        stop();
        start();
    }

    public void start() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void pause() {
        mediaPlayer.pause();
    }

}
