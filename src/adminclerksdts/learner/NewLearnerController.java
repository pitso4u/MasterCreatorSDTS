package adminclerksdts.learner;

import adminclerksdts.ControlledScreen;
import adminclerksdts.DashboardScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.LearnerPackage;
import adminclerksdts.ScreenManager;
import adminclerksdts.Package;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.util.Duration;
import javafx.animation.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class NewLearnerController implements Initializable, ControlledScreen {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField fullNameField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TextField contactNumberField;
    @FXML private TextField idNumberField;
    @FXML private DatePicker registrationDatePicker;
    @FXML private ChoiceBox<String> genderChoiceBox;
    @FXML private TextField emergencyContactField;
    @FXML private ChoiceBox<String> preferredLanguageChoiceBox;
    @FXML private CheckBox previousExperienceCheckBox;
    @FXML private TextField packageTypeField;
    @FXML private TextArea notesArea;
    @FXML private ChoiceBox<String> statusChoiceBox;
    @FXML private ChoiceBox<Package> packageChoiceBox;
    
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private ScreenManager screenManager;
    private Learner learnerToEdit;
    private LearnerPackage learnerPackageToEdit;
    @FXML
    private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (learnerToEdit != null) {
            setFieldsForEditing();
        }
        
        setupChoiceBoxes();
        loadPackages();
    }

    private void setupChoiceBoxes() {
        genderChoiceBox.getItems().addAll("Male", "Female", "Other");
        preferredLanguageChoiceBox.getItems().addAll("English", "Sesotho","Zulu","Xhosa","Setswana", "Sepedi", "Venda","Sign Lang","Afrikaans");
        statusChoiceBox.getItems().addAll("Active", "Inactive", "Suspended");
    }

    private void loadPackages() {
        List<Package> packages = DatabaseConnector.getAllPackages();
        packageChoiceBox.getItems().addAll(packages);
        packageChoiceBox.setConverter(new StringConverter<Package>() {
            @Override
            public String toString(Package pkg) {
                return pkg == null ? "" : pkg.getName() + " - " + pkg.getDurationDays() + " days, " + pkg.getTotalTests() + " tests";
            }

            @Override
            public Package fromString(String string) {
                return null;
            }
        });
    }

    private void navigateToScreen(String screenName) {
        screenManager.setScreen(screenName);
        ViewLearner viewLearnerController = (ViewLearner) screenManager.getController(screenName);
        if (viewLearnerController != null) {
            viewLearnerController.runOnScreenChange();
        } else {
            System.err.println("Error: " + screenName + " Controller is null.");
        }
    }

    @FXML
    private void handleSaveButtonAction() {
         if (validateInput()) {
            saveOrUpdateLearnerPackage();
            showAlert(Alert.AlertType.INFORMATION, "Learner Saved", "Learner information has been successfully saved.");
            navigateToViewLearnerScreen();
        }
    }

    private void handleCancelButtonAction(ActionEvent event) {
        navigateToViewLearnerScreen();
    }

    private void saveOrUpdateLearnerPackage() {
        saveLearnerData();
        Package selectedPackage = packageChoiceBox.getValue();
        if (selectedPackage != null) {
            LocalDate startDate = LocalDate.now();
            int testsRemaining = selectedPackage.getTotalTests();
            int daysRemaining = selectedPackage.getDurationDays();

            try {
                if (learnerPackageToEdit != null) {
                    learnerPackageToEdit.setPackageId(selectedPackage.getPackageId());
                    learnerPackageToEdit.setStartDate(startDate);
                    learnerPackageToEdit.setTestsRemaining(testsRemaining);
                    learnerPackageToEdit.setDaysRemaining(daysRemaining);
                    learnerPackageToEdit.setPackageCompleted(false);
                    DatabaseConnector.updateLearnerPackage(learnerPackageToEdit);
                } else {
                    LearnerPackage newPackage = new LearnerPackage(0, learnerToEdit.getLearnerId(), selectedPackage.getPackageId(),
                            startDate, testsRemaining, daysRemaining, false, null);
                    DatabaseConnector.saveLearnerPackage(newPackage);
                }
                DatabaseConnector.updateLearnerActivePackageId(learnerToEdit.getLearnerId(), selectedPackage.getPackageId());
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save learner package: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void saveLearnerData() {
        String username = usernameField.getText();

        if (DatabaseConnector.isUsernameTaken(username) && (learnerToEdit == null || !learnerToEdit.getUsername().equals(username))) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Username already exists.");
            return;
        }

        Package selectedPackage = packageChoiceBox.getValue();
        int activePackageId = selectedPackage != null ? selectedPackage.getPackageId() : 0;

        Learner newLearner = new Learner(
                
            learnerToEdit != null ? learnerToEdit.getLearnerId() : 0,
            fullNameField.getText(),
            addressField.getText(),
            passwordField.getText(),
            username,
            dobPicker.getValue(),
            contactNumberField.getText(),
            emailField.getText(),
            activePackageId,
            idNumberField.getText(),
            genderChoiceBox.getValue(),
            emergencyContactField.getText(),
            preferredLanguageChoiceBox.getValue(),
            packageTypeField.getText(),
            previousExperienceCheckBox.isSelected(),
            notesArea.getText(),
            statusChoiceBox.getValue(),
            registrationDatePicker.getValue());

        if (learnerToEdit == null) {
            int learnerId = DatabaseConnector.saveLearner(newLearner);
            if (learnerId != -1) {
                newLearner.setLearnerId(learnerId);
                learnerToEdit = newLearner;
                showAlert(Alert.AlertType.INFORMATION, "Success", "Learner saved successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save learner. Please try again.");
            }
        } else {
            DatabaseConnector.updateLearner(newLearner);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Learner updated successfully.");
        }
    }

    public void resetForm() {
        usernameField.clear();
        passwordField.clear();
        fullNameField.clear();
        dobPicker.setValue(null);
        emailField.clear();
        addressField.clear();
        contactNumberField.clear();
        idNumberField.clear();
        registrationDatePicker.setValue(LocalDate.now());
        genderChoiceBox.setValue(null);
        emergencyContactField.clear();
        preferredLanguageChoiceBox.setValue(null);
        previousExperienceCheckBox.setSelected(false);
        packageTypeField.clear();
        notesArea.clear();
        statusChoiceBox.setValue("Active");
        learnerToEdit = null;
        learnerPackageToEdit = null;
        packageChoiceBox.setValue(null);
    }

    private boolean validateInput() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()
                || fullNameField.getText().isEmpty() || dobPicker.getValue() == null
                || emailField.getText().isEmpty() || idNumberField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all required fields.");
            return false;
        }
        return true;
    }

    public void setFieldsForEditing() {
        if (learnerToEdit != null) {
            usernameField.setText(learnerToEdit.getUsername());
            passwordField.setText(learnerToEdit.getPassword());
            fullNameField.setText(learnerToEdit.getFullName());
            dobPicker.setValue(learnerToEdit.getDateOfBirth());
            emailField.setText(learnerToEdit.getEmail());
            addressField.setText(learnerToEdit.getAddress());
            contactNumberField.setText(learnerToEdit.getContact_Number());
            idNumberField.setText(learnerToEdit.getIdNumber());
            registrationDatePicker.setValue(learnerToEdit.getRegistrationDate());
            genderChoiceBox.setValue(learnerToEdit.getGender());
            emergencyContactField.setText(learnerToEdit.getEmergencyContact());
            preferredLanguageChoiceBox.setValue(learnerToEdit.getPreferredLanguage());
            previousExperienceCheckBox.setSelected(learnerToEdit.isPreviousExperience());
            packageTypeField.setText(learnerToEdit.getPackageType());
            notesArea.setText(learnerToEdit.getNotes());
            statusChoiceBox.setValue(learnerToEdit.getStatus());

            // Additional logic to load profile photo and package details
            // if (learnerToEdit.getProfilePhoto() != null) {
            //     profilePhotoView.setImage(new Image(new ByteArrayInputStream(learnerToEdit.getProfilePhoto())));
            // }

            if (learnerPackageToEdit != null) {
                packageChoiceBox.setValue(DatabaseConnector.getPackageById(learnerPackageToEdit.getPackageId()));
            }
        }
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        screenManager = screenParent;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setLearnerToEdit(Learner learnerToEdit) {
        this.learnerToEdit = learnerToEdit;
        if (learnerToEdit != null) {
            setFieldsForEditing();
        }
    }

    public void setLearnerPackageToEdit(LearnerPackage learnerPackageToEdit) {
        this.learnerPackageToEdit = learnerPackageToEdit;
    }

    @Override
    public void runOnScreenChange() {
        resetForm();
    }

    @Override
    public void cleanup() {
        // Implement any cleanup logic if needed
    }
private void navigateToViewLearnerScreen() {
        DashboardScreen dashboardScreen = (DashboardScreen) screenManager.getController("DashboardScreen");
        if (dashboardScreen != null) {
            dashboardScreen.loadScreenIntoCenter("ViewLearnerScreen");
            ViewLearner viewLearnerController = (ViewLearner) screenManager.getController("ViewLearnerScreen");
            if (viewLearnerController != null) {
                viewLearnerController.loadLearnerData();
            } else {
                System.err.println("Error: ViewLearner Controller is null.");
            }
        } else {
            System.err.println("Error: DashboardScreen Controller is null.");
        }
    }

    
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToViewLearnerScreen();
    }
}