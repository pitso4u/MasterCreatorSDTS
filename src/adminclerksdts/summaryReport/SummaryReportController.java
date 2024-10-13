package adminclerksdts.summaryReport;

import adminclerksdts.ControlledScreen;
import adminclerksdts.learner.Learner;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SummaryReportController implements ControlledScreen{

    private ScreenManager screenManager;
    @FXML
    private TableView<Learner> newLearnersTable;
    @FXML
    private TableColumn<Learner, Integer> newLearnerLearnerIdColumn;
    @FXML
    private TableColumn<Learner, String> newLearnerFullNameColumn;
    @FXML
    private TableColumn<Learner, String> newLearnerDateOfBirthColumn;
    @FXML
    private TableColumn<Learner, String> newLearnerEmailColumn;
    @FXML
    private TableColumn<Learner, String> newLearnerAddressColumn;
    @FXML
    private TableColumn<Learner, String> newLearnerContactNumberColumn;
    @FXML
    private TableColumn<Learner, Integer> newLearnerActivePackageIdColumn;
    @FXML
    private TableColumn<Learner, String> newLearnerIdNumberColumn;
    @FXML
    private TableColumn<Learner, String> newLearnerRegistrationDateColumn;
    @FXML
    private TableColumn<Learner, String> newLearnerGenderColumn;
    @FXML
    private TableColumn<Learner, String> newLearnerStatusColumn;

    @FXML
    private TableView<Learner> wroteTestTable;
    @FXML
    private TableColumn<Learner, Integer> wroteTestLearnerIdColumn;
    @FXML
    private TableColumn<Learner, String> wroteTestFullNameColumn;
    @FXML
    private TableColumn<Learner, String> wroteTestDateOfBirthColumn;
    @FXML
    private TableColumn<Learner, String> wroteTestEmailColumn;
    @FXML
    private TableColumn<Learner, String> wroteTestAddressColumn;
    @FXML
    private TableColumn<Learner, String> wroteTestContactNumberColumn;
    @FXML
    private TableColumn<Learner, Integer> wroteTestActivePackageIdColumn;
    @FXML
    private TableColumn<Learner, String> wroteTestIdNumberColumn;
    @FXML
    private TableColumn<Learner, String> wroteTestRegistrationDateColumn;
    @FXML
    private TableColumn<Learner, String> wroteTestGenderColumn;
    @FXML
    private TableColumn<Learner, String> wroteTestStatusColumn;

    @FXML
    private TableView<Learner> didNotAttendTable;
    @FXML
    private TableColumn<Learner, Integer> didNotAttendLearnerIdColumn;
    @FXML
    private TableColumn<Learner, String> didNotAttendFullNameColumn;
    @FXML
    private TableColumn<Learner, String> didNotAttendDateOfBirthColumn;
    @FXML
    private TableColumn<Learner, String> didNotAttendEmailColumn;
    @FXML
    private TableColumn<Learner, String> didNotAttendAddressColumn;
    @FXML
    private TableColumn<Learner, String> didNotAttendContactNumberColumn;
    @FXML
    private TableColumn<Learner, Integer> didNotAttendActivePackageIdColumn;
    @FXML
    private TableColumn<Learner, String> didNotAttendIdNumberColumn;
    @FXML
    private TableColumn<Learner, String> didNotAttendRegistrationDateColumn;
    @FXML
    private TableColumn<Learner, String> didNotAttendGenderColumn;
    @FXML
    private TableColumn<Learner, String> didNotAttendStatusColumn;

    @FXML
    private TableView<Learner> nextDayTable;
    @FXML
    private TableColumn<Learner, Integer> nextDayLearnerIdColumn;
    @FXML
    private TableColumn<Learner, String> nextDayFullNameColumn;
    @FXML
    private TableColumn<Learner, String> nextDayDateOfBirthColumn;
    @FXML
    private TableColumn<Learner, String> nextDayEmailColumn;
    @FXML
    private TableColumn<Learner, String> nextDayAddressColumn;
    @FXML
    private TableColumn<Learner, String> nextDayContactNumberColumn;
    @FXML
    private TableColumn<Learner, Integer> nextDayActivePackageIdColumn;
    @FXML
    private TableColumn<Learner, String> nextDayIdNumberColumn;
    @FXML
    private TableColumn<Learner, String> nextDayRegistrationDateColumn;
    @FXML
    private TableColumn<Learner, String> nextDayGenderColumn;
    @FXML
    private TableColumn<Learner, String> nextDayStatusColumn;

    @FXML
    private void initialize() {
        // Initialize columns for each TableView
        initializeTableColumns();

        // Load data into TableViews
        loadData();
    }

    private void initializeTableColumns() {
        // New Learners Today
        newLearnerLearnerIdColumn.setCellValueFactory(new PropertyValueFactory<>("learnerId"));
        newLearnerFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        newLearnerDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        newLearnerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        newLearnerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        newLearnerContactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        newLearnerActivePackageIdColumn.setCellValueFactory(new PropertyValueFactory<>("activePackageId"));
        newLearnerIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        newLearnerRegistrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        newLearnerGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        newLearnerStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Learners Who Wrote Test Today
        wroteTestLearnerIdColumn.setCellValueFactory(new PropertyValueFactory<>("learnerId"));
        wroteTestFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        wroteTestDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        wroteTestEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        wroteTestAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        wroteTestContactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        wroteTestActivePackageIdColumn.setCellValueFactory(new PropertyValueFactory<>("activePackageId"));
        wroteTestIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        wroteTestRegistrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        wroteTestGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        wroteTestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Learners Who Did Not Attend
        didNotAttendLearnerIdColumn.setCellValueFactory(new PropertyValueFactory<>("learnerId"));
        didNotAttendFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        didNotAttendDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        didNotAttendEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        didNotAttendAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        didNotAttendContactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        didNotAttendActivePackageIdColumn.setCellValueFactory(new PropertyValueFactory<>("activePackageId"));
        didNotAttendIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        didNotAttendRegistrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        didNotAttendGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        didNotAttendStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Learners Scheduled for Next Day
        nextDayLearnerIdColumn.setCellValueFactory(new PropertyValueFactory<>("learnerId"));
        nextDayFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nextDayDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        nextDayEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nextDayAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        nextDayContactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        nextDayActivePackageIdColumn.setCellValueFactory(new PropertyValueFactory<>("activePackageId"));
        nextDayIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        nextDayRegistrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        nextDayGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        nextDayStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        
          // New Learners Today
        newLearnerLearnerIdColumn.setCellValueFactory(new PropertyValueFactory<>("learnerId"));
        newLearnerFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        newLearnerDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        newLearnerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        newLearnerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        newLearnerContactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        newLearnerActivePackageIdColumn.setCellValueFactory(new PropertyValueFactory<>("activePackageId"));
        newLearnerIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        newLearnerRegistrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        newLearnerGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        newLearnerStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Learners Who Wrote Test Today
        wroteTestLearnerIdColumn.setCellValueFactory(new PropertyValueFactory<>("learnerId"));
        wroteTestFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        wroteTestDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        wroteTestEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        wroteTestAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        wroteTestContactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        wroteTestActivePackageIdColumn.setCellValueFactory(new PropertyValueFactory<>("activePackageId"));
        wroteTestIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        wroteTestRegistrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        wroteTestGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        wroteTestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Learners Who Did Not Attend
        didNotAttendLearnerIdColumn.setCellValueFactory(new PropertyValueFactory<>("learnerId"));
        didNotAttendFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        didNotAttendDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        didNotAttendEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        didNotAttendAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        didNotAttendContactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        didNotAttendActivePackageIdColumn.setCellValueFactory(new PropertyValueFactory<>("activePackageId"));
        didNotAttendIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        didNotAttendRegistrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        didNotAttendGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        didNotAttendStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Learners Scheduled for Next Day
        nextDayLearnerIdColumn.setCellValueFactory(new PropertyValueFactory<>("learnerId"));
        nextDayFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nextDayDateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        nextDayEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        nextDayAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        nextDayContactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        nextDayActivePackageIdColumn.setCellValueFactory(new PropertyValueFactory<>("activePackageId"));
        nextDayIdNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        nextDayRegistrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        nextDayGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        nextDayStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    
        
        
    }

    private void loadData() {
        // Load data into each TableView
        ObservableList<Learner> newLearners = DatabaseConnector.getNewLearnersForToday();
        newLearnersTable.setItems(newLearners);

        ObservableList<Learner> wroteTestLearners = DatabaseConnector.getLearnersWhoWroteTestToday();
        wroteTestTable.setItems(wroteTestLearners);

        ObservableList<Learner> didNotAttendLearners = DatabaseConnector.getLearnersWhoDidNotAttendToday();
        didNotAttendTable.setItems(didNotAttendLearners);

        ObservableList<Learner> nextDayLearners = DatabaseConnector.getLearnersScheduledForNextDay();
        nextDayTable.setItems(nextDayLearners);
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        screenManager = screenParent;
    }
    @Override
    public void runOnScreenChange() {
        }

    @Override
    public void cleanup() {
        
    }
}
