package org.example;

import java.util.List;

public class Bank {
    // Volatile för thread safety vid double-checked locking
    private static volatile Bank instance = null;

    private CustomerService customerService;
    private AccountService accountService;
    private BankBalanceService bankBalanceService;

    private List<Customer> customers;
    private double bankBalance;

    // Privat konstruktor för att förhindra instansiering utifrån
    private Bank() {
        customerService = new CustomerService();
        accountService = new AccountService();
        bankBalanceService = new BankBalanceService();

        customers = customerService.getAllCustomers();

        bankBalance = bankBalanceService.getBankBalance();
        if (bankBalance == 0) {
            bankBalance = 1_000_000.0;
            bankBalanceService.initializeBankBalance(bankBalance);
        }
    }

    // Thread-safe getter för singletoninstans
    public static Bank getInstance() {
        if (instance == null) {
            synchronized (Bank.class) {
                if (instance == null) {
                    instance = new Bank();
                }
            }
        }
        return instance;
    }

    // Accessors för servicelagren
    public CustomerService getCustomerService() {
        return customerService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public BankBalanceService getBankBalanceService() {
        return bankBalanceService;
    }

    // Metoder för kundhantering
    public List<Customer> getCustomers() {
        return customers;
    }

    public void refreshCustomers() {
        customers = customerService.getAllCustomers();
    }

    public boolean registerCustomer(String id, String name, String personalNumber, String address, String email, String phone) {
        boolean success = customerService.registerCustomer(id, name, personalNumber, address, email, phone);
        if (success) refreshCustomers();
        return success;
    }

    public Customer findCustomerById(String id) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean accountNumberExists(String accountNumber) {
        return customers.stream()
                .flatMap(c -> accountService.getAccountsByCustomerId(c.getCustomerId()).stream())
                .anyMatch(acc -> acc.getAccountNumber().equals(accountNumber));
    }

    // Bankens saldo
    public double getBankBalance() {
        return bankBalance;
    }

    public void decreaseBankBalance(double amount) {
        bankBalance -= amount;
        bankBalanceService.updateBankBalance(bankBalance);
    }

    // Exempelmethod för insättning
    public synchronized boolean depositToCustomer(String customerId, String accountNumber, double amount) {
        if (amount <= 0 || amount > bankBalance) return false;
        Customer customer = findCustomerById(customerId);
        if (customer == null) return false;
        Account account = customer.getAccounts().stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
        if (account == null) return false;

        account.deposit(amount);
        accountService.updateAccountBalance(account.getAccountNumber(), account.getBalance());
        decreaseBankBalance(amount);
        return true;
    }
}
