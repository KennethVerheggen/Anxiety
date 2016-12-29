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

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class ScoreCalculator {

    private static final int SECTION_BONUS = 2000;
    private static final int ENEMY_BONUS = 500;

    private static int score;
    private static int enemyCounter;

    public static int getScore() {
        return score;
    }

    public static void finishSection(int bpm) {
        score += (SECTION_BONUS / bpm);
    }

    public static void killEnemy(int bpm) {
        enemyCounter++;
        score += (ENEMY_BONUS / bpm);
    }

    public static int getEnemyCount() {
        return enemyCounter;
    }

    public static void reset() {
        score = 0;
        enemyCounter=0;
    }
}
