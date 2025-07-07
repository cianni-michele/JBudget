package it.unicam.cs.mpgc.jbudget109164.view;

import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public abstract class CustomForm<T> extends GridPane {

    public CustomForm(Consumer<T> onSubmit) {
        setupGap();
        setupComponents();
        setupLayout();
        handleEvents(onSubmit);
    }

    protected abstract void setupGap();

    protected abstract void setupComponents();

    protected abstract void setupLayout();

    protected abstract void handleEvents(Consumer<T> onSubmit);

    public abstract void clearFields();
}
