package adminclerksdts.learner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Learner {
    private final IntegerProperty learnerId;
    private final StringProperty fullName;
    private final StringProperty password;
    private final StringProperty username;
    private final StringProperty gender;
    private final ObjectProperty<LocalDate> dateOfBirth;
    private final StringProperty address;
    private final StringProperty contactNumber;
    private final StringProperty email;
    private final IntegerProperty activePackageId;
    private final StringProperty idNumber;
    private final ObjectProperty<LocalDate> registrationDate;
    private final StringProperty emergencyContact;
    private final StringProperty preferredLanguage;
    private final BooleanProperty previousExperience;
    private final StringProperty packageType;
    private final StringProperty notes;
    private final StringProperty status;

    public Learner(int learnerId, String fullName, String address, String password, 
            String username, LocalDate dateOfBirth, String contactNumber, String email, 
            int activePackageId, String idNumber, String gender, String emergencyContact, 
            String preferredLanguage, String packageType, boolean previousExperience, String notes, 
            String status, LocalDate registrationDate) {
        this.learnerId = new SimpleIntegerProperty(learnerId);
        this.fullName = new SimpleStringProperty(fullName);
        this.address = new SimpleStringProperty(address);
        this.password = new SimpleStringProperty(password);
        this.username = new SimpleStringProperty(username);
        this.dateOfBirth = new SimpleObjectProperty<>(dateOfBirth);
        this.contactNumber = new SimpleStringProperty(contactNumber);
        this.email = new SimpleStringProperty(email);
        this.activePackageId = new SimpleIntegerProperty(activePackageId);
        this.idNumber = new SimpleStringProperty(idNumber);
        this.registrationDate = new SimpleObjectProperty<>(registrationDate);
        this.gender = new SimpleStringProperty(gender);
        this.emergencyContact = new SimpleStringProperty(emergencyContact);
        this.preferredLanguage = new SimpleStringProperty(preferredLanguage);
        this.previousExperience = new SimpleBooleanProperty(previousExperience);
        this.packageType = new SimpleStringProperty(packageType);
        this.notes = new SimpleStringProperty(notes);
        this.status = new SimpleStringProperty(status);
    }

    // Getters and setters for properties

    public IntegerProperty learnerIdProperty() {
        return learnerId;
    }
    public IntegerProperty active_package_IdProperty() {
        return activePackageId;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty addressProperty() {
        return address;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public ObjectProperty<LocalDate> dateOfBirthProperty() {
        return dateOfBirth;
    }

    public StringProperty emailProperty() {
        return email;
    }
public StringProperty contact_numberProperty() {
        return contactNumber;
    }
    public StringProperty idNumberProperty() {
        return idNumber;
    }

    public ObjectProperty<LocalDate> registrationDateProperty() {
        return registrationDate;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty emergencyContactProperty() {
        return emergencyContact;
    }

    public StringProperty preferredLanguageProperty() {
        return preferredLanguage;
    }

    public BooleanProperty previousExperienceProperty() {
        return previousExperience;
    }

    public StringProperty packageTypeProperty() {
        return packageType;
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public StringProperty statusProperty() {
        return status;
    }

    

    // Getters and setters for raw fields if needed
    public int getLearnerId() {
        return learnerId.get();
    }

    public void setLearnerId(int learnerId) {
        this.learnerId.set(learnerId);
    }
    public int getActive_package_id() {
        return activePackageId.get();
    }

    public void setActive_package_id(int active_package_id) {
        this.activePackageId.set(active_package_id);
    }

    public String getFullName() {
        return fullName.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
    public String getContact_Number() {
        return contactNumber.get();
    }

    public void setContact_Number(String contact_number) {
        this.contactNumber.set(contact_number);
    }

    public String getIdNumber() {
        return idNumber.get();
    }

    public void setIdNumber(String idNumber) {
        this.idNumber.set(idNumber);
    }

    public LocalDate getRegistrationDate() {
        return registrationDate.get();
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate.set(registrationDate);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getEmergencyContact() {
        return emergencyContact.get();
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact.set(emergencyContact);
    }

    public String getPreferredLanguage() {
        return preferredLanguage.get();
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage.set(preferredLanguage);
    }

    public boolean isPreviousExperience() {
        return previousExperience.get();
    }

    public void setPreviousExperience(boolean previousExperience) {
        this.previousExperience.set(previousExperience);
    }

    public String getPackageType() {
        return packageType.get();
    }

    public void setPackageType(String packageType) {
        this.packageType.set(packageType);
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    @Override
    public String toString() {
        return "Learner{" + "learnerId=" + learnerId + ", fullName=" + fullName + ", password=" + password + ", username=" + username + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", address=" + address + ", contact_number=" + contactNumber + ", email=" + email + ", active_package_id=" + activePackageId + ", idNumber=" + idNumber + ", registrationDate=" + registrationDate + ", emergencyContact=" + emergencyContact + ", preferredLanguage=" + preferredLanguage + ", previousExperience=" + previousExperience + ", packageType=" + packageType + ", notes=" + notes + ", status=" + status + '}';
    }

    

    
}
