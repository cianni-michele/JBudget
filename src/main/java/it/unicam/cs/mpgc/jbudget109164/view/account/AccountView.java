package it.unicam.cs.mpgc.jbudget109164.view.account;

import it.unicam.cs.mpgc.jbudget109164.controller.AccountController;
import it.unicam.cs.mpgc.jbudget109164.model.account.Account;
import it.unicam.cs.mpgc.jbudget109164.view.CustomView;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class AccountView extends CustomView<Account> {

    private final AccountController accountController;

    public AccountView(AccountController accountController) {
        super(FXCollections.observableArrayList());
        this.accountController = accountController;

        initializeView();
        loadItems();
    }

    @Override
    protected void initializeView() {
        setupFormPanel();
        setupListView();
        configureListCellFactory();
    }

    private void setupFormPanel() {
        GridPane accountForm = new AccountCreationForm(formData -> {
            try {
                accountController.createAccount(
                        formData.accountType(),
                        formData.accountName(),
                        formData.accountDescription(),
                        formData.accountInitialBalance()
                );
                loadItems();
            } catch (NumberFormatException ex) {
                showAlert("Invalid initial balance. Please enter a valid number.");
            } catch (Exception ex) {
                showAlert(ex.getMessage());
            }
        });
        setupRightPanel(accountForm);
    }

    private void configureListCellFactory() {
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                if (empty || account == null) {
                    setText(null);
                } else {
                    setText(account.getName());
                }
            }
        });
    }

    @Override
    protected void loadItems() {
        try {
            itemsList.setAll(accountController.getAllAccounts());
        } catch (Exception e) {
            showAlert("Error during loading accounts: " + e.getMessage());
        }
    }


}
