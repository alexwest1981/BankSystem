package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BankApp extends Application {

    private CustomerService customerService = new CustomerService();
    private AccountService accountService = new AccountService();

    private TextField txtFirstName, txtLastName, txtPnr, txtAddress, txtEmail, txtPhone, txtInterestRate;
    private ComboBox<String> cbOffice;
    private Label lblAccountNumber, lblClearingNumber, lblMessage;

    private ListView<String> customerListView = new ListView<>();
    private ObservableList<String> observableCustomerList = FXCollections.observableArrayList();

    private TableView<Account> accountTable = new TableView<>();
    private TableView<Transaction> transactionTable = new TableView<>();

    private Label lblBankBalance = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bankapplikation med Databas");

        TabPane tabPane = new TabPane();

        Tab tabRegisterAccount = new Tab("Registrera Kund & Skapa Konto");
        tabRegisterAccount.setContent(createRegisterAndCreateAccountPane());
        tabRegisterAccount.setClosable(false);

        Tab tabShowCustomers = new Tab("Visa Kunder");
        tabShowCustomers.setContent(createShowCustomersPane());
        tabShowCustomers.setClosable(false);

        tabPane.getTabs().addAll(tabRegisterAccount, tabShowCustomers);

        Scene scene = new Scene(tabPane, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        loadAndRefreshCustomers();
        updateBankBalance();
    }

    private void loadAndRefreshCustomers() {
        customerService.reloadCustomers();
        observableCustomerList.clear();
        for (Customer c : customerService.getCustomerList()) {
            observableCustomerList.add(c.getCustomerId() + " - " + c.getName());
        }
        customerListView.setItems(observableCustomerList);
    }

    private GridPane createRegisterAndCreateAccountPane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        txtFirstName = new TextField();
        txtLastName = new TextField();
        txtPnr = new TextField();
        txtAddress = new TextField();
        txtEmail = new TextField();
        txtPhone = new TextField();
        txtInterestRate = new TextField();
        cbOffice = new ComboBox<>();

        cbOffice.getItems().setAll(accountService.getAllCitiesSorted());
        cbOffice.getSelectionModel().selectFirst();

        lblAccountNumber = new Label();
        lblClearingNumber = new Label();
        lblMessage = new Label();

        cbOffice.valueProperty().addListener((obs, oldVal, newVal) -> previewAccountData());
        txtPnr.textProperty().addListener((obs, oldVal, newVal) -> previewAccountData());

        grid.add(new Label("Förnamn:"), 0, 0); grid.add(txtFirstName, 1, 0);
        grid.add(new Label("Efternamn:"), 0, 1); grid.add(txtLastName, 1, 1);
        grid.add(new Label("Personnummer:"), 0, 2); grid.add(txtPnr, 1, 2);
        grid.add(new Label("Adress:"), 0, 3); grid.add(txtAddress, 1, 3);
        grid.add(new Label("Email:"), 0, 4); grid.add(txtEmail, 1, 4);
        grid.add(new Label("Telefon:"), 0, 5); grid.add(txtPhone, 1, 5);
        grid.add(new Label("Räntesats (%):"), 0, 6); grid.add(txtInterestRate, 1, 6);
        grid.add(new Label("Kontorsort:"), 0, 7); grid.add(cbOffice, 1, 7);
        grid.add(new Label("Genererat kontonummer:"), 0, 8); grid.add(lblAccountNumber, 1, 8);
        grid.add(new Label("Genererat clearingnummer:"), 0, 9); grid.add(lblClearingNumber, 1, 9);

        Button btnRegisterAndCreate = new Button("Registrera kund & skapa konto");
        btnRegisterAndCreate.setOnAction(e -> handleRegisterCustomer());
        grid.add(btnRegisterAndCreate, 1, 10);
        grid.add(lblMessage, 1, 11);

        return grid;
    }

    private void handleRegisterCustomer() {
        if (!validateCustomerData(
                txtFirstName.getText(), txtLastName.getText(), txtPnr.getText(),
                txtAddress.getText(), txtEmail.getText(), txtPhone.getText(), txtInterestRate.getText())) {
            lblMessage.setText("Fyll i alla fält korrekt.");
            return;
        }

        String pnr = txtPnr.getText();
        if (customerService.findCustomerByPersonalNumber(pnr) != null) {
            lblMessage.setText("Kund finns redan.");
            return;
        }

        double interestRate = Double.parseDouble(txtInterestRate.getText()) / 100.0;
        boolean registered = customerService.registerCustomer(
                pnr, txtFirstName.getText() + " " + txtLastName.getText(),
                pnr, txtAddress.getText(), txtEmail.getText(), txtPhone.getText());

        if (!registered) {
            lblMessage.setText("Fel vid registrering.");
            return;
        }

        loadAndRefreshCustomers();

        Customer customer = customerService.findCustomerByPersonalNumber(pnr);
        String accountNumber = accountService.generateAccountNumber();
        String clearingNumber = accountService.generateClearingNumber(cbOffice.getValue());

        boolean accountCreated = accountService.createAccountForCustomer(customer, accountNumber, interestRate, clearingNumber);
        if (accountCreated) {
            lblAccountNumber.setText(accountService.formatAccountNumber(accountNumber));
            lblClearingNumber.setText(clearingNumber);
            lblMessage.setText("Kund och konto skapade.");
            clearRegistrationForm();
            updateBankBalance();
        } else {
            lblMessage.setText("Fel vid kontoskapande.");
        }
    }

    private void refreshCustomerList() {
        customerListView.setItems(observableCustomerList);
    }

    private void clearRegistrationForm() {
        txtFirstName.clear();
        txtLastName.clear();
        txtPnr.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtInterestRate.clear();
        lblAccountNumber.setText("");
        lblClearingNumber.setText("");
        lblMessage.setText("");
    }

    private void previewAccountData() {
        if (txtPnr.getText() != null && txtPnr.getText().matches("\\d{10}") && cbOffice.getValue() != null) {
            String accountNumber = accountService.generateAccountNumber();
            String clearingNumber = accountService.generateClearingNumber(cbOffice.getValue());
            lblAccountNumber.setText(accountService.formatAccountNumber(accountNumber));
            lblClearingNumber.setText(clearingNumber);
        } else {
            lblAccountNumber.setText("");
            lblClearingNumber.setText("");
        }
    }

    private VBox createShowCustomersPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Button btnRefresh = new Button("Uppdatera lista");
        btnRefresh.setOnAction(e -> loadAndRefreshCustomers());

        customerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String custId = newVal.split(" - ")[0];
                Customer customer = customerService.findCustomerByCustomerId(custId);
                if (customer != null) {
                    accountTable.getItems().setAll(customer.getAccounts());
                    transactionTable.getItems().clear();
                }
            }
        });

        TableColumn<Account, String> accNumCol = new TableColumn<>("Kontonummer");
        accNumCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        TableColumn<Account, Double> balanceCol = new TableColumn<>("Saldo");
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<Account, Double> interestCol = new TableColumn<>("Ränta");
        interestCol.setCellValueFactory(new PropertyValueFactory<>("interestRate"));

        accountTable.getColumns().setAll(accNumCol, balanceCol, interestCol);
        accountTable.setPlaceholder(new Label("Välj en kund för att se konton"));

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

        vbox.getChildren().addAll(btnRefresh, customerListView, accountTable, transactionTable);
        return vbox;
    }

    private void updateBankBalance() {
        lblBankBalance.setText(String.format("Bankens saldo: %.2f kr", accountService.getBankBalance()));
    }

    private boolean validateCustomerData(String firstName, String lastName, String pnr, String address, String email, String phone, String interestText) {
        if (firstName.isBlank() || lastName.isBlank() || pnr.isBlank() ||
                address.isBlank() || email.isBlank() || phone.isBlank() || interestText.isBlank()) {
            return false;
        }
        if (!pnr.matches("\\d{10}")) return false;
        try {
            double rate = Double.parseDouble(interestText);
            if (rate < 0 || rate > 100) return false;
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }


}
