package adminclerksdts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author sam
 */
public class HomeScreenController implements Initializable, ControlledScreen {
 private ScreenManager screenManager;

    @FXML
    private Text totalLearnersLabel;
    @FXML
    private Text activePackagesLabel;
    @FXML
    private Text inactivePackagesLabel; // Added field
    @FXML
    private Text scheduledSessionsLabel;
    @FXML
    private Text testsCompletedLabel;
    @FXML
    private Text averageQuizScoreLabel; // Added field
    @FXML
    private Text averageSessionDurationLabel; // Added field
    @FXML
    private PieChart testCompletionChart;
    @FXML
    private BarChart<String, Number> monthlyProgresBarChart;
    @FXML
    public BorderPane dashboardBorderpane;
    @FXML
    private StackPane contentArea;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
    }

    public void initialize() {
        updateStatistics();
        initializeCharts();
    }

    private void updateStatistics() {
        int totalLearners = DatabaseConnector.getTotalLearners();
        int activePackages = DatabaseConnector.getActivePackagesCount();
        int inactivePackages = DatabaseConnector.getInactivePackagesCount(); // Added
        int scheduledSessions = DatabaseConnector.getScheduledSessionsCount();
        int testsCompleted = DatabaseConnector.getTestsCompletedCount();
        double averageQuizScore = DatabaseConnector.getAverageQuizScore(); // Added
        double averageSessionDuration = DatabaseConnector.getAverageSessionDuration(); // Added

        totalLearnersLabel.setText("Total Learners: " + totalLearners);
        activePackagesLabel.setText("Active Packages: " + activePackages);
        inactivePackagesLabel.setText("Inactive Packages: " + inactivePackages); // Added
        scheduledSessionsLabel.setText("Scheduled Sessions Today: " + scheduledSessions);
        testsCompletedLabel.setText("Tests Completed Today: " + testsCompleted);
        averageQuizScoreLabel.setText("Average Quiz Score: " + String.format("%.2f", averageQuizScore)); // Added
        averageSessionDurationLabel.setText("Average Session Duration: " + String.format("%.2f", averageSessionDuration) + " mins"); // Added
    }

    private void initializeCharts() {
        // Initialize the PieChart
        PieChart.Data completedTestsData = new PieChart.Data("Completed", DatabaseConnector.getTestsCompletedCount());
        PieChart.Data remainingTestsData = new PieChart.Data("Remaining", DatabaseConnector.getRemainingTestsCount());
        testCompletionChart.getData().addAll(completedTestsData, remainingTestsData);

        // Initialize the BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Progress");

        // Example data - replace with real data as needed
        series.getData().add(new XYChart.Data<>("January", 30));
        series.getData().add(new XYChart.Data<>("February", 40));
        series.getData().add(new XYChart.Data<>("March", 50));
        // Add more months as needed

        monthlyProgresBarChart.getData().add(series);
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        this.screenManager = screenParent;
    }

    @Override
    public void runOnScreenChange() {
        updateStatistics();
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
}