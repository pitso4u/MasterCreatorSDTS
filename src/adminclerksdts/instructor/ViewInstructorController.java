/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts.instructor;

import adminclerksdts.ControlledScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewInstructorController implements Initializable, ControlledScreen {

    @FXML
    private TableView<Instructor> instructorTable;

    private TableColumn<Instructor, Integer> idColumn;

    private TableColumn<Instructor, String> fullNameColumn;

    private TableColumn<Instructor, String> contactNumberColumn;

    private TableColumn<Instructor, String> emailColumn;

    private TableColumn<Instructor, LocalDate> hireDateColumn;

    private TableColumn<Instructor, String> specializationColumn;

    private ScreenManager screenManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        // Set up the instructor table columns
//      idColumn.setCellValueFactory(new PropertyValueFactory<>("instructorID"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        contactNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        // Load instructor data into the table
        loadInstructorData();
    }

    private void loadInstructorData() {
        // Implement logic to fetch instructor data from the database and populate the table
        // For example, you can use a DatabaseConnector class to retrieve a list of instructors
        try {
            List<Instructor> instructors = DatabaseConnector.getInstructors();
            instructorTable.getItems().setAll(instructors);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
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
}
