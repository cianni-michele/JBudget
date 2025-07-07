package it.unicam.cs.mpgc.jbudget109164.view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public abstract class CustomView<T> extends BorderPane {

    protected final ObservableList<T> itemsList;
    protected final ListView<T> listView;

    protected CustomView(ObservableList<T> itemsList) {
        this.itemsList = itemsList;
        this.listView = new ListView<>(itemsList);
    }

    protected abstract void initializeView();

    protected abstract void loadItems();

    protected void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        if (message == null || message.isEmpty()) {
            alert.setContentText("An unexpected error occurred.");
        }
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void setupListView() {
        setCenter(listView);
    }

    protected void setupRightPanel(Node... nodes) {
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(20));
        rightPanel.getChildren().addAll(nodes);
        setRight(rightPanel);
    }
}
