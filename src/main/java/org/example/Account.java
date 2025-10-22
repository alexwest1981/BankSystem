package org.example;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private double interestRate;
    private String clearingNumber;
    private double balance;

    private List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, double interestRate, String clearingNumber) {
        this.accountNumber = accountNumber;
        this.interestRate = interestRate;
        this.clearingNumber = clearingNumber;
        this.balance = 0.0;
    }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public String getClearingNumber() { return clearingNumber; }
    public void setClearingNumber(String clearingNumber) { this.clearingNumber = clearingNumber; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return transactions; }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add(new Transaction(Transaction.Type.DEPOSIT, amount, "Insättning"));
        } else {
            throw new IllegalArgumentException("Insättningsbeloppet måste vara positivt.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactions.add(new Transaction(Transaction.Type.WITHDRAWAL, -amount, "Uttag"));
            return true;
        } else {
            return false;
        }
    }

    public boolean transferTo(Account destination, double amount) {
        if (withdraw(amount)) {
            destination.deposit(amount);
            transactions.add(new Transaction(Transaction.Type.TRANSFER, -amount,
                    "Överföring till " + destination.getAccountNumber()));
            destination.transactions.add(new Transaction(Transaction.Type.TRANSFER, amount,
                    "Överföring från " + this.getAccountNumber()));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Konto: " + accountNumber + ", Clearing: " + clearingNumber +
                ", Ränta: " + interestRate + "%, Saldo: " + balance;
    }
}
