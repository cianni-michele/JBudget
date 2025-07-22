package it.unicam.cs.mpgc.jbudget109164.view;

import it.unicam.cs.mpgc.jbudget109164.controller.movement.MovementController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MovementDetailsView {

    private final MovementController movementController;
    private final TagController tagController;
    private Movement currentMovement;
    private Tag selectedTagForRemoval;
    private Stage stage;

    @FXML
    private Label dateLabel;

    @FXML
    private Label descLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private ComboBox<Tag> tagsComboBox;

    @FXML
    private FlowPane tagsFlowPane;

    @FXML
    private Button addTagButton;

    @FXML
    private Button removeTagButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    public MovementDetailsView(MovementController movementController,
                               TagController tagController) {
        this.movementController = movementController;
        this.tagController = tagController;
    }

    @FXML
    public void initialize() {
        addTagButton.setOnAction(event -> handleAddTag());
        removeTagButton.setOnAction(event -> handleRemoveTag());
        editButton.setOnAction(event -> handleEdit());
        deleteButton.setOnAction(event -> handleDelete());

        tagsComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tag tag) {
                return tag == null ? null : tag.getName();
            }

            @Override
            public Tag fromString(String string) {
                return null;
            }
        });

        loadAvailableTags();

        removeTagButton.setDisable(true);
    }

    private void loadAvailableTags() {
        try {
            List<Tag> allTags = tagController.getAllTags();
            tagsComboBox.getItems().setAll(allTags);
        } catch (Exception e) {
            showAlertError("Error loading tags: " + e.getMessage());
        }
    }

    private void handleAddTag() {
        Tag selectedTag = tagsComboBox.getValue();
        if (selectedTag == null) {
            showAlertError("Please select a tag to add.");
            return;
        }

        if (currentMovement.getTags().contains(selectedTag)) {
            showAlertError("This tag is already added to the movement.");
            return;
        }

        try {
            Movement updatedMovement = movementController.addTagToMovement(
                    selectedTag.getId(),
                    currentMovement.getId()
            );
            setMovement(updatedMovement);

            // Reset della selezione
            tagsComboBox.setValue(null);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Tag Added");
            successAlert.setHeaderText("Tag successfully added");
            successAlert.setContentText("Tag '" + selectedTag.getName() + "' has been added to the movement.");
            successAlert.showAndWait();
        } catch (Exception e) {
            showAlertError("Failed to add tag: " + e.getMessage());
        }
    }

    private StringConverter<Tag> getTagStringConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Tag tag) {
                return tag.getName();
            }

            @Override
            public Tag fromString(String string) {
                return null; // Not needed for this use case
            }
        };
    }

    private void handleRemoveTag() {
        if (selectedTagForRemoval == null || currentMovement == null) {
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Remove Tag");
        confirmation.setHeaderText("Remove tag from movement");
        confirmation.setContentText("Are you sure you want to remove the tag '" + selectedTagForRemoval.getName() + "'?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Movement updatedMovement = movementController.removeTagFromMovement(
                            selectedTagForRemoval.getId(),
                            currentMovement.getId()
                    );
                    setMovement(updatedMovement);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Tag Removed");
                    successAlert.setHeaderText("Tag successfully removed");
                    successAlert.setContentText("Tag '" + selectedTagForRemoval.getName() + "' has been removed from the movement.");
                    successAlert.showAndWait();
                } catch (Exception e) {
                    showAlertError("Failed to remove tag: " + e.getMessage());
                }
            }
        });
    }

    private void handleEdit() {
        if (currentMovement == null) {
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Movement");
        dialog.setHeaderText("Edit Movement Details");

        DatePicker datePicker = new DatePicker(currentMovement.getDate());
        TextField descField = new TextField(currentMovement.getDescription());
        TextField amountField = new TextField(String.valueOf(currentMovement.getAmount()));

        descField.setPromptText("Description");
        amountField.setPromptText("Amount");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Amount:"), 0, 2);
        grid.add(amountField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

        Runnable validateFields = () -> {
            boolean valid = datePicker.getValue() != null &&
                            !descField.getText().trim().isEmpty() &&
                            !amountField.getText().trim().isEmpty() &&
                            isValidAmount(amountField.getText().trim());
            okButton.setDisable(!valid);
        };

        datePicker.valueProperty().addListener((obs, old, val) -> validateFields.run());
        descField.textProperty().addListener((obs, old, val) -> validateFields.run());
        amountField.textProperty().addListener((obs, old, val) -> validateFields.run());

        validateFields.run();

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    LocalDate newDate = datePicker.getValue();
                    String newDescription = descField.getText().trim();
                    double newAmount = Double.parseDouble(amountField.getText().trim());

                    Movement updatedMovement = movementController.updateMovement(
                            currentMovement.getId(),
                            newDate,
                            newDescription,
                            newAmount
                    );

                    setMovement(updatedMovement);

                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Movement Updated");
                    successAlert.setHeaderText("Movement successfully updated");
                    successAlert.setContentText("The movement has been updated with the new details.");
                    successAlert.showAndWait();

                } catch (NumberFormatException e) {
                    showAlertError("Invalid amount format. Please enter a valid number.");
                } catch (Exception e) {
                    showAlertError("Failed to update movement: " + e.getMessage());
                }
            }
        });
    }

    private boolean isValidAmount(String amountText) {
        try {
            Double.parseDouble(amountText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void handleDelete() {
        if (currentMovement == null) {
            return;
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Movement");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    movementController.deleteMovement(currentMovement.getId());
                    stage.close();
                } catch (Exception e) {
                    showAlertError("Failed to delete movement");
                }
            }
        });
    }

    private void showAlertError(String message) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    public void setMovement(Movement movement) {
        this.currentMovement = movement;
        updateView();
    }

    private void updateView() {
        if (currentMovement != null) {
            dateLabel.setText(currentMovement.getDate().toString());
            descLabel.setText(currentMovement.getDescription());
            amountLabel.setText(String.valueOf(currentMovement.getAmount()));
            updateTagsDisplay();
        }
    }

    private void updateTagsDisplay() {
        tagsFlowPane.getChildren().clear();
        selectedTagForRemoval = null;
        removeTagButton.setDisable(true);

        if (currentMovement.getTags().isEmpty()) {
            Label noTagsLabel = new Label("No tags assigned");
            noTagsLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
            tagsFlowPane.getChildren().add(noTagsLabel);
        } else {
            for (Tag tag : currentMovement.getTags()) {
                Button tagButton = createTagButton(tag);
                tagsFlowPane.getChildren().add(tagButton);
            }
        }

        // Filtra i tag nella ComboBox per mostrare solo quelli non ancora assegnati
        updateComboBoxAvailableTags();
    }

    private Button createTagButton(Tag tag) {
        Button tagButton = new Button(tag.getName());
        tagButton.setStyle(
                "-fx-background-color: #3498db; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 5 10 5 10; " +
                "-fx-cursor: hand; " +
                "-fx-font-size: 12px;"
        );

        // Effetto hover
        tagButton.setOnMouseEntered(e -> tagButton.setStyle(
                "-fx-background-color: #2980b9; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 5 10 5 10; " +
                "-fx-cursor: hand; " +
                "-fx-font-size: 12px;"
        ));

        tagButton.setOnMouseExited(e -> {
            if (!tag.equals(selectedTagForRemoval)) {
                tagButton.setStyle(
                        "-fx-background-color: #3498db; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-padding: 5 10 5 10; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-size: 12px;"
                );
            }
        });

        // Click per selezionare il tag da rimuovere
        tagButton.setOnAction(e -> {
            // Reset stile di tutti i pulsanti
            tagsFlowPane.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    ((Button) node).setStyle(
                            "-fx-background-color: #3498db; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 15; " +
                            "-fx-padding: 5 10 5 10; " +
                            "-fx-cursor: hand; " +
                            "-fx-font-size: 12px;"
                    );
                }
            });

            // Evidenzia il tag selezionato
            tagButton.setStyle(
                    "-fx-background-color: #e74c3c; " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 15; " +
                    "-fx-padding: 5 10 5 10; " +
                    "-fx-cursor: hand; " +
                    "-fx-font-size: 12px;"
            );

            selectedTagForRemoval = tag;
            removeTagButton.setDisable(false);
        });

        return tagButton;
    }

    private void updateComboBoxAvailableTags() {
        try {
            List<Tag> allTags = tagController.getAllTags();
            List<Tag> availableTags = allTags.stream()
                    .filter(tag -> !currentMovement.getTags().contains(tag))
                    .collect(Collectors.toList());

            tagsComboBox.getItems().setAll(availableTags);

            if (availableTags.isEmpty()) {
                tagsComboBox.setPromptText("All tags are already assigned");
                addTagButton.setDisable(true);
            } else {
                tagsComboBox.setPromptText("Select a tag to add...");
                addTagButton.setDisable(false);
            }
        } catch (Exception e) {
            showAlertError("Error updating available tags: " + e.getMessage());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
