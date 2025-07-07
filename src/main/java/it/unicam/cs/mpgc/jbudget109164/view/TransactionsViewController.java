package it.unicam.cs.mpgc.jbudget109164.view;

import it.unicam.cs.mpgc.jbudget109164.utils.io.FXMLResourceLoader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class TransactionsViewController {

    @FXML
    private TableView<TransactionRow> transactionsTable;
    @FXML
    private TableColumn<TransactionRow, String> dateCol;
    @FXML
    private TableColumn<TransactionRow, String> descCol;
    @FXML
    private TableColumn<TransactionRow, String> catCol;
    @FXML
    private TableColumn<TransactionRow, String> amountCol;

    @FXML
    public void initialize() {
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().date()));
        descCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().description()));
        catCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().category()));
        amountCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().balance()));

        ObservableList<TransactionRow> data = FXCollections.observableArrayList(
                new TransactionRow("2023-10-01", "Grocery Shopping", "Food", "50.00"),
                new TransactionRow("2023-10-02", "Salary", "Income", "2000.00")
        );

        transactionsTable.setItems(data);

        transactionsTable.widthProperty().addListener(
                (obs, oldVal, newVal) -> {
                    double width = newVal.doubleValue();
                    dateCol.setPrefWidth(width * 0.2);
                    descCol.setPrefWidth(width * 0.3);
                    catCol.setPrefWidth(width * 0.2);
                    amountCol.setPrefWidth(width * 0.3);
                }
        );

        transactionsTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !transactionsTable.getSelectionModel().isEmpty()) {
                String fxmlFile = "TransactionDetails";
                Parent detailsView = FXMLResourceLoader.loadView(fxmlFile);

                TransactionDetailsViewController detailsController = FXMLResourceLoader.loadController(fxmlFile);
                TransactionRow selectedItem = transactionsTable.getSelectionModel().getSelectedItem();
                detailsController.setTransaction(selectedItem);


                BorderPane root = (BorderPane) transactionsTable.getScene().getRoot();
                root.setCenter(detailsView);
            }
        });
    }

}
