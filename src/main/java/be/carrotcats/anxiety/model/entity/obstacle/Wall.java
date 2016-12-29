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
package be.carrotcats.anxiety.model.entity.obstacle;

import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.view.AbstractGame;
import java.util.LinkedList;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class Wall extends PolygonObstacle {

    /**
     * Constructor for a wall obstacle
     * @param world the active game world
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width of the wall
     * @param height the height of the wall
     * @param angle the angle of the wall
     */
    public Wall(AbstractGame world, int x, int y, int width, int height, double angle) {
        super(world);
        objectProperties.setProperty("solid", "true");
        myShape = new Rectangle(x, y, width, height);
        myShape.setFill(Color.BROWN);
        myShape.getTransforms().add(new Rotate(angle, 0, 0));
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(5);
        myShape.setEffect(blur);
    }

    @Override
    public void update() {
        setY(getY() + getSpeed());
    }

    @Override
    public void handleCollision(GameEntity otherObject) {

    }

    @Override
    public void checkCollisions(LinkedList<GameEntity> objectsToRender) {

    }

}
