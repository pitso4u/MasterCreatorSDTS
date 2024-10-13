package adminclerksdts;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

public class ScreenManager extends BorderPane {

    private final HashMap<String, Node> screens = new HashMap<>();
    private final HashMap<String, ControlledScreen> controllers = new HashMap<>();
    private final Deque<String> screenHistory = new LinkedList<>();
    private String currentScreen;

    public ScreenManager() {
        super();
    }

    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    public Node getScreen(String name) {
        return screens.get(name);
    }

    public ControlledScreen getController(String name) {
        return controllers.get(name);
    }

    public void addController(String name, ControlledScreen controlledScreen) {
        controllers.put(name, controlledScreen);
    }
    
public <T extends ControlledScreen> boolean loadScreen(String name, String resource, Class<T> controllerClass) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent loadScreen = loader.load();
        T myScreenController = loader.getController();
        
        if (controllerClass.isInstance(myScreenController)) {
            myScreenController.setScreenParent(this);
            addScreen(name, loadScreen);
            addController(name, myScreenController);
            return true;
        } else {
            throw new IllegalArgumentException("Controller is not of type " + controllerClass.getName());
        }
    } catch (IOException | IllegalArgumentException e) {
        System.out.println(e.getMessage());
        return false;
    }
}
public boolean loadScreen(String screenID, String resource) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent loadedScreen = loader.load();
        ControlledScreen controller = loader.getController();
        controller.setScreenParent(this); // Assuming this method is present in ControlledScreen
        screens.put(screenID, loadedScreen);
        controllers.put(screenID, controller);
        return true;
    } catch (IOException e) {
        System.err.println("Error loading screen: " + screenID);
        e.printStackTrace();
    }
        return false;
}

   
    public boolean loadScreenIntoRegion(String name, BorderPane parentPane, String region) {
    if (screens.containsKey(name)) {
        Node screenToLoad = screens.get(name);

        // Remove the current content of the specified region
        switch (region.toLowerCase()) {
            case "center":
                parentPane.setCenter(null);
                break;
            case "top":
                parentPane.setTop(null);
                break;
            case "bottom":
                parentPane.setBottom(null);
                break;
            case "left":
                parentPane.setLeft(null);
                break;
            case "right":
                parentPane.setRight(null);
                break;
        }

        // Set the new content
        switch (region.toLowerCase()) {
            case "center":
                parentPane.setCenter(screenToLoad);
                break;
            case "top":
                parentPane.setTop(screenToLoad);
                break;
            case "bottom":
                parentPane.setBottom(screenToLoad);
                break;
            case "left":
                parentPane.setLeft(screenToLoad);
                break;
            case "right":
                parentPane.setRight(screenToLoad);
                break;
            default:
                System.out.println("Invalid region: " + region);
                return false;
        }

        currentScreen = name;
        return true;
    } else {
        System.out.println("Screen not found: " + name);
        return false;
    }
}
    public boolean setScreen(final String name) {
        if (screens.containsKey(name)) {
            // Track screen history
            if (currentScreen != null) {
                screenHistory.addLast(currentScreen);
            }

            // Replace the center content of the BorderPane
            setCenter(screens.get(name));
            currentScreen = name;
            return true;
        } else {
            showErrorMessage("Screen Not Loaded", "The requested screen has not been loaded.");
            return false;
        }
    }

    public boolean unloadCurrentScreen() {
        if (currentScreen != null) {
            ControlledScreen controller = controllers.remove(currentScreen);
            if (controller != null) {
                controller.cleanup();
            }
            screens.remove(currentScreen);
            setCenter(null);
            currentScreen = null;
            return true;
        } else {
            return false;
        }
    }

    public boolean unloadScreen(String name) {
        ControlledScreen controller = controllers.remove(name);
        if (controller != null) {
            controller.cleanup();
        }
        return screens.remove(name) != null;
    }

    public void goBack() {
        if (!screenHistory.isEmpty()) {
            String previousScreen = screenHistory.pollLast();
            setScreen(previousScreen);
        }
    }

    private void showErrorMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
