package adminclerksdts;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SettingsController implements ControlledScreen {

    private ScreenManager screenManager;

    @FXML
    private ChoiceBox<String> themeChoiceBox;
    
    @FXML
    private TextField dbHostField;

    @FXML
    private TextField dbPortField;

    @FXML
    private TextField dbNameField;

    @FXML
    private TextField dbUserField;

    @FXML
    private PasswordField dbPasswordField;

    @FXML
    private Label statusLabel;

    public void initialize() {
        loadSettings();
    }

    private void loadSettings() {
        // Load settings from a configuration file or database
        themeChoiceBox.getItems().addAll("Light", "Dark", "Blue");
        themeChoiceBox.setValue("Light"); // Example default theme

        // Load database settings (these can come from properties or config files)
        dbHostField.setText("localhost");
        dbPortField.setText("5432");
        dbNameField.setText("your_database_name");
        dbUserField.setText("your_username");
        dbPasswordField.setText("your_password");
    }

    @FXML
    private void handleSaveButtonAction() {
        String theme = themeChoiceBox.getValue();
        String dbHost = dbHostField.getText();
        String dbPort = dbPortField.getText();
        String dbName = dbNameField.getText();
        String dbUser = dbUserField.getText();
        String dbPassword = dbPasswordField.getText();

        // Save the settings to a file, database, or properties file
        boolean success = saveSettings(theme, dbHost, dbPort, dbName, dbUser, dbPassword);

        if (success) {
            statusLabel.setText("Settings saved successfully.");
            showAlert("Success", "Settings have been saved successfully.", Alert.AlertType.INFORMATION);
        } else {
            statusLabel.setText("Failed to save settings.");
            showAlert("Error", "Failed to save settings.", Alert.AlertType.ERROR);
        }
    }

    private boolean saveSettings(String theme, String dbHost, String dbPort, String dbName, String dbUser, String dbPassword) {
        // Here you would save the settings (e.g., write to a properties file or update the database)
        try {
            // Example: save to a configuration file or database
            // You can use java.util.Properties, or your database configuration
            System.out.println("Theme: " + theme);
            System.out.println("DB Host: " + dbHost);
            System.out.println("DB Port: " + dbPort);
            System.out.println("DB Name: " + dbName);
            System.out.println("DB User: " + dbUser);
            System.out.println("DB Password: " + dbPassword);
            return true; // Return true if save was successful
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if save failed
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        loadSettings(); // Reset fields to saved settings
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        this.screenManager = screenParent;
    }

    @Override
    public void runOnScreenChange() {
        // Called when the screen becomes active, refresh settings if needed
        loadSettings();
    }

    @Override
    public void cleanup() {
        // Optional cleanup code when leaving the screen
    }
}
