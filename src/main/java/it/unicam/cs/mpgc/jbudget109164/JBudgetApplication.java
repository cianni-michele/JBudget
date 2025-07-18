package it.unicam.cs.mpgc.jbudget109164;

import it.unicam.cs.mpgc.jbudget109164.controller.movement.MovementController;
import it.unicam.cs.mpgc.jbudget109164.controller.tag.TagController;
import it.unicam.cs.mpgc.jbudget109164.util.io.FXMLResourceLoader;
import it.unicam.cs.mpgc.jbudget109164.view.controller.MovementsViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class JBudgetApplication extends Application {

    private TagController tagController;

    private MovementController movementController;


    @Override
    public void init() throws Exception {
        super.init();
        ControllerFactory controllerFactory = new ControllerFactory();
        tagController = controllerFactory.getTagController();
        movementController = controllerFactory.getMovementController();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Main layout
        BorderPane root = new BorderPane();

        // Sidebar (Menu)
        VBox sidebar = createSideBar(root);

        // Layout setup
        root.setLeft(sidebar);

        Scene scene = new Scene(root, 1000, 800);
        stage.setTitle("JBudget");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createSideBar(BorderPane root) {
        VBox sidebar = new VBox(10);
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(200);

        Button dashboardBtn = createSidebarButton("Dashboard");
        dashboardBtn.setOnAction(e -> {
            root.setCenter(FXMLResourceLoader.loadView("Dashboard"));
        });

        Button transactionsBtn = createSidebarButton("Movements");
        transactionsBtn.setOnAction(e -> {
            FXMLLoader movementsLoader = FXMLResourceLoader.getLoader("Movements");
            movementsLoader.setController(new MovementsViewController(movementController, tagController));
            Parent parent;
            try {
                parent = movementsLoader.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            root.setCenter(parent);
        });

        Button dueDatesBtn = createSidebarButton("Due Dates");
        Button statisticsBtn = createSidebarButton("Statistics");
        Button acoountsBtn = createSidebarButton("Acoounts");

        sidebar.getChildren().addAll(dashboardBtn, transactionsBtn, dueDatesBtn, statisticsBtn, acoountsBtn);
        return sidebar;
    }

    private Button createSidebarButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 10;");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
