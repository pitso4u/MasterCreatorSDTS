package adminclerksdts;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimatedAbstractGeometricShapes extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        // Abstract geometric shapes
        Polygon polygon1 = new Polygon(300, 50, 500, 100, 600, 300, 200, 400);
        polygon1.setFill(Color.LIGHTYELLOW);
        Polygon polygon2 = new Polygon(100, 200, 700, 300, 600, 500, 150, 600);
        polygon2.setFill(Color.LIGHTPINK);

        // Vertical text
        Text sectionText = new Text("View Schedule");
        sectionText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #333;");
        sectionText.setRotate(-90);
        sectionText.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().addAll(polygon1, polygon2, sectionText);

        // Move polygons animation
        TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(5), polygon1);
        translateTransition1.setByX(200);
        translateTransition1.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition1.setAutoReverse(true);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.seconds(5), polygon2);
        translateTransition2.setByY(200);
        translateTransition2.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition2.setAutoReverse(true);

        translateTransition1.play();
        translateTransition2.play();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Animated Abstract Geometric Shapes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
