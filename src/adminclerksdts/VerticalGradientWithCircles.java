package adminclerksdts;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class VerticalGradientWithCircles extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        // Background with vertical gradient
        BackgroundFill fill = new BackgroundFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.REPEAT, new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.WHITE)), CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(fill);
        root.setBackground(background);

        // Vertical Text
        Text sectionText = new Text("New Learner");
        sectionText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #333;");
        sectionText.setRotate(-90);
        sectionText.setTextAlignment(TextAlignment.CENTER);

        // Circles decoration
        Circle circle1 = new Circle(100, Color.LIGHTGREEN);
        Circle circle2 = new Circle(150, Color.LIGHTCORAL);
        Circle circle3 = new Circle(200, Color.LIGHTCYAN);

        root.getChildren().addAll(circle1, circle2, circle3, sectionText);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Vertical Gradient with Circles");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
