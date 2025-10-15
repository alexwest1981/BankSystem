package org.example;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private double balance;
    private double interestRate;
    private List<Transaction> transactions;

    // Konstruktor
    public Account(String accountNumber, double interestRate) {
        this.accountNumber = accountNumber;
        this.interestRate = interestRate;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    // Insättning
    public void  deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }
    }

    // Uttag
    public void withdraw(double amount) throws Exception {
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }
        if (balance < amount) {
            throw new Exception("Insufficient funds.");
        }
        balance -= amount;
        transactions.add(new Transaction(amount, Transaction.Type.WITHDRAWAL, "Uttag"));
    }

    // Överföring till annat konto
    public void transferTo(Account targetAccount, double amount) throws Exception {
        this.withdraw(amount);
        targetAccount.deposit(amount);
        transactions.add(new Transaction(amount, Transaction.Type.TRANSFER, "Överföring"));
    }

    // Beräkna och lägg till ränta
    public void applyInteres() {
        double interest = balance * interestRate;
        balance += interest;
        transactions.add(new Transaction(interest, Transaction.Type.DEPOSIT, "Ränta"));
    }

    // Getters
    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public double getInterestRate() {
        return interestRate;
    }
}
