package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BankApp extends Application {

    private Bank bank = new Bank();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        bank.loadFromFile();

        primaryStage.setTitle("Bankapplikation");

        TabPane tabPane = new TabPane();

        // Flik för Kundregistrering
        Tab tabRegisterCustomer = new Tab("Registrera kund");
        GridPane gridRegister = createRegisterCustomerPane();
        tabRegisterCustomer.setContent(gridRegister);
        tabRegisterCustomer.setClosable(false);

        // Flik för att visa kunder
        Tab tabShowCustomers = new Tab("Visa kunder");
        VBox showCustomerPane = createShowCustomersPane();
        tabShowCustomers.setContent(showCustomerPane);
        tabShowCustomers.setClosable(false);

        // Flik för att skapa konto
        Tab tabCreateAccount = new Tab("Skapa konto");
        GridPane gridCreateAccount = createCreateAccountPane();
        tabCreateAccount.setContent(gridCreateAccount);
        tabCreateAccount.setClosable(false);

        tabPane.getTabs().addAll(tabRegisterCustomer, tabShowCustomers, tabCreateAccount);

        Scene scene = new Scene(tabPane, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createRegisterCustomerPane() {
        GridPane gridRegister = new GridPane();
        gridRegister.setPadding(new Insets(15));
        gridRegister.setVgap(10);
        gridRegister.setHgap(10);

        Label labelId = new Label("Kund-ID:");
        TextField txtId = new TextField();
        Label labelName = new Label("Namn:");
        TextField txtName = new TextField();
        Button btnRegister = new Button("Registrera");
        Label lblMessage = new Label();

        btnRegister.setOnAction(e -> {
            String custId = txtId.getText().trim();
            String custName = txtName.getText().trim();

            if (custId.isEmpty() || custName.isEmpty()) {
                lblMessage.setText("Fyll i båda fälten.");
                return;
            }
            if (bank.findCustomerById(custId) != null) {
                lblMessage.setText("Kund-id finns redan.");
                return;
            }

            bank.registerCustomer(custId, custName);
            bank.saveToFile();
            lblMessage.setText("Kund registrerad: " + custName);
            txtId.clear();
            txtName.clear();
        });

        gridRegister.add(labelId, 0, 0);
        gridRegister.add(txtId, 1, 0);
        gridRegister.add(labelName, 0, 1);
        gridRegister.add(txtName, 1, 1);
        gridRegister.add(btnRegister, 1, 2);
        gridRegister.add(lblMessage, 1, 3);

        return gridRegister;
    }

    private VBox createShowCustomersPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        ListView<String> customerListView = new ListView<>();
        Button btnRefresh = new Button("Uppdatera lista");

        btnRefresh.setOnAction(e -> {
            customerListView.getItems().clear();
            for (Customer c : bank.getCustomers()) {
                customerListView.getItems().add(c.getCustomerId() + " - " + c.getName());
            }
        });

        vbox.getChildren().addAll(btnRefresh, customerListView);
        return vbox;
    }

    private GridPane createCreateAccountPane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        Label labelCustomerId = new Label("Kund-ID:");
        TextField txtCustomerId = new TextField();
        Label labelAccountNumber = new Label("Kontonummer:");
        TextField txtAccountNumber = new TextField();
        Label labelInterestRate = new Label("Räntesats:");
        TextField txtInterestRate = new TextField();

        Button btnCreate = new Button("Skapa konto");
        Label lblMessage = new Label();

        btnCreate.setOnAction(e -> {
            String customerId = txtCustomerId.getText().trim();
            String accNum = txtAccountNumber.getText().trim();
            String interestStr = txtInterestRate.getText().trim();

            if (customerId.isEmpty() || accNum.isEmpty() || interestStr.isEmpty()) {
                lblMessage.setText("Alla fält måste fyllas i.");
                return;
            }

            Customer customer = bank.findCustomerById(customerId);
            if (customer == null) {
                lblMessage.setText("Ingen kund med detta ID.");
                return;
            }

            if (bank.accountNumberExists(accNum)) {
                lblMessage.setText("Kontonummer finns redan.");
                return;
            }

            double rate;
            try {
                rate = Double.parseDouble(interestStr);
            } catch (NumberFormatException ex) {
                lblMessage.setText("Felaktigt format på räntesatsen");
                return;
            }

            customer.openAccount(accNum, rate);
            bank.saveToFile();
            lblMessage.setText("Konto skapat för kund " + customer.getName());

            txtCustomerId.clear();
            txtAccountNumber.clear();
            txtInterestRate.clear();
        });

        grid.add(labelCustomerId, 0, 0);
        grid.add(txtCustomerId, 1, 0);
        grid.add(labelAccountNumber, 0, 1);
        grid.add(txtAccountNumber, 1, 1);
        grid.add(labelInterestRate, 0, 2);
        grid.add(txtInterestRate, 1, 2);
        grid.add(btnCreate, 1, 3);
        grid.add(lblMessage, 1, 4);

        return grid;
    }
}
