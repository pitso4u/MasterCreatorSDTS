/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts.schedule;

import adminclerksdts.DatabaseConnector;
import adminclerksdts.learner.Learner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassScheduler {

    // Method to schedule a two-hour class session for a learner over five consecutive days
    public static void scheduleClasses(Learner learner) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            for (int day = 1; day <= 5; day++) {
                if (isSlotAvailable(connection, day)) {
                    insertClassSession(connection, learner, day);
                } else {
                    System.out.println("Class slot not available for Day " + day);
                    // You may want to handle this situation accordingly
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    // Method to check if a class slot is available for a given day
public static boolean isSlotAvailable(Connection connection, int day) throws SQLException {
    // Implement your logic to check class slot availability
    // You may want to query the database or use another mechanism
    // Return true if the slot is available, false otherwise

    // Placeholder, replace with actual logic
    // Example: Check if the day has less than the maximum allowed learners for the class
    String query = "SELECT COUNT(*) FROM Schedule WHERE session_date = CURRENT_DATE + INTERVAL '" + day + " day'";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int enrolledLearners = resultSet.getInt(1);
                int maxAllowedLearners = 10; // Adjust this based on your requirement
                return enrolledLearners < maxAllowedLearners;
            }
        }
    }
    return false; // Placeholder, replace with actual logic
}


    // Method to insert a class session into the database
    private static void insertClassSession(Connection connection, Learner learner, int day) throws SQLException {
        String query = "INSERT INTO Schedule (learner_id, session_date, session_start_time, session_end_time) " +
                       "VALUES (?, CURRENT_DATE + INTERVAL '" + day + " day', '09:00:00', '11:00:00')";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, learner.getLearnerId()); // Assuming you have a method to get learner ID
            preparedStatement.executeUpdate();
        }
    }
}
