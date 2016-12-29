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
package be.carrotcats.anxiety.control.util;

import java.util.Random;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class ShapeLibrary {

    public static final double[] HEXAGON = new double[]{-50, 30, 0, 60, 50, 30, 50, -30, 0, -60, -50, -30};
    public static final double[] SQUARE = new double[]{-50, 30, 50, 30, 50, -30, -50, -30};
    public static final double[] TRIANGLE = new double[]{-50, -30, 0, 60, 50, -30};
    public static final double[] PLUS = new double[]{10, 10, 10, 60, -10, 60, -10, 10, -60, 10, -60, -10, -10, -10, -10, -60, 10, -60, 10, -10, 60, -10, 60, 10};
    public static final double[] STAR = new double[]{10, 10, 0, 60, -10, 10, -60, 0, -10, -10, 0, -60, 10, -10, 60, 0};
    public static final double[] BOWTIE = new double[]{20, 30, -25, 40, -15, 50, 0, 40, 15, 30, 25, 20, 20, 0, 5, -20, 20, -30, 25, -40, 15, -50, 0, -40, -15, -30, -25, -20, -20, 0, -5};

    private static final Random RANDOM = new Random();

    private static final double[][] shapes = new double[][]{
        TRIANGLE, TRIANGLE, TRIANGLE, TRIANGLE, TRIANGLE, TRIANGLE, TRIANGLE, TRIANGLE,
        SQUARE, SQUARE, SQUARE, SQUARE, SQUARE, SQUARE,
        HEXAGON, HEXAGON, HEXAGON, HEXAGON, HEXAGON,
        PLUS, PLUS, PLUS, PLUS,
        STAR, STAR, STAR,
 //       BOWTIE, BOWTIE
    };

    public static double[] getRandomShape() {
        return shapes[RANDOM.nextInt(shapes.length)];
    }

}
