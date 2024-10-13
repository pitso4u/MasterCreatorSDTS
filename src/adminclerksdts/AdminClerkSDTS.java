
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public class AdminClerkSDTS extends Application {

//    private static final Logger LOGGER = LoggerFactory.getLogger(AdminClerkSDTS.class);
    public static String LoginScreenID = "LoginScreen";
    public static String LoginScreenFile = "LoginScreen.fxml";

    public static String DashboardScreenID = "DashboardScreen";
    public static String DashboardScreenFile = "/adminclerksdts/DashboardScreen.fxml";
    public static String HomeScreenID = "HomeScreen";
    public static String HomeScreenFile = "/adminclerksdts/HomeScreen.fxml";
    public static String SettingsScreenID = "SettingsScreen";
    public static String SettingsScreenFile = "/adminclerksdts/SettingsScreen.fxml";
    public static String SummaryReportScreenID = "SummaryReportScreen";
    public static String SummaryReportScreenFile = "/adminclerksdts/summaryReport/SummaryReport.fxml";

    public static String NewLearnerScreenID = "NewLearnerScreen";
    public static String NewLearnerScreenFile = "/adminclerksdts/learner/NewLearner.fxml";
    public static String ViewLearnerScreenID = "ViewLearnerScreen";
    public static String ViewLearnerScreenFile = "/adminclerksdts/learner/ViewLearner.fxml";
    public static String CreateScheduleScreenID = "CreateScheduleScreen";
    public static String CreateScheduleScreenFile = "/adminclerksdts/schedule/CreateSchedule.fxml";
    public static String CreateScheduleDialogScreenID = "CreateScheduleDialogScreen";
    public static String CreateScheduleDialogScreenFile = "/adminclerksdts/schedule/CreateScheduleDialog.fxml";
    public static String ViewScheduleScreenID = "ViewScheduleScreen";
    public static String ViewScheduleScreenFile = "/adminclerksdts/schedule/ViewSchedules.fxml";
//    public static String NewInstructorScreenID = "NewInstructorScreen";
//    public static String NewInstructorFile = "/adminclerksdts/instructor/NewInstructor.fxml";
//    public static String ViewInstructorScreenID = "ViewInstructorScreen";
//    public static String ViewInstructorScreenFile = "/adminclerksdts/instructor/ViewInstructor.fxml";
    public static String NewProgressScreenID = "NewProgressScreen";
    public static String NewProgressScreenFile = "/adminclerksdts/progress/NewProgress.fxml";
    public static String ViewProgressScreenID = "ViewProgressScreen";
    public static String ViewProgressScreenFile = "/adminclerksdts/progress/ViewProgress.fxml";
    public static String QuestionsScreenID = "NewQuestionScreen";
    public static String QuestionsScreenFile = "/adminclerksdts/question/Questions.fxml";
    public static String ViewQuestionsScreenID = "ViewQuestionsScreen";
    public static String ViewQuestionsScreenFile = "/adminclerksdts/question/ViewQuestions.fxml";

    private ScreenManager mainContainer;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/smartdrive_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Soetsang@144156";

    @Override
    public void start(Stage primaryStage) {

        try {
            mainContainer = new ScreenManager();

            // Load other screens
//        mainContainer.loadScreen(LoginScreenID, LoginScreenFile);
            mainContainer.loadScreen(DashboardScreenID, DashboardScreenFile);
            mainContainer.loadScreen(HomeScreenID, HomeScreenFile);
            mainContainer.loadScreen(SettingsScreenID, SettingsScreenFile);
            mainContainer.loadScreen(SummaryReportScreenID, SummaryReportScreenFile);
            mainContainer.loadScreen(NewLearnerScreenID, NewLearnerScreenFile);
            mainContainer.loadScreen(ViewLearnerScreenID, ViewLearnerScreenFile);
            mainContainer.loadScreen(CreateScheduleScreenID, CreateScheduleScreenFile);
            mainContainer.loadScreen(CreateScheduleDialogScreenID, CreateScheduleDialogScreenFile);
            mainContainer.loadScreen(ViewScheduleScreenID, ViewScheduleScreenFile);
//        mainContainer.loadScreen(NewInstructorScreenID, NewInstructorFile);
//        mainContainer.loadScreen(ViewInstructorScreenID, ViewInstructorScreenFile);
            mainContainer.loadScreen(NewProgressScreenID, NewProgressScreenFile);
            mainContainer.loadScreen(ViewProgressScreenID, ViewProgressScreenFile);
            mainContainer.loadScreen(QuestionsScreenID, QuestionsScreenFile);
            mainContainer.loadScreen(ViewQuestionsScreenID, ViewQuestionsScreenFile);

// Set the initial screen (e.g., Login screen)
//        mainContainer.setScreen(AdminClerkSDTS.LoginScreenID);
           
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LoginScreenFile));
            Node loginScreen = loader.load();
            LoginScreen loginController = loader.getController();
            loginController.setScreenParent(mainContainer);  // Use mainContainer here

            // Add the login screen to the ScreenManager
            mainContainer.addScreen(LoginScreenID, loginScreen);

            // Set the initial screen to the login screen
            mainContainer.setScreen(LoginScreenID);
// Link your CSS file
            String cssFilePath = getClass().getResource("custom-yellowstyles.css").toExternalForm();
            mainContainer.getStylesheets().add(cssFilePath);

// Set the stage to full screen
            primaryStage.setMaximized(true);
//        mainContainer.setStyle("-fx-background-color: #ffed4b;");

// Get the primary screen
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

// Calculate the desired scene size based on screen resolution
            double screenWidth = bounds.getWidth();
            double screenHeight = bounds.getHeight();
            double sceneWidth = screenWidth * 0.8; // 80% of screen width
            double sceneHeight = screenHeight * 0.8; // 80% of screen height
            System.out.println("sceneWidth:" + sceneWidth);
            System.out.println("sceneHeight:" + sceneHeight);

            primaryStage.setScene(new Scene(mainContainer, sceneWidth, sceneHeight));
            primaryStage.setTitle("SmartDrive Training Suite");
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminClerkSDTS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
