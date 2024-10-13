/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adminclerksdts.instructor;
import javafx.beans.property.*;

import java.time.LocalDate;

public class Instructor {
    private final IntegerProperty instructorID;
    private final StringProperty fullName;
    private final StringProperty contactNumber;
    private final StringProperty email;
    private final ObjectProperty<LocalDate> hireDate;
    private final StringProperty specialization;

    public Instructor(int instructorID, String fullName, String contactNumber, String email,
                      LocalDate hireDate, String specialization) {
        this.instructorID = new SimpleIntegerProperty(instructorID);
        this.fullName = new SimpleStringProperty(fullName);
        this.contactNumber = new SimpleStringProperty(contactNumber);
        this.email = new SimpleStringProperty(email);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.specialization = new SimpleStringProperty(specialization);
    }

    public IntegerProperty instructorIDProperty() {
        return instructorID;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty contactNumberProperty() {
        return contactNumber;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public ObjectProperty<LocalDate> hireDateProperty() {
        return hireDate;
    }

    public StringProperty specializationProperty() {
        return specialization;
    }
}
