package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AccountService {
    // Simulerad lista av kontorsstäder, kan ersättas med databasfråga om du vill
    private List<String> cities;

    public AccountService() {
        // Hämta alla nycklar (städer) från CityClearingNumbers och sortera dem
        cities = new ArrayList<>(CityClearingNumbers.CITY_CLEARING_MAP.keySet());
        Collections.sort(cities);
    }

    public List<String> getAllCitiesSorted() {
        return new ArrayList<>(cities);
    }

    // Genererar ett nytt unikt kontonummer (12 siffror)
    public String generateAccountNumber() {
        Random rnd = new Random();
        return String.format("%04d%04d%04d", rnd.nextInt(9000) + 1000,
                rnd.nextInt(9000) + 1000,
                rnd.nextInt(9000) + 1000);
    }


    // Hämtar clearingnummer via CityClearingNumbers-klassen
    public String generateClearingNumber(String city) {
        return CityClearingNumbers.getClearingNumber(city);
    }

    // Skapar ett konto i databasen
    public boolean createAccount(String accountNumber, String customerId,
                                 double interestRate, double initialBalance,
                                 String clearingNumber) {
        String sql = "INSERT INTO accounts (number, customer_id, interest_rate, balance, clearing_number) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            stmt.setString(2, customerId);
            stmt.setDouble(3, interestRate);
            stmt.setDouble(4, initialBalance);
            stmt.setString(5, clearingNumber);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Skapar ett konto kopplat till kundobjektet
    public boolean createAccountForCustomer(Customer customer, String accountNumber,
                                            double interestRate, String clearingNumber) {
        if (customer == null || accountNumber == null || clearingNumber == null) {
            return false;
        }
        double initialBalance = 0.0;
        return createAccount(accountNumber, customer.getCustomerId(), interestRate, initialBalance, clearingNumber);
    }

    // Formaterar kontonummer (exempel: delar upp i grupper om 4 siffror med mellanslag)
    public String formatAccountNumber(String accountNumber) {
        if (accountNumber == null) return "";
        return accountNumber.replaceAll("(\\d{4})(?=\\d)", "$1 ");
    }

    // Uppdaterar saldot för ett konto
    public boolean updateAccountBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hämtar alla konton för en viss kund
    public List<Account> getAccountsByCustomerId(String customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT number, interest_rate, balance, clearing_number FROM accounts WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Account acc = new Account(
                            rs.getString("number"),
                            rs.getDouble("interest_rate"),
                            rs.getString("clearing_number")
                    );
                    acc.setBalance(rs.getDouble("balance"));  // Sätt saldo korrekt vid läsning
                    accounts.add(acc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    // Hämtar bankens totala saldo (summa av alla kontons saldo)
    public double getBankBalance() {

        String sql = "SELECT SUM(balance) AS totalBalance FROM accounts";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("totalBalance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Om något går fel, returnera 0
    }
}
