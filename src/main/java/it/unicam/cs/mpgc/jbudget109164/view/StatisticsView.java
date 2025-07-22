package it.unicam.cs.mpgc.jbudget109164.view;

import it.unicam.cs.mpgc.jbudget109164.controller.statistic.StatisticsController;
import it.unicam.cs.mpgc.jbudget109164.model.statistic.Statistics;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class StatisticsView {

    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00 €");

    private final StatisticsController statisticsController;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button calculateButton;

    @FXML
    private Label totalIncomeLabel;

    @FXML
    private Label totalExpensesLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label averageIncomeLabel;

    @FXML
    private Label averageExpensesLabel;


    public StatisticsView(StatisticsController statisticsController) {
        this.statisticsController = statisticsController;
    }

    @FXML
    public void initialize() {
        calculateButton.setOnAction(event -> calculateStatistics());
        clearLabels();
    }

    @FXML
    private void calculateStatistics() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showError("Seleziona entrambe le date");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showError("La data di inizio deve essere precedente alla data di fine");
            return;
        }

        try {
            Statistics statistics = statisticsController.getStatistics(startDate, endDate);
            displayStatistics(statistics);
        } catch (Exception e) {
            showError("Errore nel calcolo delle statistiche: " + e.getMessage());
        }
    }

    private void displayStatistics(Statistics statistics) {
        totalIncomeLabel.setText(currencyFormat.format(statistics.getTotalIncome()));
        totalExpensesLabel.setText(currencyFormat.format(Math.abs(statistics.getTotalExpenses())));
        balanceLabel.setText(currencyFormat.format(statistics.getBalance()));
        averageIncomeLabel.setText(currencyFormat.format(statistics.getAverageIncome()));
        averageExpensesLabel.setText(currencyFormat.format(Math.abs(statistics.getAverageExpenses())));
    }

    private void clearLabels() {
        totalIncomeLabel.setText("-");
        totalExpensesLabel.setText("-");
        balanceLabel.setText("-");
        averageIncomeLabel.setText("-");
        averageExpensesLabel.setText("-");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
