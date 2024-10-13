package adminclerksdts;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ImageValidator {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/smartdrive_db";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "Soetsang@144156";
    private static final String IMAGE_FOLDER_PATH = "C:\\Users\\sam\\BakubungAppSignsImages";
    private static final String IMAGE_PATH_PREFIX = "src/Images/";

    public static void main(String[] args) {
        List<String> imageUrls = new ArrayList<>();
        List<String> missingFiles = new ArrayList<>();
        File imageFolder = new File(IMAGE_FOLDER_PATH);
        String[] imageFiles = imageFolder.list();

        if (imageFiles == null) {
            System.out.println("Image folder is empty or does not exist.");
            return;
        }

        // Clean and standardize image file names in the folder
        for (int i = 0; i < imageFiles.length; i++) {
            imageFiles[i] = cleanImageName(imageFiles[i]);
        }

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             Statement statement = connection.createStatement()) {

            // Fetch image URLs from signs, controls, and rules tables
            String[] tables = {"signs", "controls", "rules"};
            for (String table : tables) {
                ResultSet resultSet = statement.executeQuery("SELECT image_url FROM " + table);
                while (resultSet.next()) {
                    String imageUrl = resultSet.getString("image_url");
                    if (imageUrl != null) {
                        imageUrls.add(cleanImageName(imageUrl.replace("/src/images/", "")));
                    }
                }
            }

            // Compare image URLs with filenames in the folder
            for (String imageUrl : imageUrls) {
                boolean matchFound = false;
                for (String fileName : imageFiles) {
                    if (fileName.equals(imageUrl)) {
                        matchFound = true;
                        break;
                    }
                }
                if (!matchFound) {
                    missingFiles.add(IMAGE_PATH_PREFIX + imageUrl);  // Collect missing files
                }
            }

            // Print out missing files
            if (missingFiles.isEmpty()) {
                System.out.println("All image files are accounted for.");
            } else {
                System.out.println("Missing image files:");
                for (String missingFile : missingFiles) {
                    System.out.println(missingFile);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cleans and standardizes image names:
     * 1. Converts the name to lowercase.
     * 2. Removes underscores.
     * 3. Ensures the name doesn't start with a number (prefixes with "image_" if it does).
     * 4. Ensures all image paths start with "src/Images/".
     * 
     * @param imageName the original image name
     * @return the cleaned and standardized image name
     */
    private static String cleanImageName(String imageName) {
        // Extract just the filename if it's a full path
        String fileName = new File(imageName).getName();

        // Convert to lowercase and remove underscores
        fileName = fileName.toLowerCase().replace("_", "");

        // If the filename starts with a number, prefix with "image_"
        if (Character.isDigit(fileName.charAt(0))) {
            fileName = "" + fileName;
        }

        // Return the standardized name with the correct path
        return IMAGE_PATH_PREFIX + fileName;
    }
}
