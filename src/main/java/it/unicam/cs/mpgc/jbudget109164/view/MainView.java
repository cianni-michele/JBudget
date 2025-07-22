package it.unicam.cs.mpgc.jbudget109164.view;

import it.unicam.cs.mpgc.jbudget109164.controller.budget.MonthlyBudgetController;
import it.unicam.cs.mpgc.jbudget109164.controller.statistic.StatisticsController;
import it.unicam.cs.mpgc.jbudget109164.controller.movement.MovementController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.util.io.FXMLResourceLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainView {
    private final TagController tagController;
    private final MovementController movementController;
    private final StatisticsController statisticsController;
    private final MonthlyBudgetController monthlyBudgetController;

    @FXML
    private BorderPane root;

    @FXML
    private Button movementsButton;

    @FXML
    private Button statisticsButton;

    @FXML
    private Button categoriesButton;

    @FXML
    private Button budgetsButton;

    public MainView(TagController tagController,
                    MovementController movementController,
                    StatisticsController statisticsController,
                    MonthlyBudgetController monthlyBudgetController) {
        this.tagController = tagController;
        this.movementController = movementController;
        this.statisticsController = statisticsController;
        this.monthlyBudgetController = monthlyBudgetController;
    }

    @FXML
    public void initialize() {
        setupButtons();
        loadMovementsView();
    }

    private void setupButtons() {
        movementsButton.setOnAction(event -> handleMovementsAction());
        budgetsButton.setOnAction(event -> handleBudgetsAction());
        statisticsButton.setOnAction(event -> handleStatisticsAction());
        categoriesButton.setOnAction(event -> handleCategoriesAction());
    }


    private void handleMovementsAction() {
        loadMovementsView();
    }

    private void loadMovementsView() {
        FXMLLoader movementsLoader = FXMLResourceLoader.getLoader("Movements");
        movementsLoader.setController(new MovementsView(movementController, tagController));
        try {
            root.setCenter(movementsLoader.<Parent>load());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    private void handleBudgetsAction() {
        loadBudgetsView();
    }

    private void loadBudgetsView() {
        FXMLLoader budgetsLoader = FXMLResourceLoader.getLoader("Budgets");
        budgetsLoader.setController(new BudgetsView(monthlyBudgetController, tagController));
        try {
            root.setCenter(budgetsLoader.<Parent>load());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleStatisticsAction() {
        loadStatisticsView();
    }

    private void loadStatisticsView() {
        FXMLLoader statisticsLoader = FXMLResourceLoader.getLoader("Statistics");
        statisticsLoader.setController(new StatisticsView(statisticsController));
        try {
            root.setCenter(statisticsLoader.<Parent>load());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleCategoriesAction() {
        loadCategoriesView();
    }

    private void loadCategoriesView() {
        FXMLLoader categoriesLoader = FXMLResourceLoader.getLoader("Categories");
        categoriesLoader.setController(new CategoriesView(tagController));
        try {
            root.setCenter(categoriesLoader.<Parent>load());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
