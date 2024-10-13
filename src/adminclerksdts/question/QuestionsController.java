package adminclerksdts.question;

import adminclerksdts.ControlledScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class QuestionsController implements Initializable, ControlledScreen {

@FXML private ChoiceBox<String> tableName;
    @FXML private TextField questionText, option1, option2, option3;
    @FXML private ChoiceBox<String> correctAnswerCHO, hasImageCHO;
    @FXML private Button saveButton, uploadImageButton;
    @FXML private Label uploadImageLBL;
    @FXML private TextField uploadImageTX;
    @FXML private ImageView uploadedImageView;
    @FXML private Text sideText;
    @FXML private VBox questionSectionBox;
    @FXML private HBox correctImageSectionBox;
    @FXML private HBox uploadSectionBox;
    @FXML private VBox displayImageSectionBox;
    @FXML private Button backButton;

    private ScreenManager screenManager;
    private Question currentQuestion;
    private boolean isEditMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        correctAnswerCHO.getItems().addAll("1", "2", "3");
        hasImageCHO.getItems().addAll("Has Image", "No Image");
        hasImageCHO.setValue("No Image");

        // Populate tableName ChoiceBox with available table names
        tableName.getItems().addAll(DatabaseConnector.getAvailableTableNames());

        hideImageSection();
//        uploadButton.setVisible(false);
        
        if (backButton != null) {
            backButton.setDisable(false);
        }
        
        uploadSectionBox.setDisable(true);
    }

    private void toggleImageSection() {
        if ("Has Image".equals(hasImageCHO.getValue())) {
            uploadSectionBox.setDisable(false);
        } else {
            uploadSectionBox.setDisable(true);
        }
    }

    


    private void disableInputFields(boolean disable) {
        tableName.setDisable(disable);
        questionText.setDisable(disable);
        option1.setDisable(disable);
        option2.setDisable(disable);
        option3.setDisable(disable);
        correctAnswerCHO.setDisable(disable);
        hasImageCHO.setDisable(disable);
    }
private void showImageSection() {
    uploadImageLBL.setVisible(true);
    uploadImageTX.setVisible(true);
    uploadImageButton.setVisible(true);
    uploadedImageView.setVisible(true);
    uploadSectionBox.setDisable(false);
}

