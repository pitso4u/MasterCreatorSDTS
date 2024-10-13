/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminclerksdts;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LinearGradientBackground extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        // Linear gradient background
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, 
                javafx.scene.paint.CycleMethod.REPEAT, new Stop(0, Color.PALEGREEN), new Stop(1, Color.WHITE));
        Rectangle background = new Rectangle(800, 600, gradient);
        root.getChildren().add(background);

        // Vertical text
        Text sectionText = new Text("View Schedule");
        sectionText.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-fill: #333;");
        sectionText.setRotate(-90);
        sectionText.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().add(sectionText);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Linear Gradient Background");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
