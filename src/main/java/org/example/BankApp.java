package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

public class BankApp extends Application {

    private Bank bank = new Bank();
    private ListView<String> customerListView = new ListView<>();
    private TableView<Account> accountTable = new TableView<>();
    private TableView<Transaction> transactionTable = new TableView<>();


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

        // Flik för kontohantering (transaktioner och ränta)
        Tab tabTransactionManagement = createTransactionManagementTab();

        tabPane.getTabs().addAll(tabRegisterCustomer, tabShowCustomers, tabCreateAccount, tabTransactionManagement);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // Panel för att skapa kund
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

    // Panel för att skapa konto
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

    // Panel för att visa kunder
    private VBox createShowCustomersPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        Button btnRefresh = new Button("Uppdatera lista");
        btnRefresh.setOnAction(e -> {
            customerListView.getItems().clear();
            for (Customer c : bank.getCustomers()) {
                customerListView.getItems().add(c.getCustomerId() + " - " + c.getName());
            }
        });

        // Set up columns for accountTable
        TableColumn<Account, String> accNumCol = new TableColumn<>("Kontonummer");
        accNumCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        TableColumn<Account, Double> balanceCol = new TableColumn<>("Saldo");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<Account, Double> interestCol = new TableColumn<>("Ränta");
        interestCol.setCellValueFactory(new PropertyValueFactory<>("interestRate"));

        accountTable.getColumns().setAll(accNumCol, balanceCol, interestCol);
        accountTable.setPlaceholder(new Label("Välj en kund för att se konton"));

