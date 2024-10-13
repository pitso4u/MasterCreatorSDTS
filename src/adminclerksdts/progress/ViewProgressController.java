package adminclerksdts.progress;

import adminclerksdts.ControlledScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewProgressController implements Initializable, ControlledScreen {

    @FXML
    private TableView<Progress> progressTableView;

    @FXML
    private TableColumn<Progress, Integer> progressIDColumn;

    // Change learnerIDColumn to display full name (String)
    @FXML
    private TableColumn<Progress, String> learnerFullNameColumn;

    @FXML
    private TableColumn<Progress, Integer> instructorIDColumn;

    @FXML
    private TableColumn<Progress, Double> quizScoreColumn;

    @FXML
    private TableColumn<Progress, String> completionStatusColumn;

    @FXML
    private TableColumn<Progress, String> timestampColumn;

    @FXML
    private TableColumn<Progress, String> instructorNotesColumn;

    private ScreenManager screenManager;

    // ObservableList to hold progress data
    private ObservableList<Progress> progressData = FXCollections.observableArrayList();
    @FXML
    private TextField searchField;
 private FilteredList<Progress> filteredProgress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the TableView columns
        progressIDColumn.setCellValueFactory(new PropertyValueFactory<>("progressID"));
        
        // Set learnerFullNameColumn to display full name instead of learner ID
        learnerFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("learnerFullName"));
        
        instructorIDColumn.setCellValueFactory(new PropertyValueFactory<>("instructorID"));
        quizScoreColumn.setCellValueFactory(new PropertyValueFactory<>("quizScore"));
        completionStatusColumn.setCellValueFactory(new PropertyValueFactory<>("completionStatus"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        instructorNotesColumn.setCellValueFactory(new PropertyValueFactory<>("instructorNotes"));

        // Set the TableView items to the ObservableList
        progressTableView.setItems(progressData);

        // Load progress data into the table
        loadProgressData();
    }

    private void loadProgressData() {
        // Implement logic to fetch progress data from the database, including learner's full name
        
        filteredProgress = new FilteredList<>(FXCollections.observableArrayList(DatabaseConnector.getAllProgress()), p -> true);
        progressTableView.setItems(filteredProgress);
    
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
        // Navigate back to the previous screen
        screenManager.setScreen("DashboardScreen");
    }

    @FXML
    private void LoadProgress(ActionEvent event) {
        progressTableView.setItems(DatabaseConnector.getAllProgress());
    }

    @FXML
    private void EditProgress(ActionEvent event) {
    }

    @FXML
    private void DeleteProgress(ActionEvent event) {
    }

    @FXML
  private void handleSearchAction() {
     String query = searchField.getText().toLowerCase();
     filteredProgress.setPredicate(progress -> {
         if (query == null || query.isEmpty()) {
             return true; // No filter
         }

         // Compare learner's full name and quiz score with the query
         return progress.getLearnerFullName().getValue().toLowerCase().contains(query) ||
                String.valueOf(progress.getQuizScore()).contains(query);
     });
 }

}
