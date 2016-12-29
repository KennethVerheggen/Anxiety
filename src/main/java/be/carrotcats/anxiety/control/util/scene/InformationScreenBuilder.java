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

import be.carrotcats.anxiety.view.AbstractGame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class InformationScreenBuilder {

    /**
     * Creates an information screen
     * @param game the game 
     * @return a new scene that has the information layout
     * @throws IOException 
     */
    public static Scene createInformationScreen(AbstractGame game) throws IOException {
        Text t = new Text("Introduction");
        t.setFont(Font.font("Copperplate", 22));
        t.setEffect(getNormalButtonEffect());
        t.setFill(Color.WHITE);


        Label story = new Label(readFromFile("/info/introduction.txt"));
        story.setTextFill(Color.WHITE);
        story.setFont(Font.font("Copperplate", 18));
        story.setEffect(getNormalButtonEffect());
        story.setWrapText(true);


        Text t2 = new Text("How To Play");
        t2.setFont(Font.font("Copperplate", 22));
        t2.setEffect(getNormalButtonEffect());
        t2.setFill(Color.WHITE);

        Label howToPlay = new Label(readFromFile("/info/how_to_play.txt"));
        howToPlay.setTextFill(Color.WHITE);
        howToPlay.setFont(Font.font("Copperplate", 18));
        howToPlay.setEffect(getNormalButtonEffect());
        howToPlay.setWrapText(true);

        Effect highlightedButtonEffect = getButtonHighlightEffect();
        Effect normalButtonEffect = getNormalButtonEffect();
        Button button1 = createBackButton(game, highlightedButtonEffect, normalButtonEffect);

        VBox layout1 = new VBox(20);
        layout1.setBackground(new Background(new BackgroundFill(AbstractGame.getAppForeGround(), null, null)));
        layout1.setAlignment(Pos.TOP_CENTER);
        layout1.getChildren().addAll(t,  story,  t2, howToPlay,  button1);
        return new Scene(layout1, APP_WIDTH, APP_HEIGHT);
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
        button.setStyle("-fx-background-color: transparent;-fx-font: 22 arial; -fx-text-fill: white;");
    }

    private static Button createBackButton(AbstractGame game, Effect normalEffect, Effect highlightEffect) {
        Button button = new Button("Back");
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
            game.activateMenu();
        });
        return button;
    }

    private static String readFromFile(String path) throws IOException {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(InformationScreenBuilder.class.getResourceAsStream(path),"UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        }
        return text.toString();
    }

}
