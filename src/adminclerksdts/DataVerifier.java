/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package adminclerksdts;

import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;

public class DataVerifier {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/smartdrive_db";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "Soetsang@144156";
    private static final String IMAGE_DIRECTORY = "C:\\Users\\sam\\BakubungAppSignsImages";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            // 1. Verify Images
            verifyImages(conn);

            // 2. Check Spelling
//            checkSpelling(conn);

            // 3. Detect Duplicates
            detectDuplicateQuestions(conn);
            detectDuplicateOptions(conn);

            // 4. Validate Correct Options
            validateCorrectOptions(conn);

            System.out.println("Data verification completed.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void verifyImages(Connection conn) {
        System.out.println("---- Verifying Image URLs ----");
        List<String> missingImages = new ArrayList<>();

        String query = "SELECT question_id, image_url FROM signs WHERE has_image = TRUE";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int questionId = rs.getInt("question_id");
                String imageUrl = rs.getString("image_url");
                File imageFile = new File(IMAGE_DIRECTORY + imageUrl);

                if (!imageFile.exists()) {
                    missingImages.add("Question ID: " + questionId + ", Image URL: " + imageUrl);
                }
            }

            if (missingImages.isEmpty()) {
                System.out.println("All images are present.");
            } else {
                System.out.println("Missing images for the following question IDs:");
                missingImages.forEach(System.out::println);
            }

        } catch (SQLException e) {
            System.out.println("Error verifying images: " + e.getMessage());
        }
        System.out.println();
    }

private static void checkSpelling(Connection conn) {
    System.out.println("---- Checking for Spelling Errors ----");
    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(
            "SELECT question_id, question_text, option1, option2, option3, option4 FROM signs")) {

        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

        while (rs.next()) {
            int questionId = rs.getInt("question_id");
            String questionText = rs.getString("question_text");
            String option1 = rs.getString("option1");
            String option2 = rs.getString("option2");
            String option3 = rs.getString("option3");
            String option4 = rs.getString("option4");

            StringBuilder combinedText = new StringBuilder();
            combinedText.append(questionText).append(" ");
            if (option1 != null) combinedText.append(option1).append(" ");
            if (option2 != null) combinedText.append(option2).append(" ");
            if (option3 != null) combinedText.append(option3).append(" ");
            if (option4 != null) combinedText.append(option4).append(" ");

            List<RuleMatch> matches = langTool.check(combinedText.toString());

            if (!matches.isEmpty()) {
                System.out.println("Potential typos found in Question ID: " + questionId);
                for (RuleMatch match : matches) {
                    System.out.println(" - " + match.getMessage() + " at position " + match.getFromPos() + "-" + match.getToPos());
                    // Removed getContext().getText(), as it doesn't exist in this version
                    System.out.println("   Suggested correction: " + match.getSuggestedReplacements());
                }
                System.out.println();
            }
        }

    } catch (Exception e) {
        System.out.println("Error checking spelling: " + e.getMessage());
    }
    System.out.println();
}

   private static void detectDuplicateQuestions(Connection conn) {
    System.out.println("---- Detecting Duplicate Questions ----");

    String query = 
        "SELECT question_text, option1, option2, option3, option4, COUNT(*) as count " +
        "FROM signs " +
        "GROUP BY question_text, option1, option2, option3, option4 " +
        "HAVING COUNT(*) > 1";

    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        boolean duplicatesFound = false;
        while (rs.next()) {
            duplicatesFound = true;
            String questionText = rs.getString("question_text");
            String optionA = rs.getString("option1");
            String optionB = rs.getString("option2");
            String optionC = rs.getString("option3");
            String optionD = rs.getString("option4");
            int count = rs.getInt("count");

            System.out.println("Duplicate Question: \"" + questionText + "\" with options (" +
                               optionA + ", " + optionB + ", " + optionC + ", " + optionD + 
                               ") appears " + count + " times.");
        }

        if (!duplicatesFound) {
            System.out.println("No duplicate questions found.");
        }

    } catch (SQLException e) {
        System.out.println("Error detecting duplicate questions: " + e.getMessage());
    }
    System.out.println();
}

    private static void detectDuplicateOptions(Connection conn) {
        System.out.println("---- Detecting Duplicate Options within Questions ----");
        String query = 
            "SELECT question_id, option_text, COUNT(*) as count FROM (" +
            "SELECT question_id, option1 AS option_text FROM signs " +
            "UNION ALL " +
            "SELECT question_id, option2 FROM signs " +
            "UNION ALL " +
            "SELECT question_id, option3 FROM signs " +
            "UNION ALL " +
            "SELECT question_id, option4 FROM signs" +
            ") AS all_options " +
            "GROUP BY question_id, option_text " +
            "HAVING COUNT(*) > 1";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            boolean duplicatesFound = false;
            while (rs.next()) {
                duplicatesFound = true;
                int questionId = rs.getInt("question_id");
                String optionText = rs.getString("option_text");
                int count = rs.getInt("count");
                System.out.println("Duplicate Option in Question ID " + questionId + ": \"" + optionText + "\" appears " + count + " times.");
            }

            if (!duplicatesFound) {
                System.out.println("No duplicate options found within questions.");
            }

        } catch (SQLException e) {
            System.out.println("Error detecting duplicate options: " + e.getMessage());
        }
        System.out.println();
    }

    private static void validateCorrectOptions(Connection conn) {
        System.out.println("---- Validating Correct Option Mappings ----");
        String query = 
            "SELECT question_id, correct_option, option1, option2, option3, option4 " +
            "FROM signs " +
            "WHERE correct_option NOT BETWEEN 1 AND 4 " +
            "OR (correct_option = 1 AND option1 IS NULL) " +
            "OR (correct_option = 2 AND option2 IS NULL) " +
            "OR (correct_option = 3 AND option3 IS NULL) " +
            "OR (correct_option = 4 AND option4 IS NULL)";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            boolean invalidMappingsFound = false;
            while (rs.next()) {
                invalidMappingsFound = true;
                int questionId = rs.getInt("question_id");
                int correctOption = rs.getInt("correct_option");
                String option1 = rs.getString("option1");
                String option2 = rs.getString("option2");
                String option3 = rs.getString("option3");
                String option4 = rs.getString("option4");

                System.out.println("Invalid correct_option in Question ID: " + questionId);
                System.out.println(" - correct_option: " + correctOption);
                System.out.println(" - Options: ");
                System.out.println("   1. " + option1);
                System.out.println("   2. " + option2);
                System.out.println("   3. " + option3);
                System.out.println("   4. " + option4);
                System.out.println();
            }

            if (!invalidMappingsFound) {
                System.out.println("All correct_option mappings are valid.");
            }

        } catch (SQLException e) {
            System.out.println("Error validating correct options: " + e.getMessage());
        }
        System.out.println();
    }
}
