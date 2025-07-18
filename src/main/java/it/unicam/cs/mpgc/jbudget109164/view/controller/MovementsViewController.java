package it.unicam.cs.mpgc.jbudget109164.view.controller;

import it.unicam.cs.mpgc.jbudget109164.controller.movement.MovementController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.util.io.FXMLResourceLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class MovementsViewController {

    private final MovementController movementController;
    private final TagController tagController;
    private final ObservableList<Movement> movementsList;

    @FXML
    private TableView<Movement> movementsTable;

    @FXML
    private TableColumn<Movement, String> dateColumn;

    @FXML
    private TableColumn<Movement, String> descriptionColumn;

    @FXML
    private TableColumn<Movement, Double> amountColumn;

    @FXML
    private Button addMovementButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button refreshButton;


    public MovementsViewController(MovementController movementController,
                                   TagController tagController) {
        this.movementController = movementController;
        this.tagController = tagController;
        this.movementsList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTable();
        setupButtons();
        loadMovements();
    }

    private void setupTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        amountColumn.setCellFactory(column -> new TableCell<Movement, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", amount));
                }
            }
        });

        movementsTable.setItems(movementsList);

        viewDetailsButton.setDisable(true);
        movementsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) ->
                        viewDetailsButton.setDisable(newSelection == null)
        );

        movementsTable.setRowFactory(tv -> {
            TableRow<Movement> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    openMovementDetails(row.getItem());
                }
            });
            return row;
        });
    }

    private void setupButtons() {
        addMovementButton.setOnAction(event -> handleAddMovement());
        viewDetailsButton.setOnAction(event -> handleViewDetails());
        refreshButton.setOnAction(event -> loadMovements() );
    }

    private void handleAddMovement() {
        try {
            Dialog<Movement> dialog = createAddMovementDialog();

            dialog.showAndWait().ifPresent(newMovement -> {
                loadMovements();
                showSuccessAlert("Movement added successfully!");
            });

        } catch (Exception e){
           showErrorAlert("Error adding movement: " + e.getMessage());
        }
    }

    private Dialog<Movement> createAddMovementDialog() {
        Dialog<Movement> dialog = new Dialog<>();
        dialog.setTitle("Add New Movement");
        dialog.setHeaderText("Enter movement details");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        TextField descriptionField = new TextField();
        TextField amountField = new TextField();

        descriptionField.setPromptText("Description");
        amountField.setPromptText("Amount");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Amount:"), 0, 2);
        grid.add(amountField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        Runnable validateFields = () -> {
            boolean valid = datePicker.getValue() != null &&
                    !descriptionField.getText().trim().isEmpty() &&
                    !amountField.getText().trim().isEmpty();
            okButton.setDisable(!valid);
        };

        datePicker.valueProperty().addListener((obs, old, val) -> validateFields.run());
        descriptionField.textProperty().addListener((obs, old, val) -> validateFields.run());
        amountField.textProperty().addListener((obs, old, val) -> validateFields.run());

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    return movementController.createMovement(
                            datePicker.getValue(),
                            descriptionField.getText().trim(),
                            Double.parseDouble(amountField.getText().trim())
                    );
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid amount format");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    private void handleViewDetails() {
        Movement selectedMovement = movementsTable.getSelectionModel().getSelectedItem();
        if (selectedMovement != null) {
            openMovementDetails(selectedMovement);
        }
    }

    private void openMovementDetails(Movement movement) {
        try {
            FXMLLoader loader = FXMLResourceLoader.getLoader("MovementDetails");
            MovementDetailsViewController controller = new MovementDetailsViewController(movementController, tagController);
            loader.setController(controller);

            Stage detailsStage = new Stage();
            detailsStage.setTitle("Movement Details");
            detailsStage.setScene(new Scene(loader.load()));
            detailsStage.initModality(Modality.APPLICATION_MODAL);

            controller.setMovement(movement);
            controller.setStage(detailsStage);

            detailsStage.showAndWait();

            loadMovements();
        } catch (Exception e) {
            showErrorAlert("Error opening movement details: " + e.getMessage());
        }
    }

    private void loadMovements() {
        try {
            List<Movement> movements = movementController.getMovements(0, 20, "date", true);
            movementsList.setAll(movements);
        } catch (Exception e) {
            showErrorAlert("Error loading movements" + e.getMessage());
        }

    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
