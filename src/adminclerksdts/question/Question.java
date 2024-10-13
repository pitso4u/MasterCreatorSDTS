/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts.question;
import javafx.beans.property.*;

public class Question {
    private final IntegerProperty questionID;
    private final StringProperty questionText;
    private final StringProperty imageURL;
    private final BooleanProperty hasImage;
    private final StringProperty option1;
    private final StringProperty option2;
    private final StringProperty option3;
    private final IntegerProperty correctOption;
    private final StringProperty tableName;

    
   public Question(int questionID, String tableName, String questionText, String imageURL, boolean hasImage,
                String option1, String option2, String option3,  int correctOption){

    
        this.questionID = new SimpleIntegerProperty(questionID);
        this.questionText = new SimpleStringProperty(questionText);
        this.imageURL = new SimpleStringProperty(imageURL);
        this.hasImage = new SimpleBooleanProperty(hasImage);
        this.option1 = new SimpleStringProperty(option1);
        this.option2 = new SimpleStringProperty(option2);
        this.option3 = new SimpleStringProperty(option3);
        this.correctOption = new SimpleIntegerProperty(correctOption);
        this.tableName = new SimpleStringProperty(tableName);
        
}

    public Question(IntegerProperty questionID,StringProperty questionText, StringProperty imageURL, BooleanProperty hasImage, StringProperty option1, StringProperty option2, StringProperty option3, IntegerProperty correctOption, StringProperty tableName) {
        this.questionText = new SimpleStringProperty(questionText.get());
        this.imageURL = new SimpleStringProperty(imageURL.get());
        this.hasImage = new SimpleBooleanProperty(hasImage.get());
        this.option1 = new SimpleStringProperty(option1.get());
        this.option2 = new SimpleStringProperty(option2.get());
        this.option3 = new SimpleStringProperty(option3.get());
        this.correctOption = new SimpleIntegerProperty(correctOption.get());
        this.tableName = new SimpleStringProperty(tableName.get());
        this.questionID = questionID;
    }

    // Getters and setters for questionID
    public int getQuestionID() {
        return questionID.get();
    }

    public void setQuestionID(int questionID) {
        this.questionID.set(questionID);
    }

    public IntegerProperty questionIDProperty() {
        return questionID;
    }

    // Getter and setter for tableName
    public String getTableName() {
        return tableName.get();
    }

    public void setTableName(String tableName) {
        this.tableName.set(tableName);
    }

    // Other getters and setters for properties
    public String getQuestionText() {
        return questionText.get();
    }

    public StringProperty questionTextProperty() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText.set(questionText);
    }

    public String getImageURL() {
        return imageURL.get();
    }

    public StringProperty imageURLProperty() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL.set(imageURL);
    }

    public boolean isHasImage() {
        return hasImage.get();
    }

    public BooleanProperty hasImageProperty() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage.set(hasImage);
    }

    public String getOption1() {
        return option1.get();
    }

    public StringProperty option1Property() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1.set(option1);
    }

    public String getOption2() {
        return option2.get();
    }

    public StringProperty option2Property() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2.set(option2);
    }

    public String getOption3() {
        return option3.get();
    }

    public StringProperty option3Property() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3.set(option3);
    }

    

    public int getCorrectOption() {
        return correctOption.get();
    }

    public IntegerProperty correctOptionProperty() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption.set(correctOption);
    }
}
