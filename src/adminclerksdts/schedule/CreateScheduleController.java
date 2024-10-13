package adminclerksdts.schedule;

import adminclerksdts.ControlledScreen;
import adminclerksdts.DashboardScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import adminclerksdts.settings.Settings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;

public class CreateScheduleController implements Initializable, ControlledScreen {

     @FXML private GridPane calendarGrid;
    @FXML private ListView<String> classList;
    @FXML private Label currentMonthLabel;
    @FXML private Label morningSlot1Available, morningSlot2Available, afternoonSlot1Available, afternoonSlot2Available;
    @FXML private Label morningSlot1Total, morningSlot2Total, afternoonSlot1Total, afternoonSlot2Total;

    
    @FXML
    private ListView<String> morningList1;
    @FXML
    private ListView<String> morningList2;
    @FXML
    private ListView<String> afternoonList1;
    @FXML
    private ListView<String> afternoonList2;
 private ScreenManager screenManager;
    private LocalDate currentDate = LocalDate.now();
    private YearMonth currentYearMonth;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentYearMonth = YearMonth.from(currentDate);
        updateCalendarView();
    }

    private void loadCalendar() {
        calendarGrid.getChildren().clear();

        // Add day of week labels
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(dayNames[i]);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            calendarGrid.add(dayLabel, i, 0);
        }

        LocalDate date = currentYearMonth.atDay(1);
        int column = date.getDayOfWeek().getValue() % 7;
        int row = 1;

        while (!date.isAfter(currentYearMonth.atEndOfMonth())) {
            StackPane dayPane = createDayPane(date);
            calendarGrid.add(dayPane, column, row);
            
            column = (column + 1) % 7;
            if (column == 0) row++;
            
            date = date.plusDays(1);
        }
    }

    private StackPane createDayPane(LocalDate date) {
        StackPane dayPane = new StackPane();
        dayPane.setMinSize(50, 50);
        dayPane.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0.5px;");

        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);

        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.setStyle("-fx-font-weight: bold;");

        int availableSlots = getTotalAvailableSlotsForDay(date);
        Label slotsLabel = new Label(availableSlots + " slots");
        slotsLabel.setStyle("-fx-font-size: 10px;");

        content.getChildren().addAll(dayLabel, slotsLabel);

        dayPane.getChildren().add(content);
        updateDayPaneStyle(dayPane, availableSlots);

        dayPane.setOnMouseClicked(e -> handleDayClick(date));

        return dayPane;
    }

    private void updateDayPaneStyle(StackPane dayPane, int availableSlots) {
        if (availableSlots == 0) {
            dayPane.setStyle("-fx-background-color: #FF9999; -fx-border-color: #cccccc; -fx-border-width: 0.5px;"); // Light red for fully booked
        } else if (availableSlots < 80) {
            dayPane.setStyle("-fx-background-color: #FFFF99; -fx-border-color: #cccccc; -fx-border-width: 0.5px;"); // Light yellow for partially booked
        } else {
            dayPane.setStyle("-fx-background-color: #99FF99; -fx-border-color: #cccccc; -fx-border-width: 0.5px;"); // Light green for available
        }
    }

    private int getTotalAvailableSlotsForDay(LocalDate date) {
        int totalSlots = 80; // 20 slots * 4 time periods
        int bookedSlots = 0;
        LocalTime[] startTimes = {LocalTime.of(8, 0), LocalTime.of(10, 0), LocalTime.of(14, 0), LocalTime.of(16, 0)};
        for (LocalTime startTime : startTimes) {
            bookedSlots += DatabaseConnector.getNumberOfLearnersForSlot(date, startTime, startTime.plusHours(2));
        }
        return totalSlots - bookedSlots;
    }

    private void handleDayClick(LocalDate date) {
        currentDate = date;
        updateAvailableSlots(date);
        loadClassList(date);
    }
  private void updateSlotAvailability(Label availableLabel, Label totalLabel, LocalDate date, LocalTime startTime) {
        LocalTime endTime = startTime.plusHours(2);
        int numberOfLearners = DatabaseConnector.getNumberOfLearnersForSlot(date, startTime, endTime);
        int maxSeats = Settings.getInstance().getMaxSeats();
        int availableSlots = maxSeats - numberOfLearners;

        availableLabel.setText(availableSlots + " / " + maxSeats + " available");
        totalLabel.setText("Total learners: " + numberOfLearners);

        if (availableSlots == 0) {
            availableLabel.setStyle("-fx-text-fill: red;");
        } else if (availableSlots < 5) {
            availableLabel.setStyle("-fx-text-fill: orange;");
        } else {
            availableLabel.setStyle("-fx-text-fill: green;");
        }
    }

    private void loadClassList(LocalDate date) {
        ObservableList<String> classes = FXCollections.observableArrayList(DatabaseConnector.getClassesForDate(date));
        classList.setItems(classes);

        loadSlotList(morningList1, date, LocalTime.of(8, 0), LocalTime.of(10, 0));
        loadSlotList(morningList2, date, LocalTime.of(10, 0), LocalTime.of(12, 0));
        loadSlotList(afternoonList1, date, LocalTime.of(14, 0), LocalTime.of(16, 0));
        loadSlotList(afternoonList2, date, LocalTime.of(16, 0), LocalTime.of(18, 0));

        updateSlotAvailability(morningSlot1Available, morningSlot1Total, date, LocalTime.of(8, 0));
        updateSlotAvailability(morningSlot2Available, morningSlot2Total, date, LocalTime.of(10, 0));
        updateSlotAvailability(afternoonSlot1Available, afternoonSlot1Total, date, LocalTime.of(14, 0));
        updateSlotAvailability(afternoonSlot2Available, afternoonSlot2Total, date, LocalTime.of(16, 0));
    }

    private void loadSlotList(ListView<String> listView, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<String> learners = DatabaseConnector.getLearnersForSlot(date, startTime, endTime);
        ObservableList<String> learnersWithDetails = FXCollections.observableArrayList();

        for (String learner : learners) {
            int learnerId = DatabaseConnector.getLearnerIdByName(learner);
            int remainingLessons = DatabaseConnector.getRemainingLessons(learnerId);
            int remainingTests = DatabaseConnector.getRemainingTests(learnerId);
            learnersWithDetails.add(String.format("%s (Lessons: %d, Tests: %d)", learner, remainingLessons, remainingTests));
        }

        listView.setItems(learnersWithDetails);
    }
    private void updateAvailableSlots(LocalDate date) {
        updateSlotAvailability(morningSlot1Available, date, LocalTime.of(8, 0));
        updateSlotAvailability(morningSlot2Available, date, LocalTime.of(10, 0));
        updateSlotAvailability(afternoonSlot1Available, date, LocalTime.of(14, 0));
        updateSlotAvailability(afternoonSlot2Available, date, LocalTime.of(16, 0));
    }

    private void updateSlotAvailability(Label label, LocalDate date, LocalTime startTime) {
        LocalTime endTime = startTime.plusHours(2);
        int numberOfLearners = DatabaseConnector.getNumberOfLearnersForSlot(date, startTime, endTime);
        int maxSeats = Settings.getInstance().getMaxSeats();
        int availableSlots = maxSeats - numberOfLearners;

        label.setText(availableSlots + " / " + maxSeats + " available");

        if (availableSlots == 0) {
            label.setStyle("-fx-text-fill: red;");
        } else if (availableSlots < 5) {
            label.setStyle("-fx-text-fill: orange;");
        } else {
            label.setStyle("-fx-text-fill: green;");
        }
    }

