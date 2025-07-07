package it.unicam.cs.mpgc.jbudget109164.utils.io;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public final class FXMLResourceLoader {

    private static final String FXML_EXTENSION = ".fxml";
    private static final String RESOURCE_VIEW_PATH = "/view";

    public static Parent loadView(String fxmlFile) {
        URL fxmlLocation = FXMLResourceLoader.class.getResource(RESOURCE_VIEW_PATH + "/" + fxmlFile + FXML_EXTENSION);

        if (fxmlLocation == null) {
            throw new RuntimeException("FXML file not found: " + fxmlFile);
        }

        try {
            return FXMLLoader.load(fxmlLocation);
        } catch (IOException e) {
            throw new RuntimeException("Error during loading view: " + fxmlFile, e);
        }
    }

    public static <T> T loadController(String fxmlFile) {
        URL fxmlLocation = FXMLResourceLoader.class.getResource(RESOURCE_VIEW_PATH + "/" + fxmlFile + FXML_EXTENSION);

        if (fxmlLocation == null) {
            throw new RuntimeException("FXML file not found: " + fxmlFile);
        }

        try {
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Error during loading controller: " + fxmlFile, e);
        }
    }

}
