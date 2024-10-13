package adminclerksdts;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class BackupController {

    @FXML
    private Label statusLabel;

    @FXML
    private void backupDatabase() {
        // Choose a file location to save the backup
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Database Backup");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQL Files", "*.sql"));
        File file = fileChooser.showSaveDialog(statusLabel.getScene().getWindow());

        if (file != null) {
            String backupFilePath = file.getAbsolutePath();
            boolean success = backupPostgreSQLDatabase(backupFilePath);
            
            if (success) {
                statusLabel.setText("Status: Backup completed successfully.");
                showAlert(AlertType.INFORMATION, "Backup Completed", "Database backup saved to: " + backupFilePath);
            } else {
                statusLabel.setText("Status: Backup failed.");
                showAlert(AlertType.ERROR, "Backup Failed", "Failed to backup the database.");
            }
        }
    }

    // Method to run the PostgreSQL pg_dump command for backup
    private boolean backupPostgreSQLDatabase(String backupFilePath) {
        String pgDumpPath = "C:\\Program Files\\PostgreSQL\\14\\bin\\pg_dump"; // Path to your pg_dump utility
        String dbName = "your_database_name"; // Your database name
        String dbUser = "your_database_user"; // Your database username
        String dbPassword = "your_database_password"; // Your database password

        ProcessBuilder processBuilder = new ProcessBuilder(
                pgDumpPath,
                "-U", dbUser,
                "-F", "c", // Use custom format
                "-b", // Include large objects
                "-f", backupFilePath, // Output file
                dbName
        );
        
        // Set environment variable for PostgreSQL password
        processBuilder.environment().put("PGPASSWORD", dbPassword);
        
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            
            return exitCode == 0; // Return true if backup was successful
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false; // Return false if backup failed
        }
    }

    // Utility to show alerts
    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
