package it.unicam.cs.mpgc.jbudget109164.view.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.account.Account;
import it.unicam.cs.mpgc.jbudget109164.view.CustomForm;
import javafx.collections.FXCollections;
import javafx.scene.control.*;

import java.util.List;
import java.util.function.Consumer;

public class MovementCreationForm extends CustomForm<MovementFormData> {

    private TextField amountField;
    private TextField descriptionField;
    private ComboBox<Account> accountComboBox;
    private Button addButton;

    public MovementCreationForm(Consumer<MovementFormData> onSubmit) {
        super(onSubmit);
    }

    @Override
    protected void setupGap() {
        setHgap(10);
        setVgap(10);
    }

    @Override
    protected void setupComponents() {
        amountField = new TextField();
        amountField.setPromptText("Amount");

        descriptionField = new TextField();
        descriptionField.setPromptText("Movement Description");

        accountComboBox = new ComboBox<>();
        accountComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                setText(empty || account == null ? null : account.getName());
            }
        });
        accountComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                setText(empty || account == null ? null : account.getName());
            }
        });

        addButton = new Button("Add Movement");
        addButton.setDisable(true);
    }

    @Override
    protected void setupLayout() {
        add(new Label("Amount:"), 0, 0);
        add(amountField, 1, 0);

        add(new Label("Description:"), 0, 1);
        add(descriptionField, 1, 1);

        add(new Label("Account:"), 0, 2);
        add(accountComboBox, 1, 2);

        add(addButton, 1, 3);
    }

    @Override
    protected void handleEvents(Consumer<MovementFormData> onSubmit) {
        addButton.setOnAction(e -> {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();
            Account selectedAccount = accountComboBox.getSelectionModel().getSelectedItem();

            if (selectedAccount == null) {
                throw new IllegalArgumentException("Please select an account.");
            }

            onSubmit.accept(new MovementFormData(amount, description, selectedAccount));
            clearFields();
        });
    }

    public void setAddButtonDisabled(boolean disabled) {
        addButton.setDisable(disabled);
    }

    public void updateAccounts(List<Account> accounts) {
        accountComboBox.setItems(FXCollections.observableArrayList(accounts));
    }

    @Override
    public void clearFields() {
        amountField.clear();
        descriptionField.clear();
        accountComboBox.getSelectionModel().clearSelection();
    }
}
