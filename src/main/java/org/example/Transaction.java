package org.example;

import java.util.Date;

public class Transaction {
    public enum Type { DEPOSIT, WITHDRAWAL, TRANSFER }

    private Date date;
    private double amount;
    private Type type;
    private String description;

    public Transaction(Type type, double amount, String description) {
        this.date = new Date();
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public Date getDate() { return date; }
    public double getAmount() { return amount; }
    public Type getType() { return type; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return date + " - " + type + " - " + amount + " - " + description;
    }
}
