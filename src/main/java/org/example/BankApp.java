package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Random;

public class BankApp extends Application {

    private Bank bank = Bank.getInstance();
    private ListView<String> customerListView = new ListView<>();
    private TableView<Account> accountTable = new TableView<>();
    private TableView<Transaction> transactionTable = new TableView<>();
    private Label lblAccountNumber = new Label();
    private Label lblClearingNumber = new Label();

    // Instansvariabler för förhandsvisning
    private String previewedAccountNumber = "";
    private String previewedClearingNumber = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        bank.loadFromFile();

        primaryStage.setTitle("Bankapplikation");

        TabPane tabPane = new TabPane();

        Tab tabRegisterAndCreateAccount = new Tab("Registrera Kund & Skapa Konto");
        tabRegisterAndCreateAccount.setContent(createRegisterAndCreateAccountPane());
        tabRegisterAndCreateAccount.setClosable(false);

        Tab tabShowCustomers = new Tab("Visa kunder", createShowCustomersPane());
        tabShowCustomers.setClosable(false);

        Tab tabTransactionManagement = createTransactionManagementTab();
        tabTransactionManagement.setClosable(false);

        tabPane.getTabs().addAll(tabRegisterAndCreateAccount, tabShowCustomers, tabTransactionManagement);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createRegisterAndCreateAccountPane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setVgap(10);
        grid.setHgap(10);

        // Skapa och placera alla kundfält och etiketter
        Label labelFirstName = new Label("Förnamn:");
        TextField txtFirstName = new TextField();

        Label labelLastName = new Label("Efternamn:");
        TextField txtLastName = new TextField();

        Label labelPnr = new Label("Personnummer:");
        TextField txtPnr = new TextField();

        Label labelAddress = new Label("Adress:");
        TextField txtAddress = new TextField();

        Label labelEmail = new Label("Email:");
        TextField txtEmail = new TextField();

        Label labelPhone = new Label("Telefon:");
        TextField txtPhone = new TextField();

        Label labelInterestRate = new Label("Räntesats (%):");
        TextField txtInterestRate = new TextField();
        txtInterestRate.setPromptText("Ex. 1.25 för 1.25%");

        Label labelOffice = new Label("Kontorsort:");
        ComboBox<String> cbOffice = new ComboBox<>();
        cbOffice.getItems().addAll("Stockholm", "Göteborg", "Malmö", "Uppsala", "Västerås",
                "Örebro", "Linköping", "Helsingborg", "Jönköping", "Norrköping");
        cbOffice.getSelectionModel().selectFirst();
        cbOffice.valueProperty().addListener((obs, oldVal, newVal) -> previewAccountData(txtPnr.getText(), newVal));
        txtPnr.textProperty().addListener((obs, oldVal, newVal) -> previewAccountData(newVal, cbOffice.getValue()));

        Label labelGeneratedAccountNumber = new Label("Genererat kontonummer:");

        Label labelGeneratedClearingNumber = new Label("Genererat clearingnummer:");

        Button btnRegisterAndCreate = new Button("Registrera kund & skapa konto");
        Label lblMessage = new Label();

        btnRegisterAndCreate.setOnAction(e -> {
            // Valideringar
            if (txtFirstName.getText().isBlank() || txtLastName.getText().isBlank() ||
                    txtPnr.getText().isBlank() || txtAddress.getText().isBlank() ||
                    txtEmail.getText().isBlank() || txtPhone.getText().isBlank() ||
                    txtInterestRate.getText().isBlank()) {
                lblMessage.setText("Fyll i alla kunduppgifter och räntesats.");
                return;
            }
            if (!txtPnr.getText().matches("\\d{10}")) {
                lblMessage.setText("Personnummer måste vara 10 siffror.");
                return;
            }

            double interestRate;
            try {
                interestRate = Double.parseDouble(txtInterestRate.getText()) / 100.0;
                if (interestRate < 0 || interestRate > 1) {
                    lblMessage.setText("Räntesats måste vara mellan 0 och 100.");
                    return;
                }
            } catch (NumberFormatException ex) {
                lblMessage.setText("Felaktigt format på räntesatsen.");
                return;
            }

            String customerId = txtPnr.getText();
            if (bank.findCustomerById(customerId) != null) {
                lblMessage.setText("Kund med detta personnummer finns redan.");
                return;
            }

            // Generera kontonummer och clearingnummer innan kundskapande
            String accountNumber = previewedAccountNumber;
            String clearingNumber = previewedClearingNumber;

            if (accountNumber.isEmpty() || clearingNumber.isEmpty()) {
                lblMessage.setText("Fyll i giltigt personnummer och kontorsort för förhandsvisning/kontoskaping.");
                return;
            }

            bank.registerCustomer(
                    customerId,
                    txtFirstName.getText() + " " + txtLastName.getText(),
                    txtPnr.getText(),
                    txtAddress.getText(),
                    txtEmail.getText(),
                    txtPhone.getText()
            );
            Customer customer = bank.findCustomerById(customerId);

            if (customer != null) {
                customer.openAccount(accountNumber, interestRate, clearingNumber);
                lblAccountNumber.setText(formatAccountNumber(accountNumber));
                lblClearingNumber.setText(clearingNumber);
                lblMessage.setText("Kund och konto skapade.");

                // Rensa endast textfälten för ny registrering
                txtFirstName.clear();
                txtLastName.clear();
                txtPnr.clear();
                txtAddress.clear();
                txtEmail.clear();
                txtPhone.clear();
                txtInterestRate.clear();

                // OBS: Rensa INTE lblAccountNumber eller lblClearingNumber här!
                bank.saveToFile();
                // Generera ny förhandsvisning efter skapande om användaren vill registrera fler
                previewAccountData("", cbOffice.getValue());
            } else {
                lblMessage.setText("Fel vid skapande av kund.");
            }
        });

