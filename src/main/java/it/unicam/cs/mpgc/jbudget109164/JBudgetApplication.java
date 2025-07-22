package it.unicam.cs.mpgc.jbudget109164;

import it.unicam.cs.mpgc.jbudget109164.controller.ControllerFactory;
import it.unicam.cs.mpgc.jbudget109164.controller.budget.MonthlyBudgetController;
import it.unicam.cs.mpgc.jbudget109164.controller.statistic.StatisticsController;
import it.unicam.cs.mpgc.jbudget109164.controller.movement.MovementController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.util.io.FXMLResourceLoader;
import it.unicam.cs.mpgc.jbudget109164.view.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JBudgetApplication extends Application {

    private TagController tagController;

    private MovementController movementController;

    private StatisticsController statisticsController;

    private MonthlyBudgetController monthlyBudgetController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        ControllerFactory controllerFactory = new ControllerFactory();
        tagController = controllerFactory.getTagController();
        movementController = controllerFactory.getMovementController();
        statisticsController = controllerFactory.getStatisticsController();
        monthlyBudgetController = controllerFactory.getMonthlyBudgetController();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader mainLoader = FXMLResourceLoader.getLoader("Main");
        mainLoader.setController(new MainView(
                        tagController,
                        movementController,
                        statisticsController,
                        monthlyBudgetController
                )
        );

        Parent root = mainLoader.load();

        Scene scene = new Scene(root, 1000, 800);
        stage.setTitle("JBudget");
        stage.setScene(scene);
        stage.show();
    }
}
