package it.unicam.cs.mpgc.jbudget109164.view.transaction;

import it.unicam.cs.mpgc.jbudget109164.view.CustomForm;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.function.Consumer;

public class TransactionCreationForm extends CustomForm<TransactionFormData> {

    private DatePicker dateField;
    private TextField descriptionField;
    private Button createButton;

    public TransactionCreationForm(Consumer<TransactionFormData> onSubmit) {
        super(onSubmit);
    }

    @Override
    protected void setupGap() {
        setHgap(10);
        setVgap(10);
    }

    @Override
    protected void setupComponents() {
        dateField = new DatePicker(LocalDate.now());
        dateField.setPromptText("Select Date");
        dateField.setEditable(false);

        descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        createButton = new Button("Create Transaction");
    }

    @Override
    protected void setupLayout() {
        add(new Label("Date:"), 0, 0);
        add(dateField, 1, 0);

        add(new Label("Description:"), 0, 1);
        add(descriptionField, 1, 1);

        add(createButton, 1, 2);
    }

    @Override
    protected void handleEvents(Consumer<TransactionFormData> onSubmit) {
        createButton.setOnAction(e -> {
            String description = descriptionField.getText();
            LocalDate date = dateField.getValue();

            onSubmit.accept(new TransactionFormData(description, date));
            clearFields();
        });
    }

    @Override
    public void clearFields() {
        dateField.setValue(LocalDate.now());
        descriptionField.clear();
    }
}
