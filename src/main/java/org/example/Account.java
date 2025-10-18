package org.example;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private double balance;
    private double interestRate;
    private String clearingNumber; // Nytt fält för clearingnummer

    private List<Transaction> transactions;

    public Account(String accountNumber, double interestRate, String clearingNumber) {
        this.accountNumber = accountNumber;
        this.interestRate = interestRate;
        this.clearingNumber = clearingNumber;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getClearingNumber() {
        return clearingNumber;
    }

    public void setClearingNumber(String clearingNumber) {
        this.clearingNumber = clearingNumber;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(double amount) {
        if(amount > 0) {
            balance += amount;
            transactions.add(new Transaction(Transaction.Type.DEPOSIT, amount, "Insättning"));
        }
    }

    public void withdraw(double amount) throws Exception {
        if(amount > 0 && balance >= amount) {
            balance -= amount;
            transactions.add(new Transaction(Transaction.Type.WITHDRAWAL, amount, "Uttag"));
        } else {
            throw new Exception("Otillräckligt saldo för uttag");
        }
    }

    public void transferTo(Account target, double amount) throws Exception {
        if (target == null) {
            throw new Exception("Mottagarkonto saknas");
        }
        if (this.equals(target)) {
            throw new Exception("Kan ej överföra till samma konto");
        }
        this.withdraw(amount);
        target.deposit(amount);
        transactions.add(new Transaction(Transaction.Type.TRANSFER, amount, "Överfört till konto " + target.getAccountNumber()));
        target.getTransactions().add(new Transaction(Transaction.Type.TRANSFER, amount, "Mottagit från konto " + this.getAccountNumber()));
    }
}
