package it.unicam.cs.mpgc.jbudget109164.view.account;

import it.unicam.cs.mpgc.jbudget109164.model.account.AccountType;
import it.unicam.cs.mpgc.jbudget109164.view.CustomForm;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class AccountCreationForm extends CustomForm<AccountFormData> {

    private ComboBox<AccountType> accountTypeComboBox;
    private TextField nameField;
    private TextField descriptionField;
    private TextField initialBalanceField;
    private Button createButton;

    public AccountCreationForm(Consumer<AccountFormData> onSubmit) {
        super(onSubmit);
    }

    @Override
    protected void setupGap() {
        this.setHgap(10);
        this.setVgap(10);
    }

    @Override
    protected void setupComponents() {
        accountTypeComboBox = new ComboBox<>();
        accountTypeComboBox.getItems().addAll(AccountType.values());

        nameField = new TextField();
        nameField.setPromptText("Account Name");

        descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        initialBalanceField = new TextField();
        initialBalanceField.setPromptText("Initial Balance");

        createButton = new Button("Create Account");
    }

    @Override
    protected void setupLayout() {
        add(new Label("Account Type:"), 0, 0);
        add(accountTypeComboBox, 1, 0);

        add(new Label("Name:"), 0, 1);
        add(nameField, 1, 1);

        add(new Label("Description:"), 0, 2);
        add(descriptionField, 1, 2);

        add(new Label("Initial Balance:"), 0, 3);
        add(initialBalanceField, 1, 3);

        add(createButton, 1, 4);
    }

    @Override
    protected void handleEvents(Consumer<AccountFormData> onSubmit) {
        createButton.setOnAction(e -> {
            AccountType accountType = accountTypeComboBox.getValue();
            String name = nameField.getText();
            String description = descriptionField.getText();
            double initialBalance = Double.parseDouble(initialBalanceField.getText());

            onSubmit.accept(new AccountFormData(accountType, name, description, initialBalance));
            clearFields();
        });
    }

    @Override
    public void clearFields() {
        accountTypeComboBox.setValue(null);
        nameField.clear();
        descriptionField.clear();
        initialBalanceField.clear();
    }
}
