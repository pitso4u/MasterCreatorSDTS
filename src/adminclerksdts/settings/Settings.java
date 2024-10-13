
package adminclerksdts.settings;

public class Settings {
    private int maxSeats;

    // Singleton instance
    private static Settings instance;

    // Private constructor to prevent instantiation
    private Settings() {
        // Default value for maxSeats
        maxSeats = 20; // Default value, can be adjusted as needed
    }

    // Get singleton instance
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    // Getters and setters for maxSeats
    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }
}