//    private void loadClassList(LocalDate date) {
//    // Retrieve and populate the general class list for the day
//    ObservableList<String> classes = FXCollections.observableArrayList(DatabaseConnector.getClassesForDate(date));
//    System.out.println("Classes retrieved: " + classes);
//    classList.setItems(classes);
//
//    ObservableList<String> morningClasses1 = DatabaseConnector.getClassesForSlot(date, LocalTime.of(8, 0), LocalTime.of(10, 0));
//    morningList1.setItems(morningClasses1);
//
//    ObservableList<String> morningClasses2 = DatabaseConnector.getClassesForSlot(date, LocalTime.of(10, 0), LocalTime.of(12, 0));
//    morningList2.setItems(morningClasses2);
//
//    ObservableList<String> afternoonClasses1 = DatabaseConnector.getClassesForSlot(date, LocalTime.of(14, 0), LocalTime.of(16, 0));
//    afternoonList1.setItems(afternoonClasses1);
//
//    ObservableList<String> afternoonClasses2 = DatabaseConnector.getClassesForSlot(date, LocalTime.of(16, 0), LocalTime.of(18, 0));
//    afternoonList2.setItems(afternoonClasses2);
//}
//


    @FXML
    public void handleCreateScheduleButtonAction() {
        try {
            // Obtain the DashboardScreen controller
    DashboardScreen dashboardScreen = (DashboardScreen) screenManager.getController("DashboardScreen");

    if (dashboardScreen != null) {
        // Load the CreateScheduleScreen into the center of the dashboard
        dashboardScreen.loadScreenIntoCenter("CreateScheduleDialogScreen");

        // Get the CreateScheduleScreen and reset the form if available
        ControlledScreen controller = screenManager.getController("CreateScheduleDialogScreen");
        if (controller instanceof CreateScheduleDialogController) {
            ((CreateScheduleDialogController) controller).cleanup();
        } else {
            System.err.println("Error: The loaded controller is not an instance of NewLearnerController.");
        }
    } else {
        System.err.println("Error: DashboardScreen is null. Ensure the screen is correctly loaded.");
    }
            loadCalendar();
            updateAvailableSlots(currentDate);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to open the dialog");
            alert.setContentText("Could not open the create schedule dialog.");
            alert.showAndWait();
        }
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        this.screenManager = screenParent;
    }

    @Override
    public void runOnScreenChange() {
        currentDate = LocalDate.now();
        currentYearMonth = YearMonth.from(currentDate);
        updateCalendarView();
        updateAvailableSlots(currentDate);
        loadClassList(currentDate);
    }

    @Override
    public void cleanup() {
        // Cleanup code, if needed
    }

    @FXML
    public void handlePreviousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendarView();
    }

    @FXML
    public void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendarView();
    }

    private void updateCalendarView() {
        currentMonthLabel.setText(currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        loadCalendar();
        updateAvailableSlots(currentYearMonth.atDay(1));
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        screenManager.setScreen("DashboardScreen");
    }
}