        // Placera alla element i grid
        grid.add(labelFirstName, 0, 0);
        grid.add(txtFirstName, 1, 0);
        grid.add(labelLastName, 0, 1);
        grid.add(txtLastName, 1, 1);
        grid.add(labelPnr, 0, 2);
        grid.add(txtPnr, 1, 2);
        grid.add(labelAddress, 0, 3);
        grid.add(txtAddress, 1, 3);
        grid.add(labelEmail, 0, 4);
        grid.add(txtEmail, 1, 4);
        grid.add(labelPhone, 0, 5);
        grid.add(txtPhone, 1, 5);
        grid.add(labelInterestRate, 0, 6);
        grid.add(txtInterestRate, 1, 6);
        grid.add(labelOffice, 0, 7);
        grid.add(cbOffice, 1, 7);
        grid.add(labelGeneratedAccountNumber, 0, 8);
        grid.add(lblAccountNumber, 1, 8);
        grid.add(labelGeneratedClearingNumber, 0, 9);
        grid.add(lblClearingNumber, 1, 9);
        grid.add(btnRegisterAndCreate, 1, 10);
        grid.add(lblMessage, 1, 11);

        return grid;
    }

    private String generateAccountNumber() {
        Random rnd = new Random();
        int part1 = rnd.nextInt(9000) + 1000;
        int part2 = rnd.nextInt(9000) + 1000;
        int part3 = rnd.nextInt(9000) + 1000;
        return String.format("%04d%04d%04d", part1, part2, part3);
    }

    private String formatAccountNumber(String number) {
        return number.replaceAll("(\\d{4})(\\d{4})(\\d{4})", "$1 $2 $3");
    }

    private String generateClearingNumber(String city) {
        switch (city.toLowerCase()) {
            case "stockholm": return "1111-1";
            case "göteborg": return "2222-2";
            case "malmö": return "3333-3";
            case "uppsala": return "4444-4";
            case "västerås": return "5555-5";
            case "örebro": return "6666-6";
            case "linköping": return "7777-7";
            case "helsingborg": return "8888-8";
            case "jönköping": return "9999-9";
            case "norrköping": return "0000-0";
            default: return "0000-0";
        }
    }

    private void previewAccountData(String pnr, String office) {
        if (pnr != null && pnr.matches("\\d{10}") && office != null) {
            previewedAccountNumber = generateAccountNumber();
            previewedClearingNumber = generateClearingNumber(office);

            lblAccountNumber.setText(formatAccountNumber(previewedAccountNumber));
            lblClearingNumber.setText(previewedClearingNumber);
        } else {
            previewedAccountNumber = "";
            previewedClearingNumber = "";
            lblAccountNumber.setText("");
            lblClearingNumber.setText("");
        }
    }


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

        Label lblSaldo = new Label("Saldo: ");
        Label lblRanta = new Label("Ränta: ");

        VBox accountInfoBox = new VBox(5, lblSaldo, lblRanta);
        accountInfoBox.setPadding(new Insets(10));
        accountInfoBox.setPrefWidth(200);

        Button btnDeleteCustomer = new Button("Ta bort vald kund");
        btnDeleteCustomer.setOnAction(e -> {
            String selected = customerListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String customerId = selected.split(" - ")[0];
                if (bank.removeCustomerById(customerId)) {
                    customerListView.getItems().remove(selected);
                    accountTable.getItems().clear();
                    transactionTable.getItems().clear();
                }
            }
        });

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
        VBox customerBox = new VBox(5, btnRefresh, customerListView, btnDeleteCustomer);
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

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Sök kunder eller konton (namn, ID, konto-nr)");

        Button btnSearch = new Button("Sök");

        ListView<String> lvSearchResults = new ListView<>();

        TableView<Account> tvAccounts = new TableView<>();
        TableView<Transaction> tvTransactions = new TableView<>();

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

        TableColumn<Account, String> colAccNum = new TableColumn<>("Kontonummer");
        colAccNum.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));

        TableColumn<Account, Double> colBalance = new TableColumn<>("Saldo");
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<Account, Double> colInterest = new TableColumn<>("Ränta");
        colInterest.setCellValueFactory(new PropertyValueFactory<>("interestRate"));

        tvAccounts.getColumns().addAll(colAccNum, colBalance, colInterest);

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

        tvTransactions.getColumns().setAll(colDate, colType, colAmount, colDesc);
        tvTransactions.setPlaceholder(new Label("Välj ett konto för att se transaktioner"));

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

        tvAccounts.getSelectionModel().selectedItemProperty().addListener((obs, oldAcc, newAcc) -> {
            if (newAcc != null) {
                tvTransactions.getItems().setAll(newAcc.getTransactions());
                lblTransferStatus.setText("");
                lblInterestStatus.setText("");
            } else {
                tvTransactions.getItems().clear();
            }
        });

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
                tvAccounts.refresh();
            } catch (NumberFormatException ex) {
                lblInterestStatus.setText("Felaktigt värde för ränta.");
            }
        });

        HBox searchBox = new HBox(10, txtSearch, btnSearch);
        btnSearch.setOnAction(e -> txtSearch.fireEvent(new javafx.event.ActionEvent()));

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
