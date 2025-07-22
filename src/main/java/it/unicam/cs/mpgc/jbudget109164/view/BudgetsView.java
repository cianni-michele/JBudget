package it.unicam.cs.mpgc.jbudget109164.view;

import it.unicam.cs.mpgc.jbudget109164.controller.budget.MonthlyBudgetController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.model.budget.Budget;
import it.unicam.cs.mpgc.jbudget109164.model.budget.BudgetDetails;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;

public class BudgetsView implements Initializable {

    @FXML private ComboBox<Tag> tagComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private ComboBox<Integer> monthComboBox;
    @FXML private TextField expectedAmountField;
    @FXML private Button createBudgetButton;
    @FXML private Button updateBudgetButton;
    @FXML private Button clearFormButton;
    @FXML private Button deleteBudgetButton;
    @FXML private TableView<BudgetTableRow> budgetsTable;
    @FXML private TableColumn<BudgetTableRow, String> periodColumn;
    @FXML private TableColumn<BudgetTableRow, String> tagColumn;
    @FXML private TableColumn<BudgetTableRow, String> expectedAmountColumn;
    @FXML private TableColumn<BudgetTableRow, String> actualAmountColumn;
    @FXML private TableColumn<BudgetTableRow, String> remainingColumn;
    @FXML private TableColumn<BudgetTableRow, String> progressColumn;
    @FXML private Button refreshButton;

    private final MonthlyBudgetController monthlyBudgetController;
    private final TagController tagController;
    private Budget<YearMonth> selectedBudget;

    public BudgetsView(MonthlyBudgetController monthlyBudgetController, TagController tagController) {
        this.monthlyBudgetController = monthlyBudgetController;
        this.tagController = tagController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupComboBoxes();
        setupTable();
        setupEventHandlers();
        loadTags();
        refreshBudgets();
    }

