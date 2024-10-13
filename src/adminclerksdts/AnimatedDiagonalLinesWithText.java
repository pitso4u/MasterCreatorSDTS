/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminclerksdts;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimatedDiagonalLinesWithText extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        // Diagonal lines pattern
        for (int i = 0; i < 100; i++) {
            Rectangle line = new Rectangle(800, 5, Color.GRAY);
            line.setRotate(i * 10);
            root.getChildren().add(line);
        }

        // Large vertical text
        Text sectionText = new Text("View Schedule");
        sectionText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-fill: #333;");
        sectionText.setRotate(-90);
        sectionText.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().add(sectionText);

        // Rotate lines animation
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(10), root);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.play();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Animated Diagonal Lines with Text");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

