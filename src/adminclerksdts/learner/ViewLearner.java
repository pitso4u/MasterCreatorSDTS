package adminclerksdts.learner;

import adminclerksdts.AdminClerkSDTS;
import adminclerksdts.ControlledScreen;
import adminclerksdts.DashboardScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

public class ViewLearner implements ControlledScreen {

     @FXML
    private TextField searchField;
    private FilteredList<Learner> filteredLearners;

    @FXML
    private TableView<Learner> learnerTable;

    @FXML
    private TableColumn<Learner, String> fullNameColumn;

    @FXML
    private TableColumn<Learner, String> addressColumn;

    @FXML
    private TableColumn<Learner, String> usernameColumn;

    

    @FXML
    private TableColumn<Learner, String> emailColumn;

    @FXML
    private TableColumn<Learner, String> idNumberColumn;

    @FXML
    private TableColumn<Learner, LocalDate> registrationDateColumn;

    @FXML
    private TableColumn<Learner, String> genderColumn;

    @FXML
    private TableColumn<Learner, String> emergencyContactColumn;

    @FXML
    private TableColumn<Learner, String> preferredLanguageColumn;

    @FXML
    private TableColumn<Learner, Boolean> previousExperienceColumn;

    @FXML
    private TableColumn<Learner, String> packageTypeColumn;

    @FXML
    private TableColumn<Learner, String> notesColumn;

    @FXML
    private TableColumn<Learner, String> statusColumn;
    @FXML
    private TableColumn<Learner, Integer> learnerIdColumn;
    @FXML
    private TableColumn<Learner, LocalDate> dateOfBirthColumn;
    @FXML
    private TableColumn<Learner, String> passwordColumn;
    @FXML
    private TableColumn<Learner, String> contactNumberColumn;
    @FXML
    private TableColumn<Learner, Integer> activePackageIdColumn;

    private ScreenManager screenManager;
    

    public void initialize() {
        // Set up the learner table columns
        fullNameColumn.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        passwordColumn.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        contactNumberColumn.setCellValueFactory(cellData -> cellData.getValue().contact_numberProperty());
        dateOfBirthColumn.setCellValueFactory(cellData -> cellData.getValue().dateOfBirthProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        idNumberColumn.setCellValueFactory(cellData -> cellData.getValue().idNumberProperty());
        registrationDateColumn.setCellValueFactory(cellData -> cellData.getValue().registrationDateProperty());
        genderColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        emergencyContactColumn.setCellValueFactory(cellData -> cellData.getValue().emergencyContactProperty());
        preferredLanguageColumn.setCellValueFactory(cellData -> cellData.getValue().preferredLanguageProperty());
        previousExperienceColumn.setCellValueFactory(cellData -> cellData.getValue().previousExperienceProperty().asObject());
        packageTypeColumn.setCellValueFactory(cellData -> cellData.getValue().packageTypeProperty());
        notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // Load learner data into the table
        loadLearnerData();
    }

    // Method to load learner data into the table
    public void loadLearnerData() {
        filteredLearners = new FilteredList<>(FXCollections.observableArrayList(DatabaseConnector.getLearners()), p -> true);
        learnerTable.setItems(filteredLearners);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
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
        // Code to run when the screen is changed, if needed
    }

    @Override
    public void cleanup() {
        // Cleanup code, if needed
    }
@FXML
    private void handleSearchAction() {
        String query = searchField.getText().toLowerCase();
        filteredLearners.setPredicate(learner -> {
            if (query == null || query.isEmpty()) {
                return true; // No filter
            }

            // Compare learner's full name, username, etc. with the query
            return learner.getFullName().toLowerCase().contains(query) ||
                   learner.getUsername().toLowerCase().contains(query) ||
                   learner.getEmail().toLowerCase().contains(query);
        });
    }
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        screenManager.setScreen("DashboardScreen");
    }

@FXML
private void NewLearner(ActionEvent event) {
    // Obtain the DashboardScreen controller
    DashboardScreen dashboardScreen = (DashboardScreen) screenManager.getController("DashboardScreen");

    if (dashboardScreen != null) {
        // Load the NewLearnerScreen into the center of the dashboard
        dashboardScreen.loadScreenIntoCenter("NewLearnerScreen");

        // Get the NewLearnerController and reset the form if available
        ControlledScreen controller = screenManager.getController("NewLearnerScreen");
        if (controller instanceof NewLearnerController) {
            ((NewLearnerController) controller).resetForm();
        } else {
            System.err.println("Error: The loaded controller is not an instance of NewLearnerController.");
        }
    } else {
        System.err.println("Error: DashboardScreen is null. Ensure the screen is correctly loaded.");
    }
}

@FXML
private void LoadLearners(ActionEvent event) {
    // Load learner data
    loadLearnerData();
}

@FXML
    private void EditLearner(ActionEvent event) {
        Learner selectedLearner = learnerTable.getSelectionModel().getSelectedItem();
        
        if (selectedLearner != null) {
            DashboardScreen dashboardScreen = (DashboardScreen) screenManager.getController("DashboardScreen");

            if (dashboardScreen != null) {
                dashboardScreen.loadScreenIntoCenter("NewLearnerScreen");
                NewLearnerController newLearnerController = (NewLearnerController) screenManager.getController("NewLearnerScreen");
                
                if (newLearnerController != null) {
                    newLearnerController.setLearnerToEdit(selectedLearner);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Could not initialize the learner edit form.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not access the dashboard screen.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Learner Selected", "Please select a learner to edit.");
        }
    }

   @FXML
private void DeleteLearner(ActionEvent event) {
    Learner selectedLearner = learnerTable.getSelectionModel().getSelectedItem();
    if (selectedLearner != null) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Delete Learner");
        confirmationAlert.setContentText("Are you sure you want to delete this learner? This action cannot be undone.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = DatabaseConnector.deleteLearner(selectedLearner.getLearnerId());
            if (deleted) {
                loadLearnerData();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Learner deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete learner.");
            }
        }
    } else {
        showAlert(Alert.AlertType.WARNING, "No Learner Selected", "Please select a learner to delete.");
    }
}
}
