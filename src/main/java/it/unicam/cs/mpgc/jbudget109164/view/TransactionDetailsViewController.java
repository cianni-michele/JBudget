package it.unicam.cs.mpgc.jbudget109164.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TransactionDetailsViewController {

    @FXML
    private Label dateLabel, descLabel, catLabel, totalLabel;
    @FXML
    private TableView<MovementRow> movementsTable;
    @FXML
    private TableColumn<MovementRow, String> accountCol, amountCol, noteCol;
    @FXML
    private Button addMovementButton, editTransactionButton, deleteTransactionButton;

    public TransactionDetailsViewController() {
    }

    @FXML
    public void initialize() {
        accountCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().account()));
        amountCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().amount()));
        noteCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().note()));

        dateLabel.setText("2025-01-01");
        descLabel.setText("Sample Transaction");
        catLabel.setText("Sample Category");
        totalLabel.setText("100.00");

        ObservableList<MovementRow> data = FXCollections.observableArrayList(
            new MovementRow("Account1", "50.00", "Note1"),
            new MovementRow("Account2", "50.00", "Note2")
        );
        movementsTable.setItems(data);
    }

    public void setTransaction(TransactionRow selectedItem) {

    }
}
