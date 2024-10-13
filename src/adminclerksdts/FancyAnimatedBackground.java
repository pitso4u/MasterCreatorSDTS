package adminclerksdts;

import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FancyAnimatedBackground extends Application {

    private Stop[] stops;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        // Initial stops for the gradient
        stops = new Stop[]{
            new Stop(0, Color.PALEGREEN),
            new Stop(0.33, Color.LIGHTBLUE),
            new Stop(0.66, Color.LAVENDER),
            new Stop(1, Color.PEACHPUFF)
        };

        // Linear gradient background
        Rectangle background = new Rectangle(800, 600);
        background.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, stops));
        root.getChildren().add(background);

        // Fancy vertical text
        Text sectionText = new Text("View Schedule");
        sectionText.setFont(Font.font("Verdana", 48));
        sectionText.setFill(Color.WHITE);
        sectionText.setEffect(new DropShadow(10, Color.BLACK));
        sectionText.getTransforms().add(new Rotate(-90, 0, 0));

        // Text animation
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), sectionText);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(Timeline.INDEFINITE);
        root.getChildren().add(sectionText);

        // Animate gradient colors using AnimationTimer
        AnimationTimer gradientAnimation = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 50_000_000) { // Update every 50ms
                    updateGradientColors();
                    background.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, stops));
                    lastUpdate = now;
                }
            }
        };

        // Start animations
        scaleTransition.play();
        gradientAnimation.start();

        Scene scene = new Scene(root, 800, 600);

        // Add interactivity
        scene.setOnMouseClicked(event -> {
            if (scaleTransition.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                scaleTransition.pause();
                gradientAnimation.stop();
            } else {
                scaleTransition.play();
                gradientAnimation.start();
            }
        });

        primaryStage.setTitle("Fancy Animated Background");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateGradientColors() {
        for (int i = 0; i < stops.length; i++) {
            Color currentColor = stops[i].getColor();
            Color newColor = currentColor.interpolate(Color.color(Math.random(), Math.random(), Math.random()), 0.02);
            stops[i] = new Stop(stops[i].getOffset(), newColor);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
