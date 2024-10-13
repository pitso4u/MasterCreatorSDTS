package adminclerksdts.schedule;

import adminclerksdts.AdminClerkSDTS;
import adminclerksdts.ControlledScreen;
import adminclerksdts.DashboardScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewSchedulesController implements Initializable, ControlledScreen {

    private ScreenManager screenManager;

    @FXML
    private TableView<Schedule> scheduleTable;

    @FXML
    private TableColumn<Schedule, Number> schedule_id;

    @FXML
    private TableColumn<Schedule, String> learnerName;

    @FXML
    private TableColumn<Schedule, LocalDate> session_date;

    @FXML
    private TableColumn<Schedule, String> session_start_time;

    @FXML
    private TableColumn<Schedule, String> session_end_time;

    @FXML
    private TextField searchField;

    private FilteredList<Schedule> filteredSchedules;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeScheduleTable();
        loadScheduleData();

        // Bind search functionality to the search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAction());
    }

    private void initializeScheduleTable() {
        schedule_id.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getScheduleId()));
        learnerName.setCellValueFactory(cellData -> 
            new SimpleStringProperty(DatabaseConnector.getLearnerNameById(cellData.getValue().getLearnerId()))
        );
        session_date.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSessionDate()));
        session_start_time.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getSessionStartTime().toString())
        );
        session_end_time.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getSessionEndTime().toString())
        );
    }

    private void loadScheduleData() {
        ObservableList<Schedule> schedules = DatabaseConnector.getSchedules();
        if (schedules != null && !schedules.isEmpty()) {
            filteredSchedules = new FilteredList<>(schedules, p -> true);
            scheduleTable.setItems(filteredSchedules);
        }
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
    private void handleBackButtonAction(ActionEvent event) {
        screenManager.setScreen("DashboardScreen");
    }

    @FXML
    private void LoadSchedules(ActionEvent event) {
        loadScheduleData();
    }

    @FXML
    private void EditSchedules(ActionEvent event) {
        Schedule selectedSchedule = scheduleTable.getSelectionModel().getSelectedItem();
        if (selectedSchedule != null) {
            screenManager.setScreen("CreateScheduleScreen");
            CreateScheduleController newScheduleController = 
                (CreateScheduleController) screenManager.getController("CreateScheduleScreen");
            if (newScheduleController != null) {
                // Optionally, set the selected schedule for editing
                // newScheduleController.setScheduleToEdit(selectedSchedule);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Schedule Selected", "Please select a schedule to edit.");
        }
    }

    @FXML
    private void DeleteSchedules(ActionEvent event) {
        Schedule selectedSchedule = scheduleTable.getSelectionModel().getSelectedItem();
        if (selectedSchedule != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Are you sure you want to delete this schedule?");
            confirmation.setContentText("This action cannot be undone.");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DatabaseConnector.deleteSchedule(selectedSchedule.getScheduleId());
                scheduleTable.getItems().remove(selectedSchedule);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a schedule to delete.");
        }
    }

    @FXML
    private void NewSchedule(ActionEvent event) {
        DashboardScreen dashboardScreen = (DashboardScreen) screenManager.getController("DashboardScreen");

        if (dashboardScreen != null) {
            dashboardScreen.loadScreenIntoCenter("CreateScheduleScreen");

            ControlledScreen controller = screenManager.getController("CreateScheduleScreen");
            if (controller instanceof CreateScheduleController) {
                ((CreateScheduleController) controller).cleanup();
            } else {
                System.err.println("Error: The loaded controller is not an instance of CreateScheduleController.");
            }
        } else {
            System.err.println("Error: DashboardScreen is null. Ensure the screen is correctly loaded.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleSearchAction() {
        String query = searchField.getText().toLowerCase();
        filteredSchedules.setPredicate(schedule -> {
            if (query == null || query.isEmpty()) {
                return true; // No filter
            }

            // Compare learner's full name or session date with the query
            return DatabaseConnector.getLearnerNameById(schedule.getLearnerId()).toLowerCase().contains(query) ||
                   schedule.getSessionDate().toString().contains(query);
        });
    }
}
