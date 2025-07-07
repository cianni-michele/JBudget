package it.unicam.cs.mpgc.jbudget109164.view.transaction;

import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import it.unicam.cs.mpgc.jbudget109164.view.CustomForm;
import it.unicam.cs.mpgc.jbudget109164.view.tag.TagFormData;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.util.List;
import java.util.function.Consumer;

public class TagSelectionForm extends CustomForm<TagFormData> {

    private ComboBox<Tag> tagComboBox;
    private Button addTagButton;

    public TagSelectionForm(Consumer<TagFormData> onTagSelected) {
        super(onTagSelected);
    }

    @Override
    protected void setupGap() {
        setHgap(10);
        setVgap(10);
    }

    @Override
    protected void setupComponents() {
        tagComboBox = new ComboBox<>();
        addTagButton = new Button("Add Tag");
        addTagButton.setDisable(true);
    }

    @Override
    protected void setupLayout() {
        add(new Label("Select Tag:"), 0, 0);
        add(tagComboBox, 1, 0);
        add(addTagButton, 1, 1);
    }

    @Override
    protected void handleEvents(Consumer<TagFormData> onTagSelected) {
        tagComboBox.setOnAction(event -> {
            Tag selectedTag = tagComboBox.getSelectionModel().getSelectedItem();
            if (selectedTag != null) {
                onTagSelected.accept(new TagFormData(selectedTag.getId(), selectedTag.getName()));
            }
        });
    }

    public void updateTags(List<Tag> tags) {
        tagComboBox.setItems(FXCollections.observableArrayList(tags));

        tagComboBox.setCellFactory(lv -> new ListCell<>(){
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

    public void setAddTagButtonDisabled(boolean disabled) {
        addTagButton.setDisable(disabled);
    }

    @Override
    public void clearFields() {
        tagComboBox.getSelectionModel().clearSelection();
    }
}
