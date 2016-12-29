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
package be.carrotcats.anxiety.control.input;

import be.carrotcats.anxiety.game.Anxiety;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * A default keyboard listener
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class KeyboardListener implements EventHandler<KeyEvent> {

    /**
     * The active game
     */
    private final Anxiety game;

    /**
     * Constructor for the keyboard listener
     *
     * @param game the active game
     */
    public KeyboardListener(Anxiety game) {
        this.game = game;
    }

    /**
     * Handles the key event
     * @param ke the key event
     */
    @Override
    public void handle(KeyEvent ke) {
        boolean keyPressed = ke.getEventType().equals(KeyEvent.KEY_PRESSED);
        KeyCode code = ke.getCode();
        float speed = game.getBasicPlayer().getSpeed();
        switch (code) {
            case LEFT:
                if (keyPressed) {
                    game.getBasicPlayer().setDx(-speed);
                } else {
                    game.getBasicPlayer().setDx(0);
                }
                break;
            case RIGHT:
                if (keyPressed) {
                    game.getBasicPlayer().setDx(speed);
                } else {
                    game.getBasicPlayer().setDx(0);
                }
                break;
            case UP:
                if (keyPressed) {
                    game.getBasicPlayer().setDy(-speed);
                } else {
                    game.getBasicPlayer().setDy(0);
                }
                break;
            case DOWN:
                if (keyPressed) {
                    game.getBasicPlayer().setDy(speed);
                } else {
                    game.getBasicPlayer().setDy(0);
                }
                break;
            case ESCAPE:
                if (keyPressed) {
                    if (game.isGameActive()) {
                        game.activateMenu();
                    } else {
                        game.activateGame();
                    }
                }
                break;
            case TAB:
                if (keyPressed && !game.isMetaDataActive()) {
                    game.setMetaDataActive(true);
                } else if (!keyPressed && game.isMetaDataActive()) {
                    game.setMetaDataActive(false);
                }
            default:
                //do nothing?
                break;
        }
    }

}
