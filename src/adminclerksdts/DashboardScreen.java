package adminclerksdts;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DashboardScreen implements Initializable, ControlledScreen {

    private ScreenManager screenManager;
    @FXML
    private VBox menuBar;
    @FXML
    public BorderPane dashboardBorderpane;
    @FXML
    private StackPane contentArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
        setupGenericButtonHandler();
        
    }

    

    private void setupGenericButtonHandler() {
        menuBar.getChildren().stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .forEach(button -> {
                    button.setOnAction(event -> handleButtonAction(button.getText(), event));
                });
    }

    private void handleButtonAction(String buttonText, ActionEvent event) {
        System.out.println("Button clicked: " + buttonText);
        switch (buttonText) {
            case "Dashboard":
                loadScreenIntoCenter(AdminClerkSDTS.HomeScreenID);
                break;
            case "Learners":
                loadScreenIntoCenter(AdminClerkSDTS.ViewLearnerScreenID);
                break;
            case "Scheduling":
                loadScreenIntoCenter(AdminClerkSDTS.ViewScheduleScreenID);
                break;
            case "Tests Questions":
                loadScreenIntoCenter(AdminClerkSDTS.ViewQuestionsScreenID);
                break;
            case "Progress Tracking":
                loadScreenIntoCenter(AdminClerkSDTS.ViewProgressScreenID);
                break;
            case "Summary Report":
                loadScreenIntoCenter(AdminClerkSDTS.SummaryReportScreenID);
                break;
            case "Settings":
                loadScreenIntoCenter("SettingsScreen");
                break;
            case "Exit":
                Platform.exit();
                break;
            default:
                System.out.println("No handler for button: " + buttonText);
        }
    }

    private void loadHomeScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminclerksdts/HomeScreen.fxml"));
            Node homeScreen = loader.load();
            HomeScreenController controller = loader.getController();
            controller.setScreenParent(screenManager);
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(homeScreen);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading HomeScreen.fxml");
        }
    }

    public void loadScreenIntoCenter(String screenID) {
        Node screen = screenManager.getScreen(screenID);
        if (screen != null) {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(screen);
        } else {
            System.out.println("Screen not found: " + screenID);
        }
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        this.screenManager = screenParent;
        loadScreenIntoCenter(AdminClerkSDTS.HomeScreenID);
    }

    @Override
    public void runOnScreenChange() {
        
    }

    @Override
    public void cleanup() {
        // Cleanup code, if needed
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
public void handleDashboardButtonAction(ActionEvent event) {
    

    screenManager.setScreen(AdminClerkSDTS.HomeScreenID);
}


    @FXML
    private void handleViewLearnersButtonAction(ActionEvent event) {
    }

    @FXML
    private void handleClassSchedulingButtonAction(ActionEvent event) {
    }

    @FXML
    private void handleTestAdminButtonAction(ActionEvent event) {
    }

    @FXML
    private void handleProgressTrackingButtonAction(ActionEvent event) {
    }

    @FXML
    private void handleSummaryReportButtonAction(ActionEvent event) {
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent event) {
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
    }
}