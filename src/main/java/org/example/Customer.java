package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {
    private String customerId;
    private String name;
    private String personalNumber;
    private String address;
    private String email;
    private String phone;

    private List<Account> accounts;

    // Konstruktör med alla fält och initierar kontolistan
    public Customer(String customerId, String name, String personalNumber, String address, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.personalNumber = personalNumber;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.accounts = new ArrayList<>();
    }

    // Getters och setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPersonalNumber() { return personalNumber; }
    public void setPersonalNumber(String personalNumber) { this.personalNumber = personalNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<Account> getAccounts() { return accounts; }

    // Metod för att skapa och koppla konto till kunden
    public void openAccount(String accountNumber, double interestRate, String clearingNumber) {
        Account account = new Account(accountNumber, interestRate, clearingNumber);
        accounts.add(account);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }

    @Override
    public String toString() {
        return customerId + " - " + name;
    }
}
