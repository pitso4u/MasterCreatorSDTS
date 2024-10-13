package adminclerksdts.schedule;

import adminclerksdts.ControlledScreen;
import adminclerksdts.DashboardScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.DateCell;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class CreateScheduleDialogController implements ControlledScreen {

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> timeSlotComboBox;
    @FXML private ComboBox<String> learnerComboBox;
    @FXML private Label errorLabel;
    @FXML private Label remainingClassesLabel;

    private ScreenManager screenManager;
    private boolean scheduleCreated = false;

    public void initialize() {
        timeSlotComboBox.setItems(FXCollections.observableArrayList("8 AM - 10 AM", "10 AM - 12 PM", "2 PM - 4 PM", "4 PM - 6 PM"));
        resetFields();

        learnerComboBox.setOnAction(e -> updateRemainingClasses(learnerComboBox.getValue()));

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0);
            }
        });
    }

    private void resetFields() {
        datePicker.setValue(null);
        timeSlotComboBox.setValue(null);
        learnerComboBox.setValue(null);
        errorLabel.setVisible(false);
        remainingClassesLabel.setText("");

        List<String> learnerNames = DatabaseConnector.getAllLearnerNames();
        Collections.sort(learnerNames);
        learnerComboBox.setItems(FXCollections.observableArrayList(learnerNames));
    }

    public boolean isScheduleCreated() {
        return scheduleCreated;
    }

    @FXML
    private void handleCancel() {
        navigateToCreateScheduleScreen();
    }

    @FXML
    private void handleCreate() {
        LocalDate date = datePicker.getValue();
        String timeSlot = timeSlotComboBox.getValue();
        String learnerName = learnerComboBox.getValue();

        if (date != null && timeSlot != null && learnerName != null) {
            LocalTime startTime = parseTimeSlot(timeSlot);
            LocalTime endTime = startTime.plusHours(2);
            int learnerId = DatabaseConnector.getLearnerIdByName(learnerName);

            String validationMessage = getValidationMessage(learnerId, date, startTime, endTime);
            if (validationMessage.isEmpty()) {
                DatabaseConnector.saveSchedule(new Schedule(learnerId, date, startTime, endTime));
                scheduleCreated = true;
                navigateToCreateScheduleScreen();
            } else {
                errorLabel.setText(validationMessage);
                errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText("Please fill in all fields.");
            errorLabel.setVisible(true);
        }
    }

    private void navigateToCreateScheduleScreen() {
        DashboardScreen dashboardScreen = (DashboardScreen) screenManager.getController("DashboardScreen");
        if (dashboardScreen != null) {
            dashboardScreen.loadScreenIntoCenter("CreateScheduleScreen");
            ControlledScreen controller = screenManager.getController("CreateScheduleScreen");
            if (controller instanceof CreateScheduleController) {
                ((CreateScheduleController) controller).cleanup();
                ((CreateScheduleController) controller).runOnScreenChange();
            } else {
                System.err.println("Error: The loaded controller is not an instance of CreateScheduleController.");
            }
        } else {
            System.err.println("Error: DashboardScreen is null. Ensure the screen is correctly loaded.");
        }
    }

    private String getValidationMessage(int learnerId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        int bookedClasses = DatabaseConnector.getBookedClassesCount(learnerId);
        int maxAllowedClasses = DatabaseConnector.getLearnerPackageLimit(learnerId);

        if (bookedClasses >= maxAllowedClasses) {
            return "You have reached your package limit.";
        }

        int currentBookings = DatabaseConnector.getNumberOfLearnersForSlot(date, startTime, endTime);
        if (currentBookings >= 20) {
            return "This time slot is fully booked.";
        }

        return "";
    }

    private LocalTime parseTimeSlot(String timeSlot) {
        switch (timeSlot) {
            case "8 AM - 10 AM": return LocalTime.of(8, 0);
            case "10 AM - 12 PM": return LocalTime.of(10, 0);
            case "2 PM - 4 PM": return LocalTime.of(14, 0);
            case "4 PM - 6 PM": return LocalTime.of(16, 0);
            default: return null;
        }
    }

    private void updateRemainingClasses(String learnerName) {
        if (learnerName != null && !learnerName.isEmpty()) {
            int learnerId = DatabaseConnector.getLearnerIdByName(learnerName);
            int bookedClasses = DatabaseConnector.getBookedClassesCount(learnerId);
            int maxAllowedClasses = DatabaseConnector.getLearnerPackageLimit(learnerId);
            int remainingClasses = maxAllowedClasses - bookedClasses;
            remainingClassesLabel.setText("Remaining classes: " + remainingClasses);
        } else {
            remainingClassesLabel.setText("");
        }
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        this.screenManager = screenParent;
    }

    @Override
    public void runOnScreenChange() {
        resetFields();
    }

    @Override
    public void cleanup() {
        // No specific cleanup needed for this controller
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        navigateToCreateScheduleScreen();
    }
}