package it.unicam.cs.mpgc.jbudget109164.view;

import it.unicam.cs.mpgc.jbudget109164.controller.movement.MovementController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.model.movement.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.util.io.FXMLResourceLoader;
import it.unicam.cs.mpgc.jbudget109164.util.time.RecurrenceType;
import it.unicam.cs.mpgc.jbudget109164.util.time.ScheduledPeriod;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import java.util.UUID;
import java.util.stream.Collectors;

public class MovementsView {

    private static final int ITEMS_PER_PAGE = 20;

    private final MovementController movementController;
    private final TagController tagController;
    private final ObservableList<Movement> movementsList;

    private int currentPage = 0;
    private String currentSortBy = "date";
    private boolean currentAscending = true;

    @FXML
    private TableView<Movement> movementsTable;

    @FXML
    private TableColumn<Movement, String> dateColumn;

    @FXML
    private TableColumn<Movement, String> descriptionColumn;

    @FXML
    private TableColumn<Movement, Double> amountColumn;

    @FXML
    private TableColumn<Movement, String> tagsColumn;

    @FXML
    private Button addMovementButton;

    @FXML
    private Button addScheduledMovementsButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Pagination movementsPagination;



    public MovementsView(MovementController movementController,
                         TagController tagController) {
        this.movementController = movementController;
        this.tagController = tagController;
        this.movementsList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTable();
        setupPagination();
        setupButtons();
        loadMovements();
    }

    private void setupTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tagsColumn.setCellValueFactory(cellData -> {
            Movement movement = cellData.getValue();
            if (movement.getTags().isEmpty()) {
                return new SimpleStringProperty("No tags");
            }
            String tagNames = movement.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(tagNames);
        });

        dateColumn.setSortable(true);
        descriptionColumn.setSortable(true);
        amountColumn.setSortable(true);

        movementsTable.getSortOrder().addListener(
                (ListChangeListener<TableColumn<Movement, ?>>) change -> {
                    if (!movementsTable.getSortOrder().isEmpty()) {
                        TableColumn<Movement, ?> sortedColumn = movementsTable.getSortOrder().getFirst();

                        if (sortedColumn == dateColumn) {
                            currentSortBy = "date";
                        } else if (sortedColumn == descriptionColumn) {
                            currentSortBy = "description";
                        } else if (sortedColumn == amountColumn) {
                            currentSortBy = "amount";
                        }

                        currentAscending = sortedColumn.getSortType() == TableColumn.SortType.ASCENDING;

                        currentPage = 0;
                        movementsPagination.setCurrentPageIndex(0);
                        loadMovements();
                    }
                }
        );

        dateColumn.setSortType(TableColumn.SortType.ASCENDING);
        movementsTable.getSortOrder().add(dateColumn);

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

    private void setupPagination() {
        movementsPagination.setCurrentPageIndex(0);
        movementsPagination.currentPageIndexProperty().addListener((obs, oldPage, newPage) -> {
            currentPage = newPage.intValue() * ITEMS_PER_PAGE;
            loadMovements();
        });
        updatePaginationCount();
    }

    private void setupButtons() {
        addMovementButton.setOnAction(event -> handleAddMovement());
        addScheduledMovementsButton.setOnAction(event -> handleAddScheduledMovements());
        viewDetailsButton.setOnAction(event -> handleViewDetails());
        refreshButton.setOnAction(event -> {
            currentPage = 0;
            movementsPagination.setCurrentPageIndex(0);
            loadMovements();
        });
    }

