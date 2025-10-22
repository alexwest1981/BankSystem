package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    // Lagra kunder lokalt i minnet, synkroniseras mot DB via reloadCustomers
    private List<Customer> customers = new ArrayList<>();

    public void reloadCustomers() {
        customers.clear();
        customers.addAll(getAllCustomers());
    }

    public List<Customer> getCustomerList() {
        return customers;
    }

    public Customer findCustomerByPersonalNumber(String pnr) {
        for (Customer c : customers) {
            if (c.getPersonalNumber().equals(pnr)) {
                return c;
            }
        }
        return null;
    }

    public Customer findCustomerByCustomerId(String customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customerId)) {
                return c;
            }
        }
        return null;
    }

    public boolean registerCustomer(String id, String name, String pnr, String address, String email, String phone) {
        if (findCustomerByPersonalNumber(pnr) != null) return false;

        String sql = "INSERT INTO customers (id, name, personal_number, address, email, phone) Values (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, pnr);
            stmt.setString(4, address);
            stmt.setString(5, email);
            stmt.setString(6, phone);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                reloadCustomers(); // Uppdatera lokalt cache
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> result = new ArrayList<>();
        String sql = "SELECT id, name, personal_number, address, email, phone FROM customers";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("personal_number"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
