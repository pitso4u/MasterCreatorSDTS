/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package adminclerksdts;
import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageVerifier {

    // Database credentials
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/smartdrive_db";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "Soetsang@144156";
    private static final String IMAGE_FOLDER_PATH = "C:\\Users\\sam\\BakubungAppSignsImages";

    public static void main(String[] args) {
        List<String> missingImages = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            String query = "SELECT question_id, image_url FROM rules WHERE has_image = TRUE";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int questionId = rs.getInt("question_id");
                    String imageUrl = rs.getString("image_url");
                    File imageFile = new File(IMAGE_FOLDER_PATH + imageUrl);

                    if (!imageFile.exists()) {
                        missingImages.add("Question ID: " + questionId + ", Image URL: " + imageUrl);
                    }
                }
            }

            if (missingImages.isEmpty()) {
                System.out.println("All images are present.");
            } else {
                System.out.println("Missing images for the following question IDs:");
                missingImages.forEach(System.out::println);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
