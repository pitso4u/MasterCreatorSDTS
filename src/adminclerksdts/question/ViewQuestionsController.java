package adminclerksdts.question;

import adminclerksdts.ControlledScreen;
import adminclerksdts.DashboardScreen;
import adminclerksdts.DatabaseConnector;
import adminclerksdts.ScreenManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ViewQuestionsController implements Initializable, ControlledScreen {

    @FXML private TableView<Question> questionsTable;
    @FXML private TableColumn<Question, Integer> questionIDColumn;
    @FXML private TableColumn<Question, String> questionTextColumn;
    @FXML private TableColumn<Question, ImageView> imageURLColumn;
    @FXML private TableColumn<Question, Boolean> hasImageColumn;
    @FXML private TableColumn<Question, String> option1Column;
    @FXML private TableColumn<Question, String> option2Column;
    @FXML private TableColumn<Question, String> option3Column;
    @FXML private TableColumn<Question, Integer> correctOptionColumn;
    @FXML private TableColumn<Question, String> sourceColumn;

    private ScreenManager screenManager;
    @FXML
    private TextField searchField;
     private FilteredList<Question> filteredQuestions;

    
    
private ObservableList<Question> allQuestions;
// Define IMAGE_DIRECTORY using system-independent path handling
    private static final Path IMAGE_DIRECTORY = Paths.get(System.getProperty("user.home"), "BakubungAppSignsImages");
private static final String IMAGE_FOLDER = "/images/";

    private final Set<String> reportedMissingImages = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadQuestions();
    }

// Example method to get an image from the resources
public Image loadImage(String imageName) {
    // Construct the path to the image in the classpath
    String imagePath = IMAGE_FOLDER + imageName;
    
    // Load the image from the classpath
    InputStream imageStream = getClass().getResourceAsStream(imagePath);
    if (imageStream == null) {
        System.err.println("Image file not found: " + imagePath);
        return null;
    }
    
    // Create and return the Image object
    return new Image(imageStream);
}


    private ImageView loadImageView(String imageName, int questionId) {
        if (imageName == null || imageName.isEmpty()) {
            return createPlaceholderImageView();
        }

        Path imagePath = IMAGE_DIRECTORY.resolve(imageName);

        if (Files.notExists(imagePath)) {
            if (!reportedMissingImages.contains(imageName)) {
                Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.WARNING, "Image file not found for question " + questionId + ": " + imagePath);
                reportedMissingImages.add(imageName);
                // TODO: Notify administrator about missing image
            }
            return createPlaceholderImageView();
        }

        try {
            Image image = new Image(imagePath.toUri().toString(), 100, 100, true, true);
            return new ImageView(image);
        } catch (Exception e) {
            Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.WARNING, "Error loading image for question " + questionId + ": " + imagePath, e);
            return createPlaceholderImageView();
        }
    }

