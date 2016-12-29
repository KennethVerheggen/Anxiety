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

import be.carrotcats.anxiety.control.util.ShapeLibrary;
import be.carrotcats.anxiety.model.GameEntity;
import be.carrotcats.anxiety.view.AbstractGame;
import java.util.LinkedList;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class PolygonObstacle extends GameEntity {

    public float speed = 5;
    Rotate rotate = new Rotate();
    Polygon myPolygon;
    double angle;
    private double angleIncrement;

    /**
     * Constructor for blade obstacles
     *
     * @param world the active game world
     */
    public PolygonObstacle(AbstractGame world) {
        super(world);
        double[] points = ShapeLibrary.getRandomShape();
        init(1.0, points);
    }

    /**
     * Constructor for blade obstacles
     *
     * @param world the active game world
     * @param points the points determining the shape
     *
     */
    public PolygonObstacle(AbstractGame world, double[] points) {
        super(world);
        init(1.0, points);
    }

    /**
     * Constructor for blade obstacles
     *
     * @param world the active game world
     * @param scale the scale of the blade
     */
    public PolygonObstacle(AbstractGame world, double scale) {
        super(world);
        double[] points = ShapeLibrary.getRandomShape();
        init(scale, points);
    }

    /**
     * Constructor for blade obstacles
     *
     * @param world the active game world
     * @param points the points determining the shape
     * @param scale the scale of the blade
     */
    public PolygonObstacle(AbstractGame world, double scale, double points[]) {
        super(world);
        init(scale, points);
    }

    private void init(double scale, double[] points) {
        innerColor = Color.WHITE;
        double[] temp = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            temp[i] = points[i] * scale;
        }
        myPolygon = new Polygon(temp);
        myPolygon.setFill(Color.BROWN);
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(5);
        myPolygon.setEffect(blur);
        myShape = myPolygon;
        myPolygon.getTransforms().add(rotate);
    }

    public PolygonObstacle applyRandomRotation() {
        angle = Math.random() * 360;
        rotate.setAngle(angle);
        return this;
    }

    @Override
    public void update() {
        setX(getX() + getDx());
        setY(getY() + getDy() + speed);
        angle += angleIncrement;
        if (angle != 0) {
            rotate.setAngle(angle);
        }
        if (angleIncrement < 0) {
            angleIncrement--;
        } else if (angleIncrement < 0) {
            angleIncrement++;
        }
        if (Math.abs(angleIncrement) < 1) {
            angle = 0;
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public Shape getMyShape() {
        return myShape;
    }

    @Override
    public void handleCollision(GameEntity otherObject) {

    }

    @Override
    public void checkCollisions(LinkedList<GameEntity> objectsToRender) {

    }

    public void increaseAngle(double temp) {
        angleIncrement += temp;
    }
}
