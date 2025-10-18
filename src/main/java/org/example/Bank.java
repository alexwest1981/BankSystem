package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private static Bank instance = null;
    private List<Customer> customers;
    private double bankBalance = 1_000_000.0; // exempelstartsaldon för banken
    private static final String DATA_FILE = "bank_data.json";

    private Bank() {
        customers = new ArrayList<>();
    }

    public static synchronized Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
            instance.loadFromFile();
        }
        return instance;
    }

    public double getBankBalance() {
        return bankBalance;
    }

    public void decreaseBankBalance(double amount) {
        bankBalance -= amount;
    }


    public synchronized boolean depositToCustomer(String customerId, String accountNumber, double amount) {
        if (amount <= 0 || amount > bankBalance) return false;
        Customer customer = findCustomerById(customerId);
        if (customer == null) return false;
        Account account = customer.getAccounts().stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
        if (account == null) return false;

        // Gör insättning och minska bankens saldo
        account.deposit(amount);
        bankBalance -= amount;
        saveToFile();
        return true;
    }

    public void registerCustomer(String customerId, String name, String personalNumber, String address, String email, String phone) {
        Customer customer = new Customer(customerId, name, personalNumber, address, email, phone);
        customers.add(customer);
        saveToFile();
    }

    public boolean accountNumberExists(String accountNumber) {
        for (Customer c : customers) {
            for (Account a : c.getAccounts()) {
                if(a.getAccountNumber().equals(accountNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Customer findCustomerById(String id) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void printAllCustomers() {
        System.out.println("Kunder i banken:");
        if (customers.isEmpty()) {
            System.out.println("(Inga kunder registrerade)");
        }
        for (Customer c : customers) {
            System.out.println("ID: " + c.getCustomerId() + ", Namn: " + c.getName());
        }
    }

    public void loadFromFile() {
        try {
            if (Files.exists(Paths.get(DATA_FILE))) {
                Reader reader = Files.newBufferedReader(Paths.get(DATA_FILE));
                Gson gson = new Gson();
                Type customerListType = new TypeToken<ArrayList<Customer>>(){}.getType();
                List<Customer> loadedCustomers = gson.fromJson(reader, customerListType);
                if (loadedCustomers != null) {
                    customers = loadedCustomers;
                }
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(DATA_FILE));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(customers, writer);
            writer.close();
            System.out.println("Data sparad i fil: " + Paths.get(DATA_FILE).toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public boolean removeCustomerById(String customerId) {
        Customer c = findCustomerById(customerId);
        if (c != null) {
            customers.remove(c);
            saveToFile();
            return true;
        }
        return false;
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }
}
