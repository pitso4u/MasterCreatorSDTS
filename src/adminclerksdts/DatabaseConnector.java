/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts;

import adminclerksdts.instructor.Instructor;
import adminclerksdts.learner.Learner;
import adminclerksdts.progress.Progress;
import adminclerksdts.question.Question;
import adminclerksdts.schedule.Schedule;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseConnector {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/smartdrive_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Soetsang@144156";
// Method to save learner data to the database
// Method to save learner data to the database
public static int saveLearner(Learner learner) {
    String query = "INSERT INTO learners (full_name, address, username, password, "
                 + "date_of_birth, contact_number, email, active_package_id, id_number, "
                 + "registration_date, gender, emergency_contact, preferred_language, "
                 + "previous_experience, package_type, notes, status) VALUES "
                 + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

        pstmt.setString(1, learner.getFullName());
        pstmt.setString(2, learner.getAddress());
        pstmt.setString(3, learner.getUsername());
        pstmt.setString(4, learner.getPassword());
        pstmt.setDate(5, java.sql.Date.valueOf(learner.getDateOfBirth()));
        pstmt.setString(6, learner.getContact_Number());
        pstmt.setString(7, learner.getEmail());

        // Handle active_package_id
        if (learner.getActive_package_id()!= 0) {
            pstmt.setInt(8, learner.getActive_package_id());
        } else {
            pstmt.setNull(8, java.sql.Types.INTEGER);
        }

        pstmt.setString(9, learner.getIdNumber());

        // Convert registrationDate to java.sql.Date
        if (learner.getRegistrationDate() != null) {
            pstmt.setDate(10, java.sql.Date.valueOf(learner.getRegistrationDate()));
        } else {
            pstmt.setNull(10, java.sql.Types.DATE);
        }

        pstmt.setString(11, learner.getGender());
        pstmt.setString(12, learner.getEmergencyContact());
        pstmt.setString(13, learner.getPreferredLanguage());
        pstmt.setBoolean(14, learner.isPreviousExperience());
        pstmt.setString(15, learner.getPackageType());
        pstmt.setString(16, learner.getNotes());
        pstmt.setString(17, learner.getStatus());

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating learner failed, no rows affected.");
        }

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating learner failed, no ID obtained.");
            }
        }
    } catch (SQLException e) {
        if ("23505".equals(e.getSQLState())) { // Unique violation code
            System.out.println("Error: Duplicate username.");
        }
        e.printStackTrace();
    }
    return -1;
}

    public static void updateLearner(Learner learner) {
        String query = "UPDATE learners SET full_name = ?, address = ?, username = ?, password = ?, date_of_birth = ?, email = ?, active_package_id = ?, id_number = ?, registration_date = ?, gender = ?, emergency_contact = ?, preferred_language = ?, previous_experience = ?, package_type = ?, notes = ?, status = ? WHERE learner_id = ?";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, learner.getFullName());
            pstmt.setString(2, learner.getAddress());
            pstmt.setString(3, learner.getUsername());
            pstmt.setString(4, learner.getPassword());
            pstmt.setDate(5, java.sql.Date.valueOf(learner.getDateOfBirth()));
            pstmt.setString(6, learner.getEmail());
            pstmt.setInt(7, learner.getActive_package_id());
            pstmt.setString(8, learner.getIdNumber());
            pstmt.setDate(9, java.sql.Date.valueOf(learner.getRegistrationDate()));
            pstmt.setString(10, learner.getGender());
            pstmt.setString(11, learner.getEmergencyContact());
            pstmt.setString(12, learner.getPreferredLanguage());
            pstmt.setBoolean(13, learner.isPreviousExperience());
            pstmt.setString(14, learner.getPackageType());
            pstmt.setString(15, learner.getNotes());
            pstmt.setString(16, learner.getStatus());
            pstmt.setInt(17, learner.getLearnerId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllLearnerNames() {
        List<String> learnerNames = new ArrayList<>();
        String sql = "SELECT full_name FROM learners";

        try ( Connection conn = getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("full_name");
                learnerNames.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return learnerNames;
    }

    public static ObservableList<Learner> getLearners() {
        ObservableList<Learner> learners = FXCollections.observableArrayList();

        try ( Connection conn = getConnection()) {
            String query = "SELECT * FROM learners";

            try ( PreparedStatement pstmt = conn.prepareStatement(query);  ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Learner learner = new Learner(
                            rs.getInt("learner_id"),
                            rs.getString("full_name"),
                            rs.getString("address"),
                            rs.getString("password"),
                            rs.getString("username"),
                            rs.getDate("date_of_birth").toLocalDate(),
                            rs.getString("contact_number"),
                            rs.getString("email"),
                            rs.getInt("active_package_id"),
                            rs.getString("id_number"),
                            rs.getString("gender"),
                            rs.getString("emergency_contact"),
                            rs.getString("preferred_language"),
                            rs.getString("package_type"),
                            rs.getBoolean("previous_experience"),
                            rs.getString("notes"),
                            rs.getString("status"),
                            rs.getDate("registration_date").toLocalDate()
                    );

                    learners.add(learner);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return learners;
    }

   public static boolean deleteLearner(int learnerID) {
    try (Connection connection = getConnection()) {
        String query = "DELETE FROM Learners WHERE learner_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, learnerID);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 1) {
                System.out.println("Successfully deleted " + affectedRows + " learner record.");
                return true;
            } else {
                System.err.println("Failed to delete learner record. No rows affected.");
                return false;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Handle or log the exception as needed
        return false;
    }
}

    public static List<Question> getQuestions(String tableName) throws SQLException {
        List<Question> questions = new ArrayList<>();

        // Try-with-resources ensures the connection is closed automatically
        try ( Connection connection = getConnection()) {
            String query = "SELECT * FROM " + tableName;

            // Prepare and execute the query
            try ( PreparedStatement preparedStatement = connection.prepareStatement(query);  ResultSet resultSet = preparedStatement.executeQuery()) {

                // Iterate through the result set and create Question objects
                while (resultSet.next()) {
                    int questionID = resultSet.getInt("question_id");
                    String question_text = resultSet.getString("question_text");
                    String image_url = resultSet.getString("image_url");
                    Boolean has_image = resultSet.getBoolean("has_image");
                    String option1 = resultSet.getString("option1");
                    String option2 = resultSet.getString("option2");
                    String option3 = resultSet.getString("option3");
                    int correct_option = resultSet.getInt("correct_option");
                    questions.add(new Question(questionID, tableName, question_text, image_url, has_image,
                            option1, option2, option3, correct_option));

                }
                return questions;
            }
        }
    }

    // Method to save the image path to the database
    public static void saveImagePath(String ImageTable, String imagePath) throws SQLException {
        String sql = "INSERT INTO " + ImageTable + " (image_url, has_image) VALUES (?, ?)";

        try ( Connection connection = getConnection();  PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, imagePath);
            statement.setBoolean(2, true);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 1) {
                System.out.println("Image path saved to the database: " + imagePath);
            } else {
                System.err.println("Failed to save image path to the database.");
            }
        }
    }

    public static List<String> getAvailableTableNames() {
        return Arrays.asList("Rules", "Signs", "Controls");
    }

    public static int saveQuestion(Question question, String tableName) throws SQLException {
        int generatedQuestionID = 0;

        // Define the SQL INSERT statement with columns in the correct order
        // Define the SQL INSERT statement
        String insertQuery = "INSERT INTO " + tableName + " (question_text, image_url, has_image, option1, option2, option3,  correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try ( Connection connection = getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters in the order of the SQL query with the requested swaps
            preparedStatement.setString(1, question.questionTextProperty().get()); // Swap: option1 with question_text
            preparedStatement.setString(2, question.imageURLProperty().get()); // image_url (no change)
            preparedStatement.setBoolean(3, question.hasImageProperty().get()); // has_image (no change)
            preparedStatement.setString(4, question.option1Property().get()); // Swap: option2 with option1
            preparedStatement.setString(5, question.option2Property().get()); // Swap: option3 with option2
            preparedStatement.setString(6, question.option3Property().get()); // Swap: option4 with option3
            preparedStatement.setInt(7, question.correctOptionProperty().get()); // correct_option (no change)

            // Log values before execution
            System.out.println("Inserting into " + tableName);
            System.out.println("Image URL: " + question.imageURLProperty().get());
            System.out.println("Has Image: " + question.hasImageProperty().get());
            System.out.println("questionText: " + question.questionTextProperty().get());
            System.out.println("option1: " + question.option1Property().get());
            System.out.println("option2: " + question.option2Property().get());
            System.out.println("option3: " + question.option3Property().get());
            System.out.println("Correct Option: " + question.correctOptionProperty().get());

            // Execute the query
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try ( ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedQuestionID = generatedKeys.getInt(1);
                    }
                }
            }
        }

        return generatedQuestionID;

    }

    public static void updateEditQuestion(Question question) throws SQLException {
        try ( Connection connection = getConnection()) {
            String query = "UPDATE " + question.getTableName() + " SET question_text = ?, "
                    + "image_url = ?, has_image = ?, option1 = ?, option2 = ?, option3 = ?, "
                    + "correct_option = ? WHERE question_id = ?";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, question.questionTextProperty().get());
                preparedStatement.setString(2, question.imageURLProperty().get());
                preparedStatement.setBoolean(3, question.hasImageProperty().get());
                preparedStatement.setString(4, question.option1Property().get());
                preparedStatement.setString(5, question.option2Property().get());
                preparedStatement.setString(6, question.option3Property().get());
                preparedStatement.setInt(7, question.correctOptionProperty().get());
                preparedStatement.setInt(8, question.questionIDProperty().get());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 1) {
                    System.out.println("Successfully updated " + affectedRows + " question record.");
                } else {
                    System.err.println("Failed to update question record. No rows affected.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public static boolean isQuestionDuplicate(Question question, String tableName, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE question_text = ? AND correct_option = ?";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, question.questionTextProperty().get());
            preparedStatement.setInt(2, question.correctOptionProperty().get());

            try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }

    public static void updateImageQuestion(Question question) throws SQLException {

        Connection connection = getConnection();
        String query = "UPDATE " + question.getTableName() + " SET image_url = ? WHERE question_id = ?";

        System.out.println("SQL Query: " + query); // Print the SQL query

        try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, question.imageURLProperty().get());
            preparedStatement.setInt(2, question.questionIDProperty().get());

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("Updating question with ID: " + question.questionIDProperty().get());

            System.out.println("Affected Rows: " + affectedRows); // Print the number of affected rows

            if (affectedRows == 1) {
                System.out.println("Successfully updated " + affectedRows + " question record with the image URL: " + question.imageURLProperty().get());
            } else {
                System.err.println("Failed to update question record with image URL. No rows affected.");
            }
        }

    }

    public static void deleteQuestion(String tableName, int questionID) throws SQLException {
        try ( Connection connection = getConnection();  PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE question_id = ?")) {

            // Set the question ID parameter in the query
            preparedStatement.setInt(1, questionID);

            // Execute the delete query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
            throw new SQLException("Error deleting question from table: " + tableName, e);
        }
    }

    public static void saveInstructor(Instructor instructor) {
        try ( Connection connection = getConnection()) {
            String query = "INSERT INTO Instructors (full_name, contact_number, email, hire_date, specialization) "
                    + "VALUES (?, ?, ?, ?, ?)";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, instructor.fullNameProperty().get());
                preparedStatement.setString(2, instructor.contactNumberProperty().get());
                preparedStatement.setString(3, instructor.emailProperty().get());
                preparedStatement.setDate(4, Date.valueOf(instructor.hireDateProperty().get()));
                preparedStatement.setString(5, instructor.specializationProperty().get());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public static List<Instructor> getInstructors() throws SQLException {
        List<Instructor> instructors = new ArrayList<>();
        try ( Connection connection = getConnection()) {
            String query = "SELECT * FROM Instructors";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query);  ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Instructor instructor = new Instructor(
                            resultSet.getInt("instructor_id"),
                            resultSet.getString("full_name"),
                            resultSet.getString("contact_number"),
                            resultSet.getString("email"),
                            resultSet.getDate("hire_date").toLocalDate(),
                            resultSet.getString("specialization")
                    );

                    instructors.add(instructor);
                }
            }
        }
        return instructors;
    }

    public static void saveProgress(Progress progress) {
        try ( Connection connection = getConnection()) {
            String query = "INSERT INTO progress_tracking (learner_id, instructor_id, quiz_score, "
                    + "completion_status, timestamp, instructor_notes) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, progress.learnerIDProperty().get());
                preparedStatement.setInt(2, progress.instructorIDProperty().get());
                preparedStatement.setDouble(3, progress.quizScoreProperty().get());
                preparedStatement.setBoolean(4, progress.completionStatusProperty().get());
                preparedStatement.setTimestamp(5, Timestamp.valueOf(progress.timestampProperty().get()));
                preparedStatement.setString(6, progress.instructorNotesProperty().get());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public static List<Progress> getProgress(int learnerID) {
        List<Progress> progressList = new ArrayList<>();
        try ( Connection connection = getConnection()) {
            String query = "SELECT * FROM progress_tracking WHERE learner_id = ?";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, learnerID);

                try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Progress progress = new Progress(
                                resultSet.getInt("progress_id"),
                                resultSet.getInt("learner_id"),
                                resultSet.getInt("instructor_id"),
                                resultSet.getDouble("quiz_score"),
                                resultSet.getBoolean("completion_status"),
                                resultSet.getTimestamp("timestamp").toLocalDateTime(),
                                resultSet.getString("instructor_notes"), null
                        );

                        progressList.add(progress);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return progressList;
    }

    public static void updateProgress(Progress progress) {
        try ( Connection connection = getConnection()) {
            String query = "UPDATE progress_tracking SET quiz_score = ?, completion_status = ?, "
                    + "timestamp = ?, instructor_notes = ? "
                    + "WHERE progress_id = ?";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, progress.quizScoreProperty().get());
                preparedStatement.setBoolean(2, progress.completionStatusProperty().get());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(progress.timestampProperty().get()));
                preparedStatement.setString(4, progress.instructorNotesProperty().get());
                preparedStatement.setInt(5, progress.progressIDProperty().get());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public static void associateInstructorWithProgress(int progressID, int instructorID) {
        try ( Connection connection = getConnection()) {
            String query = "UPDATE progress_tracking SET instructor_id = ? WHERE progress_id = ?";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, instructorID);
                preparedStatement.setInt(2, progressID);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public static List<Progress> getProgressForInstructor(int instructorID) {
        List<Progress> progressList = new ArrayList<>();
        try ( Connection connection = getConnection()) {
            String query = "SELECT * FROM progress_tracking WHERE instructor_id = ?";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, instructorID);

                try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Progress progress = new Progress(
                                resultSet.getInt("progress_id"),
                                resultSet.getInt("learner_id"),
                                resultSet.getInt("instructor_id"),
                                resultSet.getDouble("quiz_score"),
                                resultSet.getBoolean("completion_status"),
                                resultSet.getTimestamp("timestamp").toLocalDateTime(),
                                resultSet.getString("instructor_notes"), null
                        );

                        progressList.add(progress);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return progressList;
    }

    public static List<Progress> getProgressForLearner(int learnerID) {
        List<Progress> progressList = new ArrayList<>();
        try ( Connection connection = getConnection()) {
            String query = "SELECT * FROM progress_tracking WHERE learner_id = ?";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, learnerID);

                try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Progress progress = new Progress(
                                resultSet.getInt("progress_id"),
                                resultSet.getInt("learner_id"),
                                resultSet.getInt("instructor_id"),
                                resultSet.getDouble("quiz_score"),
                                resultSet.getBoolean("completion_status"),
                                resultSet.getTimestamp("timestamp").toLocalDateTime(),
                                resultSet.getString("instructor_notes"), null
                        );

                        progressList.add(progress);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return progressList;
    }

    // Method to establish a database connection
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

   public static ObservableList<Progress> getAllProgress() {
    ObservableList<Progress> progressList = FXCollections.observableArrayList(); // Initialize ObservableList

    try (Connection connection = getConnection()) {
        // Updated query to join with learners table and fetch full_name
        String query = "SELECT p.progress_id, p.learner_id, l.full_name, p.instructor_id, p.quiz_score, p.completion_status, p.timestamp, p.notes FROM progress_tracking p    JOIN learners l ON p.learner_id = l.learner_id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Use full_name instead of learner_id in Progress object
                    Progress progress = new Progress(
                        resultSet.getInt("progress_id"),
                        resultSet.getString("full_name"),  // Fetch the full_name from the joined table
                        resultSet.getInt("instructor_id"),
                        resultSet.getDouble("quiz_score"),
                        resultSet.getBoolean("completion_status"),
                        resultSet.getTimestamp("timestamp").toLocalDateTime(),
                        resultSet.getString("notes"), null
                    );

                    progressList.add(progress);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Handle or log the exception as needed
    }
    return progressList;
}

    public static ObservableList<Schedule> getSchedules() {
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();
        try ( Connection connection = getConnection()) {
            String query = "SELECT * FROM schedule";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(query);  ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve data from resultSet and construct Schedule object
                    int scheduleId = resultSet.getInt("schedule_id");
                    int learnerId = resultSet.getInt("learner_id");
                    LocalDate sessionDate = resultSet.getDate("session_date").toLocalDate();
                    String sessionStartTime = resultSet.getString("session_start_time");
                    String sessionEndTime = resultSet.getString("session_end_time");
                    Schedule schedule = new Schedule(scheduleId, learnerId, sessionDate, LocalTime.parse(sessionStartTime), LocalTime.parse(sessionEndTime));

                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return schedules;
    }

    public static void saveSchedule(Schedule schedule) {
        String insertSQL = "INSERT INTO schedule (learner_id, session_date, session_start_time, session_end_time) VALUES (?, ?, ?, ?)";

        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setInt(1, schedule.getLearnerId());
            pstmt.setDate(2, java.sql.Date.valueOf(schedule.getSessionDate()));

            // Convert LocalTime to SQL Time
            LocalTime startTime = schedule.getSessionStartTime();
            LocalTime endTime = schedule.getSessionEndTime();

            // Ensure the time is not null and in the correct format
            if (startTime != null && endTime != null) {
                pstmt.setTime(3, Time.valueOf(startTime));
                pstmt.setTime(4, Time.valueOf(endTime));
            } else {
                throw new IllegalArgumentException("Invalid time values");
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., show an error dialog)
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // Handle illegal argument exception (e.g., show an error dialog)
        }
    }

    public static int getLearnerIdByName(String learnerName) {
        int learnerId = -1; // Default value indicating failure

        try ( Connection connection = getConnection()) {
            String query = "SELECT learner_id FROM Learners WHERE full_name = ?";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, learnerName);

                try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        learnerId = resultSet.getInt("learner_id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }

        return learnerId;
    }

    public static boolean isSlotAvailable(LocalDate sessionDate, LocalTime sessionStartTime, LocalTime sessionEndTime) {
        try ( Connection connection = getConnection()) {
            String query = "SELECT COUNT(*) FROM schedule WHERE session_date = ? AND session_start_time = ? AND session_end_time = ?";

            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, Date.valueOf(sessionDate));
                preparedStatement.setTime(2, Time.valueOf(sessionStartTime));
                preparedStatement.setTime(3, Time.valueOf(sessionEndTime));

                try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                    resultSet.next();
                    return resultSet.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getLearnersForDate(int maxSeats, LocalDate date) {
        List<String> learnersForDate = new ArrayList<>();
        String query = "SELECT learners.full_name, schedule.session_date, schedule.session_start_time "
                + "FROM schedule "
                + "INNER JOIN learners ON schedule.learner_id = learners.learner_id "
                + "WHERE schedule.session_date = ? "
                + "GROUP BY learners.full_name, schedule.session_date, schedule.session_start_time "
                + // Include session_date and session_start_time in GROUP BY
                "HAVING COUNT(*) < ?;"; // Assuming the number of seats is passed as a parameter

        try ( Connection connection = getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setInt(2, maxSeats);

            try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String learnerName = resultSet.getString("full_name");
                    Date sessionDate = resultSet.getDate("session_date");
                    String startTime = resultSet.getString("session_start_time");

                    learnersForDate.add(learnerName + " - Date: " + sessionDate.toString() + ", Time: " + startTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }

        return learnersForDate;
    }

    public static int getNumberOfLearnersForSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        String query = "SELECT COUNT(*) FROM Schedule WHERE session_date = ? AND session_start_time = ? AND session_end_time = ?";

        try ( Connection conn = getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setTime(2, Time.valueOf(startTime));
            stmt.setTime(3, Time.valueOf(endTime));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getLearnerNameById(int learnerId) {
        String learnerName = null;

        try ( Connection connection = getConnection()) {
            String query = "SELECT full_name FROM learners WHERE learner_id = ?";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, learnerId);
                try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        learnerName = resultSet.getString("full_name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }

        return learnerName;
    }

    public static void deleteSchedule(int scheduleId) {
        try ( Connection connection = getConnection()) {
            String query = "DELETE FROM schedule WHERE schedule_id = ?";
            try ( PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, scheduleId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    public static boolean isNextAvailableBookingDay(int learnerId, LocalDate currentDate) {
        // Get the learner's next scheduled date
        LocalDate nextScheduledDate = getNextScheduledDateForLearner(learnerId);

        if (nextScheduledDate == null) {
            return true; // No next date found, so it's available
        }

        // Check if the next scheduled date is in the future
        return nextScheduledDate.isAfter(currentDate);
    }

    public static List<String> getClassesForDate(LocalDate date) {
        List<String> classes = new ArrayList<>();
        String sql = "SELECT * FROM schedule WHERE session_date = ?";

        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Assuming the schedules table has a class name or learner name column
                String learnerName = getLearnerNameById(rs.getInt("learner_id"));
                LocalTime startTime = rs.getTime("session_start_time").toLocalTime();
                LocalTime endTime = rs.getTime("session_end_time").toLocalTime();
                String classInfo = learnerName + ": " + startTime + " - " + endTime;

                classes.add(classInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static LocalDate getNextScheduledDateForLearner(int learnerId) {
        // Query to get the next scheduled date for a learner
        String query = "SELECT MIN(session_date) FROM Schedule WHERE learner_id = ? AND session_date > CURRENT_DATE";

        try ( Connection conn = getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, learnerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDate(1).toLocalDate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//public static int getNumberOfLearnersForSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
//    String query = "SELECT COUNT(*) FROM Schedule WHERE sessionDate = ? AND sessionStartTime = ? AND sessionEndTime = ?";
//
//    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
//        stmt.setDate(1, Date.valueOf(date));
//        stmt.setTime(2, Time.valueOf(startTime));
//        stmt.setTime(3, Time.valueOf(endTime));
//        ResultSet rs = stmt.executeQuery();
//
//        if (rs.next()) {
//            return rs.getInt(1);
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//    return 0;
//}

    public static boolean isDateFullyBooked(LocalDate date) {
        // Define your time slots manually
        List<LocalTime> timeSlots = Arrays.asList(
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0)
        );

        for (LocalTime startTime : timeSlots) {
            LocalTime endTime = calculateEndTimeSlot(startTime);
            int numberOfLearners = getNumberOfLearnersForSlot(date, startTime, endTime);

            if (numberOfLearners < 20) {
                return false; // Found an available slot
            }
        }
        return true; // All slots are fully booked
    }

    // Add other methods and logic here
    private static LocalTime calculateEndTimeSlot(LocalTime startTime) {
        if (startTime.equals(LocalTime.of(8, 0))) {
            return LocalTime.of(10, 0);
        } else if (startTime.equals(LocalTime.of(10, 0))) {
            return LocalTime.of(12, 0);
        } else if (startTime.equals(LocalTime.of(14, 0))) {
            return LocalTime.of(16, 0);
        } else if (startTime.equals(LocalTime.of(16, 0))) {
            return LocalTime.of(18, 0);
        } else {
            throw new IllegalArgumentException("Unknown start time");
        }
    }

// Method to get package details by learner ID
    public static PackageDetails getPackageDetailsByLearnerId(int learnerId) {
        String query = "SELECT * FROM learner_packages WHERE learner_id = ?";
        try ( Connection connection = getConnection();  PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, learnerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                PackageDetails packageDetails = new PackageDetails();
                packageDetails.setLearnerId(rs.getInt("learner_id"));
                packageDetails.setPackageId(rs.getInt("package_id"));
                packageDetails.setStartDate(rs.getDate("start_date").toLocalDate());
                packageDetails.setTestsRemaining(rs.getInt("tests_remaining"));
                packageDetails.setDaysRemaining(rs.getInt("days_remaining"));
                packageDetails.setPackageCompleted(rs.getBoolean("package_completed"));
                packageDetails.setEndDate(rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);

                return packageDetails;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to update learner package
    public static void updateLearnerPackage(int learnerId, int remainingDays) {
        String query = "UPDATE learner_packages SET days_remaining = ? WHERE learner_id = ?";
        try ( Connection connection = getConnection();  PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, remainingDays);
            pstmt.setInt(2, learnerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getClassesForSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        ObservableList<String> classes = FXCollections.observableArrayList();

        try {
            // Assuming you have a connection object `conn` to your database
            Connection conn = getConnection();  // Replace with your actual method to get DB connection
            String query = "SELECT learner_id FROM schedule WHERE session_date = ? AND session_start_time = ? AND session_end_time = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setTime(2, java.sql.Time.valueOf(startTime));
            stmt.setTime(3, java.sql.Time.valueOf(endTime));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                classes.add("Name: " + getLearnerNameById(rs.getInt("learner_id")));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return classes;
    }

    public static int getBookedClassesCount(int learnerId) {
        String query = "SELECT COUNT(*) FROM schedule WHERE learner_id = ?";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, learnerId);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getLearnerPackageLimit(int learnerId) {
        String query = "SELECT tests_remaining FROM learner_packages WHERE learner_id = ?";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, learnerId);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tests_remaining");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static LocalDate getLastBookedDate(int learnerId) {
        String query = "SELECT MAX(session_date) AS session_date FROM schedule WHERE learner_id = ?";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, learnerId);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Date lastDate = rs.getDate("session_date");
                    return lastDate != null ? lastDate.toLocalDate() : null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getLearnersForSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<String> learners = new ArrayList<>();
        String sql = "SELECT l.full_name FROM learners l "
                + "JOIN schedule s ON l.learner_id = s.learner_id "
                + "WHERE s.session_date = ? AND s.session_start_time = ? AND s.session_end_time = ?";

        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(date));
            pstmt.setTime(2, java.sql.Time.valueOf(startTime));
            pstmt.setTime(3, java.sql.Time.valueOf(endTime));

            try ( ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    learners.add(rs.getString("full_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learners;
    }

    public static List<Package> getAllPackages() {
        List<Package> packages = new ArrayList<>();
        String query = "SELECT * FROM packages";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query);  ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                packages.add(new Package(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration_days"),
                        rs.getInt("total_tests")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    public static Package getPackageById(int packageId) {
        String query = "SELECT * FROM packages WHERE id = ?";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, packageId);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Package(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("duration_days"),
                            rs.getInt("total_tests")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveLearnerPackage(LearnerPackage learnerPackage) {
        String query = "INSERT INTO learner_packages (learner_id, package_id, start_date, tests_remaining, days_remaining, package_completed) VALUES (?, ?, ?, ?, ?, ?)";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, learnerPackage.getLearnerId());
            pstmt.setInt(2, learnerPackage.getPackageId());
            pstmt.setDate(3, java.sql.Date.valueOf(learnerPackage.getStartDate()));
            pstmt.setInt(4, learnerPackage.getTestsRemaining());
            pstmt.setInt(5, learnerPackage.getDaysRemaining());
            pstmt.setBoolean(6, learnerPackage.isPackageCompleted());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUsernameTaken(String username) {
        String query = "SELECT COUNT(*) FROM learners WHERE username = ?";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateLearnerPackage(LearnerPackage learnerPackage) throws SQLException {
        String query = "UPDATE learner_packages SET package_id = ?, start_date = ?, tests_remaining = ?, days_remaining = ?, package_completed = ? WHERE learner_packages_id = ?";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, learnerPackage.getPackageId());
            pstmt.setDate(2, java.sql.Date.valueOf(learnerPackage.getStartDate()));
            pstmt.setInt(3, learnerPackage.getTestsRemaining());
            pstmt.setInt(4, learnerPackage.getDaysRemaining());
            pstmt.setBoolean(5, learnerPackage.isPackageCompleted());
            pstmt.setInt(6, learnerPackage.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LearnerPackage getLearnerPackageByLearnerId(int learnerId) {
        String query = "SELECT * FROM learner_packages WHERE learner_id = ?";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, learnerId);
            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new LearnerPackage(
                            rs.getInt("learner_packages_id"),
                            rs.getInt("learner_id"),
                            rs.getInt("package_id"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getInt("tests_remaining"),
                            rs.getInt("days_remaining"),
                            rs.getBoolean("package_completed"),
                            rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getRemainingLessons(int learnerId) {
        String sql = "SELECT lp.tests_remaining FROM learner_packages lp "
                + "WHERE lp.learner_id = ? AND lp.package_completed = false "
                + "ORDER BY lp.start_date DESC LIMIT 1";

        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, learnerId);

            try ( ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tests_remaining");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if no active package is found or in case of an error
    }

    public static int getRemainingTests(int learnerId) {
        // For this example, we'll assume that the number of remaining tests is stored in the same way as lessons
        // In a real-world scenario, you might have a separate column for tests or a different calculation method
        return getRemainingLessons(learnerId);
    }

    public static void updateLearnerActivePackageId(int learnerId, int packageId) throws SQLException {
        String query = "UPDATE learners SET active_package_id = ? WHERE learner_id = ?";
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, packageId);
            pstmt.setInt(2, learnerId);
            pstmt.executeUpdate();
        }
    }

    public static int getTotalLearners() {
        String query = "SELECT COUNT(*) FROM learners";
        try ( Connection conn = getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ObservableList<Learner> getNewLearnersForToday() {
        String query = "SELECT * FROM learners WHERE registration_date = CURRENT_DATE";
        ObservableList<Learner> newLearners = FXCollections.observableArrayList();
        try ( Connection conn = getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                newLearners.add(mapResultSetToLearner(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newLearners;
    }

    public static ObservableList<Learner> getLearnersWhoWroteTestToday() {
        String query = "SELECT l.* FROM learners l INNER JOIN progress_tracking p ON l.learner_id = p.learner_id WHERE p.session_date = CURRENT_DATE";
        ObservableList<Learner> learnersWhoWroteTest = FXCollections.observableArrayList();
        try ( Connection conn = getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                learnersWhoWroteTest.add(mapResultSetToLearner(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learnersWhoWroteTest;
    }

    public static ObservableList<Learner> getLearnersWhoDidNotAttendToday() {
        String query = "SELECT l.* FROM learners l WHERE l.learner_id IN (SELECT s.learner_id FROM schedule s WHERE s.session_date = CURRENT_DATE) AND l.learner_id NOT IN (SELECT p.learner_id FROM progress_tracking p WHERE p.session_date = CURRENT_DATE)";
        ObservableList<Learner> learnersWhoDidNotAttend = FXCollections.observableArrayList();
        try ( Connection conn = getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                learnersWhoDidNotAttend.add(mapResultSetToLearner(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return learnersWhoDidNotAttend;
    }

    public static ObservableList<Learner> getLearnersScheduledForNextDay() {
        String query = "SELECT l.* FROM learners l INNER JOIN schedule s ON l.learner_id = s.learner_id WHERE s.session_date = CURRENT_DATE + INTERVAL '1 DAY'";
        ObservableList<Learner> nextDayLearners = FXCollections.observableArrayList();
        try ( Connection conn = getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                nextDayLearners.add(mapResultSetToLearner(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextDayLearners;
    }

    private static Learner mapResultSetToLearner(ResultSet rs) throws SQLException {
        return new Learner(
                rs.getInt("learner_id"),
                rs.getString("full_name"),
                rs.getString("address"),
                rs.getString("password"),
                rs.getString("username"),
                rs.getDate("date_of_birth").toLocalDate(),
                rs.getString("contact_number"),
                rs.getString("email"),
                rs.getInt("active_package_id"),
                rs.getString("id_number"),
                rs.getString("gender"),
                rs.getString("emergency_contact"),
                rs.getString("preferred_language"),
                rs.getString("package_type"),
                rs.getBoolean("previous_experience"),
                rs.getString("notes"),
                rs.getString("status"),
                rs.getDate("registration_date").toLocalDate()
        );
    }

    public static int getTotalLearnersDash() {
        String query = "SELECT COUNT(*) FROM learners";
        return executeCountQuery(query);
    }

    public static int getActivePackagesCount() {
        String query = "SELECT COUNT(*) FROM learner_packages WHERE package_completed = false";
        return executeCountQuery(query);
    }

    public static int getScheduledSessionsCount() {
        String query = "SELECT COUNT(*) FROM schedule";
        return executeCountQuery(query);
    }

    public static int getCompletedSessionsCount() {
        String query = "SELECT COUNT(*) FROM progress_tracking WHERE completion_status = true";
        return executeCountQuery(query);
    }

    public static int getTestsCompletedCount() {
        String query = "SELECT COUNT(*) FROM progress_tracking WHERE quiz_score IS NOT NULL";
        return executeCountQuery(query);
    }

    public static int getInactivePackagesCount() {
        String query = "SELECT COUNT(*) FROM learner_packages WHERE package_completed = true";
        return executeCountQuery(query);
    }

    public static int getCompletedQuizzesCount() {
        String query = "SELECT COUNT(*) FROM progress_tracking WHERE quiz_score IS NOT NULL";
        return executeCountQuery(query);
    }

    public static double getAverageQuizScore() {
        String query = "SELECT AVG(quiz_score) FROM progress_tracking WHERE quiz_score IS NOT NULL";
        double averageScore = 0.0;
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query);  ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                averageScore = rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return averageScore;
    }

    public static double getAverageSessionDuration() {
        String query = "SELECT AVG(EXTRACT(EPOCH FROM (session_end_time - session_start_time)) / 60) "
                + "FROM progress_tracking WHERE session_start_time IS NOT NULL AND session_end_time IS NOT NULL";
        double averageDuration = 0.0;
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query);  ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                averageDuration = rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return averageDuration;
    }

    private static int executeCountQuery(String query) {
        int count = 0;
        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query);  ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static double getRemainingTestsCount() {
        String query = "SELECT SUM(tests_remaining) FROM learner_packages WHERE package_completed = false";
        double remainingTests = 0.0;

        try ( Connection conn = getConnection();  PreparedStatement pstmt = conn.prepareStatement(query);  ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                remainingTests = rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return remainingTests;
    }

}
