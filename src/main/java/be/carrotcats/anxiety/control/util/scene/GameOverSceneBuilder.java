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
package be.carrotcats.anxiety.control.util.scene;

import be.carrotcats.anxiety.control.factory.gameobjects.SectionFactory;
import be.carrotcats.anxiety.control.util.ScoreCalculator;
import be.carrotcats.anxiety.game.Anxiety;
import be.carrotcats.anxiety.view.AbstractGame;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static be.carrotcats.anxiety.view.AbstractGame.APP_WIDTH;
import static be.carrotcats.anxiety.view.AbstractGame.APP_HEIGHT;

/**
 *
 * @author Kenneth Verheggen <kenneth.verheggen@gmail.com>
 */
public class GameOverSceneBuilder {

    /**
     * Creates a game over screen
     * @param game the active game
     * @return the standard game over screen
     */
    public static Scene createGameOverScreen(Anxiety game) {

        Text spacing = new Text(" ");
        spacing.setFont(Font.font("Copperplate", 50));
        spacing.setFill(Color.WHITE);
        spacing.setEffect(getTextHighlightEffect());

        Text t = new Text("G a m e  O v e r");
        t.setFont(Font.font("Copperplate", 60));
        t.setFill(Color.WHITE);
        t.setEffect(getTextHighlightEffect());

        Text spacing2 = new Text(" ");
        spacing.setFont(Font.font("Copperplate", 50));
        spacing.setFill(Color.WHITE);
        spacing.setEffect(getTextHighlightEffect());

        StringBuilder metadata = new StringBuilder();
        metadata.append("You've finished ").append(SectionFactory.getSectionsPassed()).append(" sections.\n")
                .append("Your average heartrate was ").append(game.getHbp().getAverageBPM())
                .append(" with a peak of ").append(game.getHbp().getPeakBPM())
                .append(" beats per minute.").append("\n")
                .append("Scores are calculated based on how calm you were during gameplay, while being able to dodge and destroy dark objects.")
                .append(" Your score was ").append(ScoreCalculator.getScore()).append(".");

        Text metaData = new Text(metadata.toString());
        metaData.setWrappingWidth(APP_WIDTH-30);
        metaData.setFont(Font.font("Copperplate", 18));
        metaData.setFill(Color.WHITE);
        metaData.setEffect(getTextHighlightEffect());

        Text spacing3 = new Text(" ");
        spacing.setFont(Font.font("Copperplate", 50));
        spacing.setFill(Color.WHITE);
        spacing.setEffect(getTextHighlightEffect());

        Effect highlightedButtonEffect = getButtonHighlightEffect();
        Effect normalButtonEffect = getNormalButtonEffect();

        Button button1 = createGameRestartButton(game, highlightedButtonEffect, normalButtonEffect);
        Button button2 = createExitButton(game, highlightedButtonEffect, normalButtonEffect);

        VBox layout1 = new VBox(20);
        layout1.setBackground(new Background(new BackgroundFill(AbstractGame.getAppForeGround(), null, null)));
        layout1.setAlignment(Pos.TOP_CENTER);
        layout1.getChildren().addAll(spacing, t, spacing2,metaData,spacing3, button1, button2);
        return new Scene(layout1, APP_WIDTH, APP_HEIGHT);
    }

    private static Effect getTextHighlightEffect() {
        Glow effect1 = new Glow(1.0);
        GaussianBlur effect2 = new GaussianBlur(3.0);
        effect2.setInput(effect1);
        return effect2;
    }

    private static Effect getButtonHighlightEffect() {
        Glow effect1 = new Glow(1.0);
        GaussianBlur effect2 = new GaussianBlur(2.0);
        effect2.setInput(effect1);
        return effect2;
    }

    private static Effect getNormalButtonEffect() {
        return new Glow(1.0);
    }

    private static void applyButtonStyle(Button button) {
        button.setStyle("-fx-background-color: transparent;-fx-font: 38 arial; -fx-text-fill: white;");
    }

    private static Button createGameRestartButton(AbstractGame game, Effect normalEffect, Effect highlightEffect) {
        Button button = new Button("Restart");
        button.setEffect(normalEffect);
        //Adding the effect when the mouse cursor is on
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setEffect(highlightEffect);
        });
        //Removing the effect when the mouse cursor is off
        button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button.setEffect(normalEffect);
        });
        applyButtonStyle(button);
        button.setOnAction((ActionEvent event) -> {
            game.reset();
            game.activateGame();
        });
        return button;
    }

    private static Button createExitButton(AbstractGame game, Effect normalEffect, Effect highlightEffect) {
        Button button = new Button("Exit");
        button.setEffect(normalEffect);
        //Adding the effect when the mouse cursor is on
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setEffect(highlightEffect);
        });
        //Removing the effect when the mouse cursor is off
        button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button.setEffect(normalEffect);
        });
        applyButtonStyle(button);
        button.setOnAction(e -> {
            try {
                game.stop();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
        });
        return button;
    }

}
