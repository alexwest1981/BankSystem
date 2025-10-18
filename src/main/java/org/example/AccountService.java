package org.example;

import java.util.Random;

public class AccountService {
    private Bank bank = Bank.getInstance();

    public String generateAccountNumber() {
        Random rnd = new Random();
        return String.format("%04d%04d%04d", rnd.nextInt(9000) + 1000, rnd.nextInt(9000) + 1000, rnd.nextInt(9000) + 1000);
    }

    public String generateClearingNumber(String city) {
        switch (city.toLowerCase()) {
            case "stockholm": return "1111-1";
            case "göteborg": return "2222-2";
            // Fler städer
            default: return "0000-0";
        }
    }

    public boolean createAccountForCustomer(Customer customer, String accountNumber, double interestRate, String clearingNumber) {
        if (customer == null) return false;
        customer.openAccount(accountNumber, interestRate, clearingNumber);
        bank.saveToFile();
        return true;
    }

    public String formatAccountNumber(String number) {
        return number.replaceAll("(\\d{4})(\\d{4})(\\d{4})", "$1 $2 $3");
    }
}