private ImageView createPlaceholderImageView() {
    String svgContent = "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 200 200\">"
            + "<rect x=\"10\" y=\"10\" width=\"180\" height=\"180\" fill=\"#f0f0f0\" stroke=\"#cccccc\" stroke-width=\"2\"/>"
            + "<circle cx=\"100\" cy=\"70\" r=\"40\" fill=\"#ffcc00\"/>"
            + "<circle cx=\"85\" cy=\"60\" r=\"5\" fill=\"#000000\"/>"
            + "<circle cx=\"115\" cy=\"60\" r=\"5\" fill=\"#000000\"/>"
            + "<path d=\"M70 90 Q100 70 130 90\" fill=\"none\" stroke=\"#000000\" stroke-width=\"3\"/>"
            + "<text x=\"100\" y=\"160\" font-family=\"Arial, sans-serif\" font-size=\"24\" text-anchor=\"middle\" fill=\"#666666\">No Image</text>"
            + "</svg>";

    try {
        InputStream svgInputStream = new ByteArrayInputStream(svgContent.getBytes(StandardCharsets.UTF_8));
        Image placeholderImage = new Image(svgInputStream);
        ImageView placeholderImageView = new ImageView(placeholderImage);
        placeholderImageView.setFitWidth(100);
        placeholderImageView.setFitHeight(100);
        placeholderImageView.setPreserveRatio(true);
        return placeholderImageView;
    } catch (Exception e) {
        Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.WARNING,"Error creating placeholder image");
        return new ImageView(); // Return an empty ImageView as a last resort
    }
}
    private void setupTableColumns() {
    questionIDColumn.setCellValueFactory(new PropertyValueFactory<>("questionID"));
    questionTextColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
    
            imageURLColumn.setCellValueFactory(cellData -> {
            String imageName = cellData.getValue().getImageURL();
            if (imageName != null && !imageName.isEmpty()) {
                try {
                    URL imageUrl = getClass().getResource("/" + IMAGE_FOLDER + "/" + imageName);
                    if (imageUrl == null) {
                        Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.WARNING, "Image file not found: " + imageName);
                        return new SimpleObjectProperty<>(null);
                    }
                    Image image = new Image(imageUrl.toExternalForm(), 100, 100, true, true);
                    if (image.isError()) {
                        Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.WARNING, "Failed to load image: " + imageName);
                        return new SimpleObjectProperty<>(null);
                    }
                    ImageView imageView = new ImageView(image);
                    return new SimpleObjectProperty<>(imageView);
                } catch (Exception e) {
                    Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.WARNING, "Error loading image: " + imageName, e);
                    return new SimpleObjectProperty<>(null);
                }
            }
            return new SimpleObjectProperty<>(null);
        });

        imageURLColumn.setCellFactory(column -> new TableCell<Question, ImageView>() {
            private final ImageView imageView = new ImageView();
            private final TextField textField = new TextField();
            
            {
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
                
                textField.setOnAction(event -> {
                    String newImageName = textField.getText();
                    if (newImageName != null && !newImageName.isEmpty()) {
                        try {
                            URL imageUrl = getClass().getResource("/" + IMAGE_FOLDER + "/" + newImageName);
                            if (imageUrl == null) {
                                throw new IllegalArgumentException("Image file not found: " + newImageName);
                            }
                            Image newImage = new Image(imageUrl.toExternalForm(), 100, 100, true, true);
                            if (newImage.isError()) {
                                throw new IllegalArgumentException("Failed to load image: " + newImageName);
                            }
                            imageView.setImage(newImage);
                            commitEdit(imageView);
                            Question question = getTableView().getItems().get(getIndex());
                            question.setImageURL(newImageName);
                            updateQuestionInDatabase(question);
                        } catch (IllegalArgumentException e) {
                            Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.WARNING, "Invalid image: " + newImageName, e);
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load image. Please check if the file exists in the Images folder.");
                        }
                    }
                });
            }
            
            protected void updateItem(ImageView item, Boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item.getImage());
                    setGraphic(imageView);
                }
            }
            
            @Override
            public void startEdit() {
                super.startEdit();
                setGraphic(textField);
                textField.setText(((Question)getTableRow().getItem()).getImageURL());
                textField.requestFocus();
            }
            
            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setGraphic(imageView);
            }
        });

    
    hasImageColumn.setCellValueFactory(new PropertyValueFactory<>("hasImage"));
    option1Column.setCellValueFactory(new PropertyValueFactory<>("option1"));
    option2Column.setCellValueFactory(new PropertyValueFactory<>("option2"));
    option3Column.setCellValueFactory(new PropertyValueFactory<>("option3"));
    correctOptionColumn.setCellValueFactory(new PropertyValueFactory<>("correctOption"));
    sourceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTableName()));
    
    questionsTable.setEditable(true);
    
    questionTextColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    questionTextColumn.setOnEditCommit(event -> {
        Question question = event.getRowValue();
        question.setQuestionText(event.getNewValue());
        updateQuestionInDatabase(question);
    });
    
    
    option1Column.setCellFactory(TextFieldTableCell.forTableColumn());
    option1Column.setOnEditCommit(event -> {
        Question question = event.getRowValue();
        question.setOption1(event.getNewValue());
        updateQuestionInDatabase(question);
    });
    
    option2Column.setCellFactory(TextFieldTableCell.forTableColumn());
    option2Column.setOnEditCommit(event -> {
        Question question = event.getRowValue();
        question.setOption2(event.getNewValue());
        updateQuestionInDatabase(question);
    });
    
    option3Column.setCellFactory(TextFieldTableCell.forTableColumn());
    option3Column.setOnEditCommit(event -> {
        Question question = event.getRowValue();
        question.setOption3(event.getNewValue());
        updateQuestionInDatabase(question);
    });
    
    correctOptionColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    correctOptionColumn.setOnEditCommit(event -> {
        Question question = event.getRowValue();
        question.setCorrectOption(event.getNewValue());
        updateQuestionInDatabase(question);
    });
    
    sourceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    sourceColumn.setOnEditCommit(event -> {
        Question question = event.getRowValue();
        question.setTableName(event.getNewValue());
        updateQuestionInDatabase(question);
    });
}

private void updateQuestionInDatabase(Question question) {
    try {
        DatabaseConnector.updateEditQuestion(question);
    } catch (SQLException e) {
        Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.SEVERE, "Error updating question", e);
        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update question: " + e.getMessage());
    }
}

