/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminclerksdts;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class CircularPatternsWithIndicator extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        // Circular patterns
        for (int i = 0; i < 10; i++) {
            Circle circle = new Circle(50 + i * 20, Color.hsb(i * 36, 1, 1, 0.5));
            circle.setTranslateX(i * 20);
            circle.setTranslateY(i * 20);
            root.getChildren().add(circle);
        }

        // Section indicator text
        Text sectionText = new Text("New Learner");
        sectionText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-fill: #333;");
        sectionText.setRotate(-90);
        sectionText.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().add(sectionText);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Circular Patterns with Indicator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
