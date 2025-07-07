package it.unicam.cs.mpgc.jbudget109164;

import it.unicam.cs.mpgc.jbudget109164.controller.AccountController;
import it.unicam.cs.mpgc.jbudget109164.controller.TagController;
import it.unicam.cs.mpgc.jbudget109164.controller.TransactionController;
import it.unicam.cs.mpgc.jbudget109164.utils.io.FXMLResourceLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JBudgetApplication extends Application {

    private TagController tagController;

    private AccountController accountController;

    private TransactionController transactionController;


    @Override
    public void init() throws Exception {
        super.init();
        ControllerFactory controllerFactory = new ControllerFactory();
        tagController = controllerFactory.getTagController();
        accountController = controllerFactory.getAccountController();
        transactionController = controllerFactory.getTransactionController();
    }

    @Override
    public void start(Stage stage) throws Exception {
        /*TabPane tabPane = new TabPane();

        Tab accountsTab = new Tab("Accounts");
        AccountView accountsView = new AccountView(accountController);
        accountsTab.setContent(accountsView);
        accountsTab.setClosable(false);

        Tab transactionsTab = new Tab("Transactions");
        TransactionView transactionView = new TransactionView(transactionController, accountController);
        transactionsTab.setContent(transactionView);
        transactionsTab.setClosable(false);

        tabPane.getTabs().addAll(accountsTab, transactionsTab);*/
        //Scene scene = new Scene(tabPane, 900, 600);
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

        Button transactionsBtn = createSidebarButton("Transactions");
        transactionsBtn.setOnAction(e -> {
            root.setCenter(FXMLResourceLoader.loadView("Transactions"));
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