public void loadQuestions() {
    try {
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.addAll(loadQuestionsFromTable("Signs"));
        allQuestions.addAll(loadQuestionsFromTable("Controls"));
        allQuestions.addAll(loadQuestionsFromTable("Rules"));
        questionsTable.getItems().setAll(allQuestions);
    } catch (SQLException e) {
        Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.SEVERE, "Error loading questions", e);
        showAlert(Alert.AlertType.ERROR, "Error", "Failed to load questions: " + e.getMessage());
    }
}

    private List<Question> loadQuestionsFromTable(String tableName) throws SQLException {
        List<Question> questions = DatabaseConnector.getQuestions(tableName);
        questions.forEach(question -> question.setTableName(tableName));
        return questions;
    }

    @Override
    public void setScreenParent(ScreenManager screenParent) {
        this.screenManager = screenParent;
    }

    @Override
    public void runOnScreenChange() {
        loadQuestions(); // Refresh questions when the screen is displayed
    }

    @Override
    public void cleanup() {
        // Cleanup code, if needed
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        screenManager.setScreen("DashboardScreen");
    }

    @FXML
    private void LoadQuestions(ActionEvent event) {
        loadQuestions();
    }

      @FXML
    private void NewQuestion(ActionEvent event) {
        // Obtain the DashboardScreen controller
    DashboardScreen dashboardScreen = (DashboardScreen) screenManager.getController("DashboardScreen");

    if (dashboardScreen != null) {
        // Load the NewLearnerScreen into the center of the dashboard
        dashboardScreen.loadScreenIntoCenter("NewQuestionScreen");

        // Get the NewLearnerController and reset the form if available
        ControlledScreen controller = screenManager.getController("NewQuestionScreen");
        if (controller instanceof QuestionsController) {
            ((QuestionsController) controller).resetForm();
        } else {
            System.err.println("Error: The loaded controller is not an instance of NewLearnerController.");
        }
    } else {
        System.err.println("Error: DashboardScreen is null. Ensure the screen is correctly loaded.");
    }
        
        
    }
 @FXML
    private void EditQuestion(ActionEvent event) {
        Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            DashboardScreen dashboardScreen = (DashboardScreen) screenManager.getController("DashboardScreen");

    if (dashboardScreen != null) {
        // Load the NewLearnerScreen into the center of the dashboard
        dashboardScreen.loadScreenIntoCenter("NewQuestionScreen");

        // Get the NewLearnerController and reset the form if available
        ControlledScreen controller = screenManager.getController("NewQuestionScreen");
        if (controller instanceof QuestionsController) {
            ((QuestionsController) controller).loadQuestion(selectedQuestion);
        } else {
            System.err.println("Error: The loaded controller is not an instance of NewLearnerController.");
        }
    } else {
        System.err.println("Error: DashboardScreen is null. Ensure the screen is correctly loaded.");
    }
   
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a question to edit.");
        }
    }
    @FXML
    private void DeleteQuestion(ActionEvent event) {
        Question selectedQuestion = questionsTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            Optional<ButtonType> result = showConfirmationAlert("Delete Question", "Are you sure you want to delete this question?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    DatabaseConnector.deleteQuestion(selectedQuestion.getTableName(), selectedQuestion.getQuestionID());
                    questionsTable.getItems().remove(selectedQuestion);
                } catch (SQLException e) {
                    Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.SEVERE, "Error deleting question", e);
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the question: " + e.getMessage());
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a question to delete.");
        }
    }

    private Optional<ButtonType> showConfirmationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void initializeQuestions(List<Question> questions) {
        allQuestions = FXCollections.observableArrayList(questions);
        questionsTable.setItems(allQuestions);
    }

    private void searchQuestions(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            questionsTable.setItems(allQuestions);
        } else {
            ObservableList<Question> filteredQuestions = allQuestions.filtered(question ->
                question.getQuestionText().toLowerCase().contains(searchText) ||
                question.getOption1().toLowerCase().contains(searchText) ||
                question.getOption2().toLowerCase().contains(searchText) ||
                question.getOption3().toLowerCase().contains(searchText)
            );
            questionsTable.setItems(filteredQuestions);
        }
    }

    // Add this method to handle real-time search as the user types
    private void initialize() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchQuestions(null);
        });
    }

   @FXML
private void handleSearchAction(ActionEvent event) {
    if (searchField == null || allQuestions == null) {
        Logger.getLogger(ViewQuestionsController.class.getName()).log(Level.WARNING, "SearchField or Questions list is null");
        return;
    }

    String searchText = searchField.getText().toLowerCase();
    if (searchText == null || searchText.isEmpty()) {
        filteredQuestions.setPredicate(question -> true);  // Show all if search is empty
    } else {
        filteredQuestions.setPredicate(question -> 
            question.getQuestionText().toLowerCase().contains(searchText) ||
            question.getOption1().toLowerCase().contains(searchText) ||
            question.getOption2().toLowerCase().contains(searchText) ||
            question.getOption3().toLowerCase().contains(searchText)
        );
    }

    questionsTable.setItems(filteredQuestions);
}

}