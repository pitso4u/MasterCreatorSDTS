package adminclerksdts;


import java.time.LocalDate;

public class LearnerPackage {
    private int id;
    private int learnerId;  // New field added
    private int packageId;
    private LocalDate startDate;
    private int testsRemaining;
    private int daysRemaining;
    private boolean packageCompleted;
    private LocalDate endDate;

    // Constructor
    public LearnerPackage(int id, int learnerId, int packageId, LocalDate startDate, int testsRemaining, int daysRemaining, boolean packageCompleted, LocalDate endDate) {
        this.id = id;
        this.learnerId = learnerId;  // Initialize the new field
        this.packageId = packageId;
        this.startDate = startDate;
        this.testsRemaining = testsRemaining;
        this.daysRemaining = daysRemaining;
        this.packageCompleted = packageCompleted;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(int learnerId) {
        this.learnerId = learnerId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getTestsRemaining() {
        return testsRemaining;
    }

    public void setTestsRemaining(int testsRemaining) {
        this.testsRemaining = testsRemaining;
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(int daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public boolean isPackageCompleted() {
        return packageCompleted;
    }

    public void setPackageCompleted(boolean packageCompleted) {
        this.packageCompleted = packageCompleted;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "LearnerPackage{" + "id=" + id + ", learnerId=" + learnerId + ", packageId=" + packageId + ", startDate=" + startDate + ", testsRemaining=" + testsRemaining + ", daysRemaining=" + daysRemaining + ", packageCompleted=" + packageCompleted + ", endDate=" + endDate + '}';
    }
    
}
