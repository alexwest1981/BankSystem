package org.example;

import java.util.Date;

public class Transaction {
    public enum Type { DEPOSIT, WITHDRAWAL, TRANSFER }
    private Date date;
    private double amount;
    private Type type;
    private String description;

    public Transaction(double amount, Type type, String description) {
        this.date = new Date();
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    @Override
    public String toString() {
        return date + " - " + type + " - " + amount + " - " + description;
    }

    public Date getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

}
