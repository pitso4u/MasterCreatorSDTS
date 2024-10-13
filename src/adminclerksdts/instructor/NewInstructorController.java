/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts.instructor;

import adminclerksdts.ControlledScreen;
import adminclerksdts.ScreenManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class NewInstructorController implements ControlledScreen {

    private TextField instructorNameField;

    private TextField contactNumberField;

    @FXML
    private TextField emailField;

    private TextField hireDateField;

    private TextField specializationField;

    private ScreenManager screenManager;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private DatePicker dobPicker;

    @Override
    public void setScreenParent(ScreenManager screenPage) {
        this.screenManager = screenPage;
    }

    private void handleSaveInstructor(ActionEvent event) {
        // Get values from text fields
        String instructorName = instructorNameField.getText();
        String contactNumber = contactNumberField.getText();
        String email = emailField.getText();
        String hireDate = hireDateField.getText();
        String specialization = specializationField.getText();

        // Perform validation (you can add more validation logic)
        if (instructorName.isEmpty() || contactNumber.isEmpty() || email.isEmpty() || hireDate.isEmpty() || specialization.isEmpty()) {
            showAlert("Please fill in all fields", Alert.AlertType.ERROR);
            return;
        }

        // TODO: Save instructor details to the database (implement your logic here)

        // Show success message
        showAlert("Instructor saved successfully", Alert.AlertType.INFORMATION);

        // Clear the input fields
        clearFields();
    }

    private void handleCancel(ActionEvent event) {
        // Navigate back to the previous screen (you can modify this based on your navigation structure)
        screenManager.goBack();
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        instructorNameField.clear();
        contactNumberField.clear();
        emailField.clear();
        hireDateField.clear();
        specializationField.clear();
    }
@Override
    public void runOnScreenChange() {
        // Code to run when the screen is changed, if needed
    }

    @Override
    public void cleanup() {
        // Cleanup resources or perform any necessary cleanup when navigating away from this screen
    }


   @FXML
    private void handleBackButtonAction(ActionEvent event) {
    
        // Navigate back to the previous screen (you might need to keep track of the previous screen)
        screenManager.goBack();
    
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
    }

}
