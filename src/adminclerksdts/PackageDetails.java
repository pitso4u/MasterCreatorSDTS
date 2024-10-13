package adminclerksdts;

import java.time.LocalDate;

public class PackageDetails {
    private int learnerId;
    private int packageId;
    private LocalDate startDate;
    private int testsRemaining;
    private int daysRemaining;
    private boolean packageCompleted;
    private LocalDate endDate;

    // Getters and setters for all fields

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
}

