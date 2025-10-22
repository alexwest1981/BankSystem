package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankBalanceService {

    // Hämtar bankens saldo från databasen
    public double getBankBalance() {
        String sql = "SELECT balance FROM bank_balance WHERE id = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("balance");
            } else {
                // Om raden saknas returnera 0
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Initierar bankens saldo med ett startsaldo (körs en gång)
    public void initializeBankBalance(double startBalance) {
        String sql = "INSERT INTO bank_balance (id, balance) VALUES (1, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, startBalance);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Uppdaterar bankens saldo i databasen
    public void updateBankBalance(double newBalance) {
        String sql = "UPDATE bank_balance SET balance = ? WHERE id = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
