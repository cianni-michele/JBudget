package it.unicam.cs.mpgc.jbudget109164.view.controller;

import it.unicam.cs.mpgc.jbudget109164.controller.movement.MovementController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;

public class MovementDetailsViewController {

    private final MovementController movementController;
    private final TagController tagController;
    private Movement currentMovement;
    private Stage stage;

    @FXML
    private Label dateLabel, descLabel, amountLabel;

    @FXML
    private Button addTagButton, editButton, deleteButton;

    public MovementDetailsViewController(MovementController movementController,
                                         TagController tagController) {
        this.movementController = movementController;
        this.tagController = tagController;
    }

    @FXML
    public void initialize() {
        addTagButton.setOnAction(event -> handleAddTag());

        editButton.setOnAction(event -> handleEdit());

        deleteButton.setOnAction(event -> handleDelete());
    }

    private void handleAddTag() {
        if (currentMovement == null) {
            return;
        }

        List<Tag> availableTags = tagController.getAllTags();

        if (availableTags.isEmpty()) {
            showAlertError("No tags available to add.");
            return;
        }

        Dialog<Tag> dialog = new Dialog<>();
        dialog.setTitle("Add tag");
        dialog.setHeaderText("Select a tag to add to the movement");

        ComboBox<Tag> tagComboBox = new ComboBox<>();
        tagComboBox.getItems().addAll(availableTags);
        tagComboBox.setConverter(getTagStringConverter());

        dialog.getDialogPane().setContent(tagComboBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        tagComboBox.valueProperty().addListener(
                (obs, oldTag, newTag) -> okButton.setDisable(newTag == null)
        );

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return tagComboBox.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(selectedTag -> {
            try {
                Movement updatedMovement = movementController.addTagToMovement(selectedTag.getId(), currentMovement.getId());
                setMovement(updatedMovement);

                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Tag Added");
                successAlert.setHeaderText("Tag successfully added to movement");
                successAlert.setContentText("Tag: " + selectedTag.getName() + " has been added to the movement.");
                successAlert.showAndWait();
            } catch (Exception e) {
                showAlertError("Failed to add tag to movement");
            }
        });
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

    private void handleEdit() {
        if (currentMovement == null) {
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Movement");
        dialog.setHeaderText("Edit Movement Details");

        // Crea i campi di input
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

        // Validazione dei campi
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

        Runnable validateFields = () -> {
            boolean valid = datePicker.getValue() != null &&
                    !descField.getText().trim().isEmpty() &&
                    !amountField.getText().trim().isEmpty() &&
                    isValidAmount(amountField.getText().trim());
            okButton.setDisable(!valid);
        };

        // Listener per validazione in tempo reale
        datePicker.valueProperty().addListener((obs, old, val) -> validateFields.run());
        descField.textProperty().addListener((obs, old, val) -> validateFields.run());
        amountField.textProperty().addListener((obs, old, val) -> validateFields.run());

        // Validazione iniziale
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
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
