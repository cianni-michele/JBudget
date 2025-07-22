package it.unicam.cs.mpgc.jbudget109164.util.io;

import javafx.fxml.FXMLLoader;

import java.net.URL;

public final class FXMLResourceLoader {

    private static final String FXML_EXTENSION = ".fxml";
    private static final String RESOURCE_VIEW_PATH = "/view";

    public static FXMLLoader getLoader(String fxmlFile) {
        URL fxmlLocation = FXMLResourceLoader.class.getResource(RESOURCE_VIEW_PATH + "/" + fxmlFile + FXML_EXTENSION);
        if (fxmlLocation == null) {
            throw new RuntimeException("FXML file not found: " + fxmlFile);
        }
        return new FXMLLoader(fxmlLocation);
    }
}
