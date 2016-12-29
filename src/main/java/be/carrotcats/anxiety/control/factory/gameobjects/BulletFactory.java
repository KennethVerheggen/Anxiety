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
package be.carrotcats.anxiety.control.factory.gameobjects;

import be.carrotcats.anxiety.control.input.MouseListener;
import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.model.entity.player.BasicPlayer;
import be.carrotcats.anxiety.model.entity.player.LightShot;
import javafx.scene.shape.Circle;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class BulletFactory {

    /**
     * Creates a bullet 
     * @param game the active game
     * @param listener the mouse listener
     * @param tempRadius the current radius of the player
     */
    public static void createLightShot(Anxiety game, MouseListener listener, double tempRadius) {
        //init game vars
        BasicPlayer basicPlayer = game.getBasicPlayer();
        //create a new shot
        LightShot shot = new LightShot(game);
        double radius = ((Circle) basicPlayer.getMyShape()).getRadius();
        //chargeup time = 100% in 1000ms, so 1 per 10ms...
        long chargeTime = listener.getChargeTime();
        double maxChargeTime = 1000;
        if (chargeTime > maxChargeTime) {
            chargeTime = (long) maxChargeTime;
        }

        double bulletRadius = 4 * basicPlayer.getRadius() * (chargeTime / maxChargeTime);
        radius -= bulletRadius;

        basicPlayer.setRadius(radius);
        shot.setX(basicPlayer.getX());
        shot.setY(basicPlayer.getY());
        shot.setRadius(bulletRadius);

        //add to render?
        game.addLightShot(shot);
        //reset charge timer?
        listener.setChargeTime(0);

    }

}