    private void setupComboBoxes() {
        int currentYear = YearMonth.now().getYear();
        for (int year = currentYear - 5; year <= currentYear + 5; year++) {
            yearComboBox.getItems().add(year);
        }
        yearComboBox.setValue(currentYear);

        for (int month = 1; month <= 12; month++) {
            monthComboBox.getItems().add(month);
        }
        monthComboBox.setValue(YearMonth.now().getMonthValue());

        tagComboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                setText(empty || tag == null ? null : tag.getName());
            }
        });
        tagComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                setText(empty || tag == null ? null : tag.getName());
            }
        });
    }

    private void setupTable() {
        periodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tagName"));
        expectedAmountColumn.setCellValueFactory(new PropertyValueFactory<>("expectedAmount"));
        actualAmountColumn.setCellValueFactory(new PropertyValueFactory<>("actualAmount"));
        remainingColumn.setCellValueFactory(new PropertyValueFactory<>("remaining"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));

        budgetsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectBudget(newSelection.getBudget());
                    } else {
                        clearSelection();
                    }
                });
    }

    private void setupEventHandlers() {
        createBudgetButton.setOnAction(event -> createBudget());
        updateBudgetButton.setOnAction(event -> updateBudget());
        clearFormButton.setOnAction(event -> clearForm());
        deleteBudgetButton.setOnAction(event -> deleteBudget());
        refreshButton.setOnAction(event -> refreshBudgets());
    }

    private void loadTags() {
        try {
            List<Tag> tags = tagController.getAllTags();
            tagComboBox.setItems(FXCollections.observableArrayList(tags));
        } catch (Exception e) {
            showError("Error loading tags: " + e.getMessage());
        }
    }

    private void createBudget() {
        try {
            BudgetDetails<YearMonth> details = createBudgetDetailsFromForm();
            monthlyBudgetController.createBudget(details);
            clearForm();
            refreshBudgets();
            showInfo("Budget created successfully");
        } catch (Exception e) {
            showError("Error creating budget: " + e.getMessage());
        }
    }

    private void updateBudget() {
        if (selectedBudget == null) return;

        try {
            BudgetDetails<YearMonth> details = createBudgetDetailsFromForm();
            monthlyBudgetController.updateBudget(selectedBudget.getId(), details);
            clearForm();
            refreshBudgets();
            showInfo("Budget updated successfully");
        } catch (Exception e) {
            showError("Error updating budget: " + e.getMessage());
        }
    }

    private void deleteBudget() {
        if (selectedBudget == null) return;

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete Budget");
        confirmation.setContentText("Are you sure you want to delete this budget?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                monthlyBudgetController.deleteBudget(selectedBudget.getId());
                clearForm();
                refreshBudgets();
                showInfo("Budget deleted successfully");
            } catch (Exception e) {
                showError("Error deleting budget: " + e.getMessage());
            }
        }
    }

    private BudgetDetails<YearMonth> createBudgetDetailsFromForm() {
        Tag selectedTag = tagComboBox.getValue();
        Integer year = yearComboBox.getValue();
        Integer month = monthComboBox.getValue();
        String amountText = expectedAmountField.getText();

        if (selectedTag == null || year == null || month == null || amountText.trim().isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }

        try {
            double amount = Double.parseDouble(amountText);
            YearMonth period = YearMonth.of(year, month);
            return new BudgetDetails<>(selectedTag, period, amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");
        }
    }

    private void selectBudget(Budget<YearMonth> budget) {
        selectedBudget = budget;
        tagComboBox.setValue(budget.getTag());
        yearComboBox.setValue(budget.getPeriod().getYear());
        monthComboBox.setValue(budget.getPeriod().getMonthValue());
        expectedAmountField.setText(String.valueOf(budget.getExpectedAmount()));

        updateBudgetButton.setDisable(false);
        deleteBudgetButton.setDisable(false);
        createBudgetButton.setDisable(true);
    }

    private void clearSelection() {
        selectedBudget = null;
        updateBudgetButton.setDisable(true);
        deleteBudgetButton.setDisable(true);
        createBudgetButton.setDisable(false);
    }

    private void clearForm() {
        tagComboBox.setValue(null);
        yearComboBox.setValue(YearMonth.now().getYear());
        monthComboBox.setValue(YearMonth.now().getMonthValue());
        expectedAmountField.clear();
        clearSelection();
    }

    private void refreshBudgets() {
        try {
            List<Budget<YearMonth>> budgets = monthlyBudgetController.getAllBudgets();
            ObservableList<BudgetTableRow> budgetRows = FXCollections.observableArrayList();

            for (Budget<YearMonth> budget : budgets) {
                double actualAmount;
                if (budget.getTag() == null){
                    actualAmount = monthlyBudgetController.getActualAmount(budget.getPeriod());
                } else {
                    actualAmount = monthlyBudgetController.getActualAmount(
                            budget.getTag().getId(),
                            budget.getPeriod()
                    );
                }
                budgetRows.add(new BudgetTableRow(budget, actualAmount));
            }

            budgetsTable.setItems(budgetRows);
        } catch (Exception e) {
            showError("Error loading budgets: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Operation Completed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class BudgetTableRow {
        private final SimpleStringProperty period;
        private final SimpleStringProperty tagName;
        private final SimpleStringProperty expectedAmount;
        private final SimpleStringProperty actualAmount;
        private final SimpleStringProperty remaining;
        private final SimpleStringProperty progress;
        private final Budget<YearMonth> budget;

        public BudgetTableRow(Budget<YearMonth> budget, double actualAmount) {
            this.budget = budget;
            this.period = new SimpleStringProperty(budget.getPeriod().toString());
            if (budget.getTag() == null) {
                this.tagName = new SimpleStringProperty("No Tag");
            } else {
                this.tagName = new SimpleStringProperty(budget.getTag().getName());
            }

            DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 €");
            this.expectedAmount = new SimpleStringProperty(currencyFormat.format(budget.getExpectedAmount()));
            this.actualAmount = new SimpleStringProperty(currencyFormat.format(actualAmount));

            double remaining = budget.getExpectedAmount() - Math.abs(actualAmount);
            this.remaining = new SimpleStringProperty(currencyFormat.format(remaining));

            double progressPercent = budget.getExpectedAmount() > 0 ?
                    (Math.abs(actualAmount) / budget.getExpectedAmount()) * 100 : 0;
            this.progress = new SimpleStringProperty(String.format("%.1f%%", progressPercent));
        }

        public String getPeriod() { return period.get(); }
        public String getTagName() { return tagName.get(); }
        public String getExpectedAmount() { return expectedAmount.get(); }
        public String getActualAmount() { return actualAmount.get(); }
        public String getRemaining() { return remaining.get(); }
        public String getProgress() { return progress.get(); }
        public Budget<YearMonth> getBudget() { return budget; }
    }
}