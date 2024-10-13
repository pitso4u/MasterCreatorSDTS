/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminclerksdts;
import javafx.animation.FillTransition;
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

public class AnimatedCircularPatternsWithIndicator extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        // Circular patterns
        Circle circle1 = new Circle(50, Color.LIGHTGREEN);
        Circle circle2 = new Circle(100, Color.LIGHTCORAL);
        Circle circle3 = new Circle(150, Color.LIGHTCYAN);

        // Section indicator text
        Text sectionText = new Text("New Learner");
        sectionText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-fill: #333;");
        sectionText.setRotate(-90);
        sectionText.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().addAll(circle1, circle2, circle3, sectionText);

        // Fill color animation
        FillTransition fillTransition1 = new FillTransition(Duration.seconds(3), circle1, Color.LIGHTGREEN, Color.LIGHTBLUE);
        FillTransition fillTransition2 = new FillTransition(Duration.seconds(3), circle2, Color.LIGHTCORAL, Color.LIGHTYELLOW);
        FillTransition fillTransition3 = new FillTransition(Duration.seconds(3), circle3, Color.LIGHTCYAN, Color.LIGHTGREEN);

        fillTransition1.setCycleCount(FillTransition.INDEFINITE);
        fillTransition1.setAutoReverse(true);

        fillTransition2.setCycleCount(FillTransition.INDEFINITE);
        fillTransition2.setAutoReverse(true);

        fillTransition3.setCycleCount(FillTransition.INDEFINITE);
        fillTransition3.setAutoReverse(true);

        fillTransition1.play();
        fillTransition2.play();
        fillTransition3.play();

        // Move circles animation
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(5), circle1);
        translateTransition1.setByX(100);
        translateTransition1.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition1.setAutoReverse(true);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(5), circle2);
        translateTransition2.setByY(100);
        translateTransition2.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition2.setAutoReverse(true);

        TranslateTransition translateTransition3 = new TranslateTransition(Duration.seconds(5), circle3);
        translateTransition3.setByX(-100);
        translateTransition3.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition3.setAutoReverse(true);

        translateTransition1.play();
        translateTransition2.play();
        translateTransition3.play();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Animated Circular Patterns with Indicator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
