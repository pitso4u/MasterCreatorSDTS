/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts.progress;

import adminclerksdts.ControlledScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;

public class NewProgressController implements Initializable, ControlledScreen {

    @FXML
    private TextField learnerIDField;

    @FXML
    private TextField instructorIDField;

    @FXML
    private TextField quizScoreField;

    @FXML
    private DatePicker timestampPicker;

    @FXML
    private TextField instructorNotesField;

    private ScreenManager screenManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: Initialization logic, if needed
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        this.screenManager = screenParent;
    }

    @Override
    public void runOnScreenChange() {
        // Code to run when the screen is changed, if needed
    }

    @Override
    public void cleanup() {
        // Cleanup code, if needed
    }

    @FXML
    private void handleSaveButtonAction() {
        // Perform validation before saving
        if (validateInput()) {
            // Save progress data to the database
            saveProgressData();

            // Display success message
            showAlert(Alert.AlertType.INFORMATION, "Progress Saved", "Progress information has been successfully saved.");

            // Navigate back to the previous screen or perform other actions as needed
            screenManager.goBack();
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        // Prompt user for confirmation before canceling
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Cancel Progress Entry");
        confirmationAlert.setContentText("Are you sure you want to cancel progress entry? Any unsaved data will be lost.");

        if (confirmationAlert.showAndWait().orElse(null) == ButtonType.OK) {
            // Navigate back to the previous screen or perform other actions as needed
            screenManager.goBack();
        }
    }

    // Validation logic for progress input fields
    private boolean validateInput() {
        // Implement your validation logic here

        // Example: Check if required fields are not empty
        if (learnerIDField.getText().isEmpty() || instructorIDField.getText().isEmpty() ||
            quizScoreField.getText().isEmpty() || timestampPicker.getValue() == null ||
            instructorNotesField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return false;
        }

        // Add more specific validation if needed

        return true; // Return true if validation passes, false otherwise
    }

    // Save progress data to the database
    private void saveProgressData() {
        // Implement the logic to save progress data to the database
        // You may use a service or manager class for database operations

        // Example: ProgressManager.saveProgress(new Progress(...));
        // Replace the following line with your actual database operation code

        // Dummy code for illustration purposes
        int learnerID = Integer.parseInt(learnerIDField.getText());
        int instructorID = Integer.parseInt(instructorIDField.getText());
        double quizScore = Double.parseDouble(quizScoreField.getText());
        LocalDateTime timestamp = LocalDateTime.of( timestampPicker.getValue(), LocalTime.now());
        String instructorNotes = instructorNotesField.getText();

        // Assuming you have a class or method to handle database operations
        // Replace this with your actual logic
        DatabaseConnector.saveProgress(new Progress(0, learnerID, instructorID, quizScore, false, timestamp, instructorNotes, null));
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
