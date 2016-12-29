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
package be.carrotcats.anxiety.model.entity.player;

import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.model.entity.Ball;
import be.carrotcats.anxiety.model.entity.obstacle.PolygonObstacle;
import be.carrotcats.anxiety.view.AbstractGame;
import javafx.animation.FadeTransition;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class LightShot extends Ball {

    private int distanceTravelled = 0;

    /**
     * Constructor for a light shot projectile
     *
     * @param world the active game world
     */
    public LightShot(AbstractGame world) {
        super(world);
        innerColor = Color.WHITE;
        myCircle = new Circle(3);
        myCircle.setCenterX(0);
        myCircle.setCenterY(0);
        myCircle.setFill(innerColor);
        myShape = myCircle;
        speed = 10;
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(5);
        myShape.setEffect(blur);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), myShape);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.playFromStart();
        //   new AudioClipPlayer(AudioClipPlayer.HEARTBEAT).start();
    }

    @Override
    public void update() {
        setY(getY() - speed);
        distanceTravelled += speed;
    }

    @Override
    public Shape getMyShape() {
        return myShape;
    }

    @Override
    public void handleCollision(GameEntity otherObject) {
        if (otherObject instanceof PolygonObstacle) {
            double leftsideHit = getMyShape().getLayoutX() - otherObject.getMyShape().getLayoutX();
            double upsideHit = getMyShape().getLayoutY() - otherObject.getMyShape().getLayoutY();
            double temp = 2 * leftsideHit;
            if (upsideHit > 0) {
                upsideHit *= -1;
            }
            ((PolygonObstacle) otherObject).increaseAngle(temp * upsideHit);
        }
    }

    public boolean isDestroyed() {
        return distanceTravelled > Anxiety.APP_HEIGHT;
    }

}
