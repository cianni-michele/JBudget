package it.unicam.cs.mpgc.jbudget109164.view;

import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.model.tag.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;

public class CategoriesView {

    private final TagController tagController;

    private TreeItem<Tag> rootItem;

    @FXML
    private TreeView<Tag> categoriesTreeView;

    @FXML
    private Button addCategoryButton;

    @FXML
    private Button addSubcategoryButton;

    @FXML
    private Button editCategoryButton;

    @FXML
    private Button deleteCategoryButton;

    @FXML
    private VBox categoryDetailsBox;

    @FXML
    private Label categoryNameLabel;

    public CategoriesView(TagController tagController) {
        this.tagController = tagController;
    }

    @FXML
    public void initialize() {
        setupTreeView();
        setupEventHandlers();
        loadCategories();
    }

    private void setupTreeView() {
        rootItem = new TreeItem<>();
        rootItem.setExpanded(true);
        categoriesTreeView.setRoot(rootItem);
        categoriesTreeView.setShowRoot(false);

        categoriesTreeView.setCellFactory(new Callback<TreeView<Tag>, TreeCell<Tag>>() {
            @Override
            public TreeCell<Tag> call(TreeView<Tag> param) {
                return new TreeCell<Tag>() {
                    @Override
                    protected void updateItem(Tag item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
    }

    private void setupEventHandlers() {
        categoriesTreeView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null && newSelection.getValue() != null) {
                        showCategoryDetails(newSelection.getValue());
                        enableActionButtons(true);
                    } else {
                        clearCategoryDetails();
                        enableActionButtons(false);
                    }
                }
        );

        addCategoryButton.setOnAction(e -> addCategory());

        addSubcategoryButton.setOnAction(e -> addSubcategory());

        editCategoryButton.setOnAction(e -> editCategory());

        deleteCategoryButton.setOnAction(e -> deleteCategory());
    }

    private void showCategoryDetails(Tag tag) {
        categoryNameLabel.setText("Name: " + tag.getName());
        categoryDetailsBox.setVisible(true);
    }

    private void clearCategoryDetails() {
        categoryNameLabel.setText("");
        categoryDetailsBox.setVisible(false);
    }

    private void enableActionButtons(boolean enable) {
        addSubcategoryButton.setDisable(!enable);
        editCategoryButton.setDisable(!enable);
        deleteCategoryButton.setDisable(!enable);
    }

    @FXML
    private void addCategory() {
        String categoryName = showInputDialog("New Category", "Enter category name:");
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            try {
                tagController.createTag(categoryName.trim());
                loadCategories();
                clearFields();
            } catch (Exception e) {
                showError("Error adding category: " + e.getMessage());
            }
        }
    }

    @FXML
    private void addSubcategory() {
        TreeItem<Tag> selectedItem = categoriesTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            showWarning("Seleziona una categoria genitore");
            return;
        }

        String subcategoryName = showInputDialog("New Subcategory", "Enter subcategory name:");
        if (subcategoryName != null && !subcategoryName.trim().isEmpty()) {
            try {
                Tag parentTag = selectedItem.getValue();
                tagController.addChildTag(parentTag.getId(), subcategoryName.trim());
                loadCategories();
                clearFields();
            } catch (Exception e) {
                showError("Error adding subcategory: " + e.getMessage());
            }
        }
    }

    @FXML
    private void editCategory() {
        TreeItem<Tag> selectedItem = categoriesTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            return;
        }

        Tag selectedTag = selectedItem.getValue();
        String newName = showInputDialog("Edit Category", "New name:", selectedTag.getName());
        if (newName != null && !newName.trim().isEmpty() && !newName.equals(selectedTag.getName())) {
            try {
                tagController.updateTag(selectedTag.getId(), newName.trim());
                loadCategories();
                clearFields();
            } catch (Exception e) {
                showError("Error updating category: " + e.getMessage());
            }
        }
    }

    @FXML
    private void deleteCategory() {
        TreeItem<Tag> selectedItem = categoriesTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem == null || selectedItem.getValue() == null) {
            return;
        }

        Tag selectedTag = selectedItem.getValue();
        boolean confirmed = showConfirmation(
                "Confirm Deletion Category",
                "Are you sure you want to delete the category '" + selectedTag.getName() + "'? " +
                "This action will also delete all its subcategories and associated budgets."
        );

        if (confirmed) {
            try {
                tagController.deleteTag(selectedTag.getId());
                loadCategories();
                clearFields();
            } catch (Exception e) {
                showError("Error deleting category: " + e.getMessage());
            }
        }
    }

    private void clearFields() {
        categoriesTreeView.getSelectionModel().clearSelection();
    }

    private void loadCategories() {
        try {
            List<Tag> allTags = tagController.getAllTags();
            rootItem.getChildren().clear();

            allTags.stream()
                    .filter(tag -> tag.getParents().isEmpty())
                    .forEach(tag -> {
                        TreeItem<Tag> item = createTreeItem(tag, allTags);
                        rootItem.getChildren().add(item);
                    });

        } catch (Exception e) {
            showError("Errore nel caricamento delle categorie: " + e.getMessage());
        }
    }

    private TreeItem<Tag> createTreeItem(Tag tag, List<Tag> allTags) {
        TreeItem<Tag> item = new TreeItem<>(tag);
        item.setExpanded(true);

        tag.getChildren().forEach(child -> {
            TreeItem<Tag> childItem = createTreeItem(child, allTags);
            item.getChildren().add(childItem);
        });

        return item;
    }

    private String showInputDialog(String title, String message) {
        return showInputDialog(title, message, "");
    }

    private String showInputDialog(String title, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