    private void handleAddMovement() {
        try {
            Dialog<Movement> dialog = createAddMovementDialog();

            dialog.showAndWait().ifPresent(newMovement -> {
                loadMovements();
                showSuccess("Movement added successfully!");
            });

        } catch (Exception e) {
            showError("Error adding movement: " + e.getMessage());
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
                    showError("Invalid amount format");
                    return null;
                }
            }
            return null;
        });

        return dialog;
    }

    private void handleAddScheduledMovements() {
        try {
            Dialog<List<Movement>> dialog = createScheduledMovementsDialog();

            dialog.showAndWait().ifPresent(scheduledMovements -> {
                loadMovements();
                showSuccess(scheduledMovements.size() + " scheduled movements added successfully!");
            });

        } catch (Exception e) {
            showError("Error adding scheduled movements: " + e.getMessage());
        }
    }

    private Dialog<List<Movement>> createScheduledMovementsDialog() {
        Dialog<List<Movement>> dialog = new Dialog<>();
        dialog.setTitle("Add Scheduled Movements");
        dialog.setHeaderText("Create recurring movements");

        TextField descriptionField = new TextField();
        TextField amountField = new TextField();
        DatePicker fromDatePicker = new DatePicker(LocalDate.now());
        DatePicker toDatePicker = new DatePicker(LocalDate.now().plusMonths(12));
        ComboBox<String> frequencyCombo = new ComboBox<>();
        Spinner<Integer> dayOfMonthSpinner = new Spinner<>(1, 31, 1);

        ComboBox<Tag> tagComboBox = new ComboBox<>();
        ListView<Tag> selectedTagsList = new ListView<>();
        selectedTagsList.setPrefHeight(80);

        tagComboBox.setPromptText("Select tags...");
        tagComboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setText(null);
                } else {
                    setText(tag.getName());
                }
            }
        });

        tagComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setText("Select tags...");
                } else {
                    setText(tag.getName());
                }
            }
        });

        tagComboBox.setOnAction(event -> {
            Tag selectedTag = tagComboBox.getSelectionModel().getSelectedItem();
            if (selectedTag != null && !selectedTagsList.getItems().contains(selectedTag)) {
                selectedTagsList.getItems().add(selectedTag);
                tagComboBox.getSelectionModel().clearSelection();
            }
        });

        selectedTagsList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                if (empty || tag == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(tag.getName());

                    Button removeButton = new Button("×");
                    removeButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 2px 6px;");
                    removeButton.setOnAction(e -> selectedTagsList.getItems().remove(tag));
                    setGraphic(removeButton);
                }
            }
        });

        descriptionField.setPromptText("Movement description");
        amountField.setPromptText("Amount (+ for income, - for expense)");

        frequencyCombo.getItems().addAll("MONTHLY", "QUARTERLY", "YEARLY");
        frequencyCombo.setValue("MONTHLY");

        try {
            List<Tag> availableTags = tagController.getAllTags();
            tagComboBox.getItems().setAll(availableTags);
        } catch (Exception e) {
            showError("Error loading tags: " + e.getMessage());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Description:"), 0, 0);
        grid.add(descriptionField, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);
        grid.add(new Label("From Date:"), 0, 2);
        grid.add(fromDatePicker, 1, 2);
        grid.add(new Label("To Date:"), 0, 3);
        grid.add(toDatePicker, 1, 3);
        grid.add(new Label("Frequency:"), 0, 4);
        grid.add(frequencyCombo, 1, 4);
        grid.add(new Label("Day of Month:"), 0, 5);
        grid.add(dayOfMonthSpinner, 1, 5);
        grid.add(new Label("Select Tags:"), 0, 6);
        grid.add(tagComboBox, 1, 6);
        grid.add(new Label("Selected Tags:"), 0, 7);
        grid.add(selectedTagsList, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        Runnable validateFields = () -> {
            boolean valid = !descriptionField.getText().trim().isEmpty() &&
                            !amountField.getText().trim().isEmpty() &&
                            fromDatePicker.getValue() != null &&
                            toDatePicker.getValue() != null &&
                            fromDatePicker.getValue().isBefore(toDatePicker.getValue()) &&
                            frequencyCombo.getValue() != null;
            okButton.setDisable(!valid);
        };

        descriptionField.textProperty().addListener((obs, old, val) -> validateFields.run());
        amountField.textProperty().addListener((obs, old, val) -> validateFields.run());
        fromDatePicker.valueProperty().addListener((obs, old, val) -> validateFields.run());
        toDatePicker.valueProperty().addListener((obs, old, val) -> validateFields.run());
        frequencyCombo.valueProperty().addListener((obs, old, val) -> validateFields.run());

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    String description = descriptionField.getText().trim();

                    List<UUID> selectedTagIds = selectedTagsList.getItems()
                            .stream()
                            .map(Tag::getId)
                            .toList();

                    ScheduledPeriod scheduledPeriod = ScheduledPeriod.of(
                            fromDatePicker.getValue(),
                            toDatePicker.getValue(),
                            RecurrenceType.valueOf(frequencyCombo.getValue()),
                            dayOfMonthSpinner.getValue()
                    );

                    return movementController.generateScheduledMovements(
                            amount, description, selectedTagIds, scheduledPeriod
                    );

                } catch (NumberFormatException e) {
                    showError("Invalid amount format");
                    return null;
                } catch (Exception e) {
                    showError("Error creating scheduled movements: " + e.getMessage());
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
            MovementDetailsView controller = new MovementDetailsView(movementController, tagController);
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
            showError("Error opening movement details: " + e.getMessage());
        }
    }

    private void loadMovements() {
        try {
            List<Movement> movements = movementController.getMovements(
                    currentPage,
                    ITEMS_PER_PAGE,
                    currentSortBy,
                    currentAscending
            );
            movementsList.setAll(movements);
            updatePaginationCount();
        } catch (Exception e) {
            showError("Error loading movements" + e.getMessage());
        }
    }

    private void updatePaginationCount() {
        try {
            int totalMovements = movementController.getTotalMovementsCount();
            int pageCount = (int) Math.ceil((double) totalMovements / ITEMS_PER_PAGE);
            movementsPagination.setPageCount(Math.max(1, pageCount));
        } catch (Exception e) {
            movementsPagination.setPageCount(1);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