private void hideImageSection() {
    uploadImageLBL.setVisible(false);
    uploadImageTX.setVisible(false);
    uploadImageButton.setVisible(false);
    uploadedImageView.setVisible(false);
    uploadSectionBox.setDisable(true);
}
    private void updateQuestion(ActionEvent event) {
        if (validateInput() && currentQuestion != null) {
            try {
                currentQuestion.setQuestionText(questionText.getText());
                currentQuestion.setHasImage("Has Image".equals(hasImageCHO.getValue()));
                currentQuestion.setOption1(option1.getText());
                currentQuestion.setOption2(option2.getText());
                currentQuestion.setOption3(option3.getText());
                currentQuestion.setCorrectOption(Integer.parseInt(correctAnswerCHO.getValue()));
                DatabaseConnector.updateEditQuestion(currentQuestion);
                
                questionSectionBox.setDisable(true);
                correctImageSectionBox.setDisable(true);
                
                toggleImageSection();
            } catch (SQLException ex) {
                Logger.getLogger(QuestionsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

   @FXML
private void uploadImage(ActionEvent event) {
    if (currentQuestion == null || currentQuestion.getQuestionID() == -1) {
        showAlert(Alert.AlertType.ERROR, "Upload Error", "Please save the question before uploading an image.");
        return;
    }

    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
    File selectedFile = fileChooser.showOpenDialog(null);

    if (selectedFile != null) {
        // Use system-independent path separators
        String userHome = System.getProperty("user.home");  // Get the user's home directory
        Path imagesDir = Paths.get(userHome, "BakubungAppSignsImages");  // Use Paths.get to handle platform-specific separators

        // Create the directory if it doesn't exist
        if (!Files.exists(imagesDir)) {
            try {
                Files.createDirectories(imagesDir);  // Create the directory
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Directory Creation Error", "Failed to create the image directory.");
                return;
            }
        }

        // Build the destination path
        Path destinationPath = imagesDir.resolve(currentQuestion.getQuestionID() + "_" + selectedFile.getName());

        try {
            Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            currentQuestion.setImageURL(destinationPath.toString());  // Update the question's image URL
            DatabaseConnector.updateEditQuestion(currentQuestion);

            uploadImageTX.setText(destinationPath.toString());  // Show the path in the UI
            displayImage(destinationPath.toString());  // Display the image in the ImageView

            backButton.setDisable(false);
            uploadSectionBox.setDisable(true);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Image Upload Error", "Failed to upload and save the image.");
        } catch (SQLException ex) {
            Logger.getLogger(QuestionsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}



    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        screenManager.setScreen("ViewQuestionsScreen");
        ViewQuestionsController viewQuestionsController = (ViewQuestionsController) screenManager.getController("ViewQuestionsScreen");
        if (viewQuestionsController != null) {
            viewQuestionsController.loadQuestions();
        }
    }

@FXML
    private void saveOrUpdateQuestion(ActionEvent event) {
        if (validateInput()) {
            try {
                if (isEditMode) {
                    updateQuestion();
                } else {
                    createNewQuestion();
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", isEditMode ? "Question updated successfully." : "Question saved successfully.");
                
                if (backButton != null) {
                    backButton.setDisable(false);
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to " + (isEditMode ? "update" : "save") + " the question. Please try again.");
                ex.printStackTrace();
            }
        }
    }

    private void createNewQuestion() throws SQLException {
        currentQuestion = new Question(
            -1,
            tableName.getValue(),
            questionText.getText(),
            "",
            "Has Image".equals(hasImageCHO.getValue()),
            option1.getText(),
            option2.getText(),
            option3.getText(),
            Integer.parseInt(correctAnswerCHO.getValue())
        );

        int generatedId = DatabaseConnector.saveQuestion(currentQuestion, tableName.getValue());
        currentQuestion.setQuestionID(generatedId);

        disableInputFields(true);
        
        if (currentQuestion.isHasImage()) {
            showImageSection();
        } else {
            hideImageSection();
        }
        
        saveButton.setDisable(true);
    }

    private void updateQuestion() throws SQLException {
        currentQuestion.setQuestionText(questionText.getText());
        currentQuestion.setHasImage("Has Image".equals(hasImageCHO.getValue()));
        currentQuestion.setOption1(option1.getText());
        currentQuestion.setOption2(option2.getText());
        currentQuestion.setOption3(option3.getText());
        currentQuestion.setCorrectOption(Integer.parseInt(correctAnswerCHO.getValue()));
        
        DatabaseConnector.updateEditQuestion(currentQuestion);
        
        if (currentQuestion.isHasImage()) {
            showImageSection();
        } else {
            hideImageSection();
        }
    }

    public void loadQuestion(Question question) {
        isEditMode = true;
        currentQuestion = question;
        tableName.setValue(question.getTableName());
        questionText.setText(question.getQuestionText());
        option1.setText(question.getOption1());
        option2.setText(question.getOption2());
        option3.setText(question.getOption3());
        correctAnswerCHO.setValue(String.valueOf(question.getCorrectOption()));
        hasImageCHO.setValue(question.isHasImage() ? "Has Image" : "No Image");
        saveButton.setVisible(true);
        if (question.isHasImage()) {
            uploadImageTX.setText(question.getImageURL());
            displayImage(question.getImageURL());
            showImageSection();
        } else {
            hideImageSection();
        }
        
        //sideText.setText("EDIT QUESTION");
        saveButton.setText("Update Question");
        backButton.setDisable(false);
        
        // Enable input fields for editing
        disableInputFields(false);
    }

    public void resetForm() {
        isEditMode = false;
        currentQuestion = null;
        tableName.setValue(null);
        questionText.clear();
        option1.clear();
        option2.clear();
        option3.clear();
        correctAnswerCHO.setValue(null);
        hasImageCHO.setValue("No Image");
        uploadImageTX.clear();
        uploadedImageView.setImage(null);
        hideImageSection();
//        sideText.setText("NEW QUESTION");
        saveButton.setText("Save Question");
        saveButton.setDisable(false);
        backButton.setDisable(true);
        questionSectionBox.setDisable(false);
        correctImageSectionBox.setDisable(false);
        uploadSectionBox.setDisable(true);
        
        // Enable input fields for new question
        disableInputFields(false);
    }
    private boolean validateInput() {
        if (tableName.getValue() == null || tableName.getValue().isEmpty() || 
            questionText.getText().isEmpty() ||
            option1.getText().isEmpty() || option2.getText().isEmpty() ||
            option3.getText().isEmpty() || 
            correctAnswerCHO.getValue() == null || 
            hasImageCHO.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void displayImage(String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            uploadedImageView.setImage(image);
            uploadedImageView.setVisible(true);
        } else {
            showAlert(Alert.AlertType.ERROR, "Image Display Error", "Image file not found.");
        }
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        this.screenManager = screenParent;
    }

    @Override
    public void runOnScreenChange() {
        // Implement if needed
    }

    @Override
    public void cleanup() {
        // Implement if needed
    }
}