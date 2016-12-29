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
package be.carrotcats.anxiety.control.factory.scenes;

import be.carrotcats.anxiety.control.util.scene.GameOverSceneBuilder;
import be.carrotcats.anxiety.control.util.scene.InformationScreenBuilder;
import be.carrotcats.anxiety.control.util.scene.MenuSceneBuilder;
import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.view.AbstractGame;
import java.io.IOException;
import javafx.scene.Scene;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class SceneFactory {

    /**
     * Generates the menu screen
     *
     * @param game the active game
     * @return a scene
     */
    public static Scene createMenuScreen(AbstractGame game) {
        return MenuSceneBuilder.createMenuScene(game);
    }

    /**
     * Generates the info screen
     *
     * @param game the active game
     * @return a scene
     * @throws IOException
     */
    public static Scene createInformationScreen(AbstractGame game) throws IOException {
        return InformationScreenBuilder.createInformationScreen(game);
    }

    /**
     * Generates the game over screen
     *
     * @param game the active game
     * @return a scene
     */
    public static Scene createGameOverScreen(AbstractGame game) {
        return GameOverSceneBuilder.createGameOverScreen((Anxiety) game);
    }
}
