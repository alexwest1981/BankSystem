package org.example;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String name;
    private List<Account> accounts;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    // Skapa och lägg till nytt konto för kunden
    public Account openAccount(String accountNumber, double interestRate) {
        Account newAccount = new Account(accountNumber, interestRate);
        accounts.add(newAccount);
        return newAccount;
    }

    // Hämta kundens konto
    public List<Account> getAccounts() {
        return accounts;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}
