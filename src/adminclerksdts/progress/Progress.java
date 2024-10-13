/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts.progress;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Progress {
    private final IntegerProperty progressID;
    private final IntegerProperty learnerID;
    private final IntegerProperty instructorID;
    private final DoubleProperty quizScore;
    private final BooleanProperty completionStatus;
    private final ObjectProperty<LocalDateTime> timestamp;
    private final StringProperty instructorNotes;
private final StringProperty learnerFullName;
    public Progress(int progressID, int learnerID, int instructorID, double quizScore, boolean completionStatus, LocalDateTime timestamp, String instructorNotes, javafx.beans.property.StringProperty learnerFullName) {
        this.progressID = new SimpleIntegerProperty(progressID);
        this.learnerID = new SimpleIntegerProperty(learnerID);
        this.instructorID = new SimpleIntegerProperty(instructorID);
        this.quizScore = new SimpleDoubleProperty(quizScore);
        this.completionStatus = new SimpleBooleanProperty(completionStatus);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
        this.instructorNotes = new SimpleStringProperty(instructorNotes);
        this.learnerFullName = learnerFullName;
    }

    public Progress(int progressID, String learnerFullName, int instructorID, double quizScore, boolean completionStatus, LocalDateTime timestamp, String instructorNotes, javafx.beans.property.IntegerProperty learnerID) {
       this.progressID = new SimpleIntegerProperty(progressID);
        this.learnerFullName = new SimpleStringProperty(learnerFullName);
        this.instructorID = new SimpleIntegerProperty(instructorID);
        this.quizScore = new SimpleDoubleProperty(quizScore);
        this.completionStatus = new SimpleBooleanProperty(completionStatus);
        this.timestamp = new SimpleObjectProperty<>(timestamp);
        this.instructorNotes = new SimpleStringProperty(instructorNotes);
        this.learnerID = learnerID;
    }

    public IntegerProperty getProgressID() {
        return progressID;
    }

    public IntegerProperty getLearnerID() {
        return learnerID;
    }

    public IntegerProperty getInstructorID() {
        return instructorID;
    }

    public DoubleProperty getQuizScore() {
        return quizScore;
    }

    public BooleanProperty getCompletionStatus() {
        return completionStatus;
    }

    public ObjectProperty<LocalDateTime> getTimestamp() {
        return timestamp;
    }

    public StringProperty getInstructorNotes() {
        return instructorNotes;
    }

    public StringProperty getLearnerFullName() {
        return learnerFullName;
    }

    public IntegerProperty progressIDProperty() {
        return progressID;
    }

    public IntegerProperty learnerIDProperty() {
        return learnerID;
    }
    public StringProperty learnerFullNameProperty() {
        return learnerFullName;
    }

    public IntegerProperty instructorIDProperty() {
        return instructorID;
    }

    public DoubleProperty quizScoreProperty() {
        return quizScore;
    }

    public BooleanProperty completionStatusProperty() {
        return completionStatus;
    }

    public ObjectProperty<LocalDateTime> timestampProperty() {
        return timestamp;
    }

    public StringProperty instructorNotesProperty() {
        return instructorNotes;
    }
}
