/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminclerksdts;

import javafx.animation.FillTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimatedVerticalGradientWithCircles extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        // Background with vertical gradient
        root.setStyle("-fx-background: linear-gradient(to right, lightblue, white);");

        // Vertical Text
        Text sectionText = new Text("New Learner");
        sectionText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #333;");
        sectionText.setRotate(-90);
        sectionText.setTextAlignment(TextAlignment.CENTER);

        // Circles decoration
        Circle circle1 = new Circle(100, Color.LIGHTGREEN);
        Circle circle2 = new Circle(150, Color.LIGHTCORAL);
        Circle circle3 = new Circle(200, Color.LIGHTCYAN);

        // Add circles to root
        root.getChildren().addAll(circle1, circle2, circle3, sectionText);

        // Animate circles
        ScaleTransition scaleTransition1 = new ScaleTransition(Duration.seconds(3), circle1);
        scaleTransition1.setByX(0.5);
        scaleTransition1.setByY(0.5);
        scaleTransition1.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition1.setAutoReverse(true);

        ScaleTransition scaleTransition2 = new ScaleTransition(Duration.seconds(3), circle2);
        scaleTransition2.setByX(0.5);
        scaleTransition2.setByY(0.5);
        scaleTransition2.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition2.setAutoReverse(true);

        ScaleTransition scaleTransition3 = new ScaleTransition(Duration.seconds(3), circle3);
        scaleTransition3.setByX(0.5);
        scaleTransition3.setByY(0.5);
        scaleTransition3.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition3.setAutoReverse(true);

        scaleTransition1.play();
        scaleTransition2.play();
        scaleTransition3.play();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Animated Vertical Gradient with Circles");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