        // Set up columns for transactionTable
        TableColumn<Transaction, String> dateCol = new TableColumn<>("Datum");
        dateCol.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getDate();
            var formatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });

        TableColumn<Transaction, Transaction.Type> typeCol = new TableColumn<>("Typ");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Belopp");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, String> descCol = new TableColumn<>("Beskrivning");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        transactionTable.getColumns().setAll(dateCol, typeCol, amountCol, descCol);
        transactionTable.setPlaceholder(new Label("Välj ett konto för att se transaktioner"));

        // Labels för kontoinformation
        Label lblSaldo = new Label("Saldo: ");
        Label lblRanta = new Label("Ränta: ");

        VBox accountInfoBox = new VBox(5, lblSaldo, lblRanta);
        accountInfoBox.setPadding(new Insets(10));
        accountInfoBox.setPrefWidth(200);

        // Event: när kund väljs, visa konton
        customerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String custId = newVal.split(" - ")[0];
                Customer customer = bank.findCustomerById(custId);
                if (customer != null) {
                    accountTable.getItems().setAll(customer.getAccounts());
                    transactionTable.getItems().clear();
                    updateAccountInfo(null, lblSaldo, lblRanta);
                }
            }
        });

        // Event: när konto väljs, visa transaktioner och uppdatera kontoinfo
        accountTable.getSelectionModel().selectedItemProperty().addListener((obs, oldAcc, newAcc) -> {
            if (newAcc != null) {
                transactionTable.getItems().setAll(newAcc.getTransactions());
                updateAccountInfo(newAcc, lblSaldo, lblRanta);
            } else {
                transactionTable.getItems().clear();
                updateAccountInfo(null, lblSaldo, lblRanta);
            }
        });

        HBox hbox = new HBox(10);
        VBox customerBox = new VBox(5, btnRefresh, customerListView);
        customerBox.setPrefWidth(150);

        VBox accountBox = new VBox(5, new Label("Konton"), accountTable);
        accountBox.setPrefWidth(250);

        VBox transactionBox = new VBox(5, new Label("Transaktioner"), transactionTable);
        transactionBox.setPrefWidth(350);

        hbox.getChildren().addAll(customerBox, accountBox, accountInfoBox, transactionBox);
        vbox.getChildren().add(hbox);

        return vbox;
    }

    private void updateAccountInfo(Account account, Label saldoLabel, Label rantaLabel) {
        if (account != null) {
            saldoLabel.setText(String.format("Saldo: %.2f kr", account.getBalance()));
            rantaLabel.setText(String.format("Ränta: %.2f %%", account.getInterestRate() * 100));
        } else {
            saldoLabel.setText("Saldo: ");
            rantaLabel.setText("Ränta: ");
        }
    }

    private Tab createTransactionManagementTab() {
        Tab tab = new Tab("Kontohantering");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Sökfält
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Sök kunder eller konton (namn, ID, konto-nr)");

        // Sökknapp
        Button btnSearch = new Button("Sök");

        // Lista som visar sökresultat: kundid + namn + kontonummer
        ListView<String> lvSearchResults = new ListView<>();

        // Tabeller för konton för vald kund och transaktioner för valt konto
        TableView<Account> tvAccounts = new TableView<>();
        TableView<Transaction> tvTransactions = new TableView<>();

        // Fält och knappar för överföring och ränta
        TextField txtTransferAmount = new TextField();
        txtTransferAmount.setPromptText("Belopp att överföra");

        TextField txtTargetAccount = new TextField();
        txtTargetAccount.setPromptText("Mottagarens kontonummer");

        Button btnTransfer = new Button("Utför överföring");
        Label lblTransferStatus = new Label();

        TextField txtNewInterestRate = new TextField();
        txtNewInterestRate.setPromptText("Ny ränta %");

        Button btnAdjustInterest = new Button("Justera ränta");
        Label lblInterestStatus = new Label();

        // Sätt upp kolumner för konton
        TableColumn<Account, String> colAccNum = new TableColumn<>("Kontonummer");
        colAccNum.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        TableColumn<Account, Double> colBalance = new TableColumn<>("Saldo");
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<Account, Double> colInterest = new TableColumn<>("Ränta");
        colInterest.setCellValueFactory(new PropertyValueFactory<>("interestRate"));

        tvAccounts.getColumns().addAll(colAccNum, colBalance, colInterest);

        // Kolumner för transaktioner
        TableColumn<Transaction, String> colDate = new TableColumn<>("Datum");
        colDate.setCellValueFactory(cellData -> {
            var date = cellData.getValue().getDate();
            var formatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });

        TableColumn<Transaction, Transaction.Type> colType = new TableColumn<>("Typ");
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, Double> colAmount = new TableColumn<>("Belopp");
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, String> colDesc = new TableColumn<>("Beskrivning");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        tvTransactions.getColumns().addAll(colDate, colType, colAmount, colDesc);

        // Sökfunktion - realtidssökning
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            lvSearchResults.getItems().clear();
            if (newVal == null || newVal.isBlank()) return;

            String search = newVal.toLowerCase();

            for (Customer c : bank.getCustomers()) {
                boolean matchCustomer = c.getName().toLowerCase().contains(search) || c.getCustomerId().toLowerCase().contains(search);
                for (Account a : c.getAccounts()) {
                    boolean matchAccount = a.getAccountNumber().toLowerCase().contains(search);

                    if (matchCustomer && matchAccount) {
                        lvSearchResults.getItems().add(c.getCustomerId() + " - " + c.getName() + " - " + a.getAccountNumber());
                    }
                }
            }
        });

        // När man klickar på ett sökresultat, visa konto i konto-tabell
        lvSearchResults.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String[] parts = newVal.split(" - ");
                String custId = parts[0];
                Customer selected = bank.findCustomerById(custId);
                if (selected != null) {
                    tvAccounts.getItems().setAll(selected.getAccounts());
                }
            }
        });

        // När konto väljs, visa transaktioner
        tvAccounts.getSelectionModel().selectedItemProperty().addListener((obs, oldAcc, newAcc) -> {
            if (newAcc != null) {
                tvTransactions.getItems().setAll(newAcc.getTransactions());
                lblTransferStatus.setText("");
                lblInterestStatus.setText("");
            } else {
                tvTransactions.getItems().clear();
            }
        });

        // Överföringsknapp - valideringar och genomför
        btnTransfer.setOnAction(e -> {
            String amountStr = txtTransferAmount.getText().trim();
            String targetAccNum = txtTargetAccount.getText().trim();
            Account sourceAcc = tvAccounts.getSelectionModel().getSelectedItem();

            if (sourceAcc == null) {
                lblTransferStatus.setText("Välj först ett konto att föra över från.");
                return;
            }
            if (amountStr.isEmpty() || targetAccNum.isEmpty()) {
                lblTransferStatus.setText("Fyll i belopp och mottagarens kontonummer.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException ex) {
                lblTransferStatus.setText("Felaktigt format på beloppet.");
                return;
            }

            Account targetAcc = null;
            outer:
            for (Customer c : bank.getCustomers()) {
                for (Account a : c.getAccounts()) {
                    if (a.getAccountNumber().equals(targetAccNum)) {
                        targetAcc = a;
                        break outer;
                    }
                }
            }
            if (targetAcc == null) {
                lblTransferStatus.setText("Mottagarkonto finns inte.");
                return;
            }
            if (sourceAcc.equals(targetAcc)) {
                lblTransferStatus.setText("Kan inte överföra till samma konto.");
                return;
            }

            try {
                sourceAcc.transferTo(targetAcc, amount);
                bank.saveToFile();
                lblTransferStatus.setText("Överföring genomförd.");
                tvTransactions.getItems().setAll(sourceAcc.getTransactions());
            } catch (Exception ex){
                lblTransferStatus.setText("Överföring misslyckades: " + ex.getMessage());
            }
        });

        // Justera ränta knapp och fält
        btnAdjustInterest.setOnAction(e -> {
            Account selected = tvAccounts.getSelectionModel().getSelectedItem();
            if (selected == null) {
                lblInterestStatus.setText("Välj ett konto att justera räntan för.");
                return;
            }
            String newRateStr = txtNewInterestRate.getText().trim();
            if (newRateStr.isEmpty()) {
                lblInterestStatus.setText("Ange ny ränta.");
                return;
            }
            try {
                double newRate = Double.parseDouble(newRateStr) / 100.0;
                selected.setInterestRate(newRate);
                bank.saveToFile();
                lblInterestStatus.setText("Ränta uppdaterad.");
                tvAccounts.refresh(); // uppdatera tabellen för att visa ny ränta
            } catch (NumberFormatException ex) {
                lblInterestStatus.setText("Felaktigt värde för ränta.");
            }
        });

        // Layout
        HBox searchBox = new HBox(10, txtSearch, btnSearch);
        btnSearch.setOnAction(e -> {
            // Om du vill implementera manuell sökning via knapp istället för live-lisning
            txtSearch.fireEvent(new javafx.event.ActionEvent());
        });

        VBox leftPane = new VBox(10, new Label("Sök Kunder/Konton"), txtSearch, lvSearchResults);
        leftPane.setPrefWidth(250);

        VBox middlePane = new VBox(10, new Label("Konton"), tvAccounts);
        middlePane.setPrefWidth(300);

        VBox rightPane = new VBox(10, new Label("Transaktioner"), tvTransactions);
        rightPane.setPrefWidth(350);

        VBox transferPane = new VBox(10, new Label("Överföring"), txtTransferAmount, txtTargetAccount, btnTransfer, lblTransferStatus);
        VBox interestPane = new VBox(10, new Label("Justera Ränta"), txtNewInterestRate, btnAdjustInterest, lblInterestStatus);

        HBox bottomPane = new HBox(20, transferPane, interestPane);

        root.getChildren().addAll(searchBox, new HBox(15, leftPane, middlePane, rightPane), bottomPane);

        tab.setContent(root);
        tab.setClosable(false);
        return tab;

    }
}
