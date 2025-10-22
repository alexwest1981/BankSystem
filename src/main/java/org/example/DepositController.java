package org.example;

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

        bank.decreaseBankBalance(amount); // Dra av från bankens saldo
        account.deposit(amount);          // Sätt in på kundens konto

        // Skriv till databasen
        bank.getAccountService().updateAccountBalance(account.getAccountNumber(), account.getBalance());

        return true;
    }
}
