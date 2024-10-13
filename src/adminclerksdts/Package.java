package adminclerksdts;

public class Package {
    private int packageId;
    private String name;
    private int durationDays;
    private int totalTests;

    // Constructors
    public Package() {
    }

    public Package(int packageId, String name, int durationDays, int totalTests) {
        this.packageId = packageId;
        this.name = name;
        this.durationDays = durationDays;
        this.totalTests = totalTests;
    }

    // Getters and Setters
    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

   @Override
public String toString() {
    return name + " - " + durationDays + " days, " + totalTests + " tests";
}

}
