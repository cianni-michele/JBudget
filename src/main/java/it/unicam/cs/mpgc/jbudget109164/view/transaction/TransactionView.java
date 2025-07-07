package it.unicam.cs.mpgc.jbudget109164.view.transaction;

import it.unicam.cs.mpgc.jbudget109164.controller.AccountController;
import it.unicam.cs.mpgc.jbudget109164.controller.TransactionController;
import it.unicam.cs.mpgc.jbudget109164.model.Movement;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.model.transaction.Transaction;
import it.unicam.cs.mpgc.jbudget109164.view.CustomView;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.UUID;

public class TransactionView extends CustomView<Transaction> {

    private final TransactionController transactionController;
    private final AccountController accountController;

    private final ListView<Movement> movementsListView = new ListView<>();
    private final ListView<Tag> tagsListView = new ListView<>();

    private TransactionCreationForm transactionCreationForm;
    private MovementCreationForm movementCreationForm;
    private TagSelectionForm tagSelectionForm;

    public TransactionView(TransactionController transactionController, AccountController accountController) {
        super(FXCollections.observableArrayList());
        this.transactionController = transactionController;
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
        transactionCreationForm = createTransactionCreationForm();
        movementCreationForm = createMovementCreationForm();
        tagSelectionForm = createTagSelectionForm();

        Button deleteButton = createDeleteTransactionButton();

        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(20));
        rightPanel.getChildren().addAll(
                transactionCreationForm,
                new Label("Add Movement:"),
                movementCreationForm,
                new Label("Movements:"),
                movementsListView,
                new Label("Add Tag:"),
                tagSelectionForm,
                new Label("Tags:"),
                tagsListView,
                deleteButton
        );

        setRight(rightPanel);
    }

    private TransactionCreationForm createTransactionCreationForm() {
        return new TransactionCreationForm(formData -> {
            try {
                transactionController.createTransaction(
                        formData.transactionDescription(),
                        formData.transactionDate()
                );

                loadItems();
            } catch (Exception ex) {
                showAlert(ex.getMessage());
            }
        });
    }

    private MovementCreationForm createMovementCreationForm() {
        return new MovementCreationForm(formData -> {
            Transaction selectedTransaction = listView.getSelectionModel().getSelectedItem();
            if (selectedTransaction != null) {
                try {
                    transactionController.addMovementToTransaction(
                            selectedTransaction.getId(),
                            formData.movementAmount(),
                            formData.movementDescription(),
                            formData.selectedAccount().getId()
                    );
                    loadMovementsForTransaction(selectedTransaction.getId());
                } catch (Exception ex) {
                    showAlert(ex.getMessage());
                }
            }
        });
    }

    private TagSelectionForm createTagSelectionForm() {
        return new TagSelectionForm(formData -> {
            Transaction selectedTransaction = listView.getSelectionModel().getSelectedItem();
            if (selectedTransaction != null) {
                try {
                    transactionController.addTagToTransaction(
                            selectedTransaction.getId(),
                            formData.tagId()
                    );
                    loadTagsForTransaction(selectedTransaction.getId());
                } catch (Exception ex) {
                    showAlert(ex.getMessage());
                }
            }
        });
    }

    private Button createDeleteTransactionButton() {
        Button deleteButton = new Button("Delete Transaction");
        deleteButton.setOnAction(e -> {
            Transaction selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    transactionController.deleteTransaction(selected.getId());
                    loadItems();
                } catch (Exception ex) {
                    showAlert(ex.getMessage());
                }
            }
        });
        return deleteButton;
    }

    private void configureListCellFactory() {
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Transaction transaction, boolean empty) {
                super.updateItem(transaction, empty);
                if (empty || transaction == null) {
                    setText(null);
                } else {
                    setText(String.format("%s - %s: %.2f",
                            transaction.getDate(),
                            transaction.getDescription(),
                            transaction.getBalance())
                    );
                }
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    boolean hasSelection = newVal != null;
                    movementCreationForm.setAddButtonDisabled(!hasSelection);
                    if (hasSelection) {
                        loadMovementsForTransaction(newVal.getId());
                    } else {
                        movementsListView.getItems().clear();
                    }
                    movementCreationForm.updateAccounts(accountController.getAllAccounts());
                }
        );

        movementsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Movement movement, boolean empty) {
                super.updateItem(movement, empty);
                if (empty || movement == null) {
                    setText(null);
                } else {
                    setText(String.format("%s: %.2f - %s",
                            movement.account().getName(),
                            movement.amount(),
                            movement.description())
                    );
                }
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    boolean isTransactionSelected = newVal != null;

                    if (movementCreationForm != null) {
                        movementCreationForm.setAddButtonDisabled(!isTransactionSelected);
                    }

                    if (tagSelectionForm != null) {
                        tagSelectionForm.setAddTagButtonDisabled(!isTransactionSelected);
                    }

                    if (isTransactionSelected) {
                        loadMovementsForTransaction(newVal.getId());
                        loadTagsForTransaction(newVal.getId());
                    } else {
                        movementsListView.getItems().clear();
                        tagsListView.getItems().clear();
                    }
                }
        );

        tagsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Tag tag, boolean empty) {
                super.updateItem(tag, empty);
                setText(empty || tag == null ? null : tag.getName());
            }
        });
    }

    private void loadMovementsForTransaction(UUID transactionId) {
        try {
            movementsListView.getItems().setAll(transactionController.getAllMovementsOfTransaction(transactionId));
        } catch (Exception e) {
            showAlert("Error during loading movements: " + e.getMessage());
        }
    }

    private void loadTagsForTransaction(UUID transactionId) {
        try {
            tagsListView.getItems().setAll(transactionController.getAllTagsOfTransaction(transactionId));
        } catch (Exception e) {
            showAlert("Error during loading tags: " + e.getMessage());
        }
    }

    @Override
    protected void loadItems() {
        try {
            itemsList.setAll(transactionController.getAllTransactions());
        } catch (Exception e) {
            showAlert("Error during loading transactions: " + e.getMessage());
        }
    }
}
