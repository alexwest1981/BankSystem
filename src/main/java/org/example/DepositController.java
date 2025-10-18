package org.example;

import org.example.Bank;
import org.example.Customer;
import org.example.Account;

public class DepositController {
    private Bank bank = Bank.getInstance();

    public boolean depositToCustomerAccount(String customerId, String accountNumber, double amount) {
        if (amount <= 0) return false;

        if (bank.getBankBalance() < amount) {
            return false; // Inte tillräckligt saldo i banken
        }

        Customer customer = bank.findCustomerById(customerId);
        if (customer == null) return false;

        Account account = customer.getAccounts().stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
        if (account == null) return false;

        // Dra av från bankens saldo
        bank.decreaseBankBalance(amount);
        //Sätt in på kundens konto
        account.deposit(amount);
        // Spara ändringarna
        bank.saveToFile();

        return true;
    }
}
