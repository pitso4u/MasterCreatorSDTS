
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts;

/**
 *
 * @author pitso
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginScreen implements ControlledScreen {

    private ScreenManager screenManager;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    public void initialize() {
    if (usernameField == null || passwordField == null || loginButton == null) {
        System.err.println("Error: One or more UI elements not properly initialized");
    }
    usernameField.setText("mosa");
    passwordField.setText("mosa");
}
    @FXML
    private void handleLoginButtonAction() {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        boolean loginSuccessful = validateLogin(enteredUsername, enteredPassword);

        if (loginSuccessful) {
            if (screenManager != null) {
                screenManager.setScreen("DashboardScreen");
            } else {
                System.err.println("Error: screenManager is null");
                showAlert("Error", "An internal error occurred. Please try again.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Login Failed", "Invalid username or password.", Alert.AlertType.ERROR);
        }
    }

    private loginDat getLearnerDataFromDatabase(String username, String password) {
        try ( Connection connection = DatabaseConnector.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM learners WHERE username = ? AND password = ?");) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int learnerId = resultSet.getInt("learner_id");
                    String fullName = resultSet.getString("full_name");
                    LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                    String email = resultSet.getString("email");

                    loginDat learnerData = new loginDat(learnerId, username, password, fullName, dateOfBirth, email);

                    return learnerData;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return null;
    }

    private boolean validateLogin(String username, String password) {
        // Connect to the database and check if the credentials are valid
        try ( Connection connection = DatabaseConnector.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM learners WHERE username = ? AND password = ?");) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // If there is at least one row, credentials are valid
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
            return false; // Login fails due to an exception
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void cleanup() {
        // Implement any cleanup operations needed for this screen
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        this.screenManager = screenParent;
    }

    @Override
    public void runOnScreenChange() {
        // Code to run when the screen is changed, if needed
    }
}
