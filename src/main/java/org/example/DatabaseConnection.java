package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MariaDB driver not found");
        }
        String url = "jdbc:mariadb://localhost:3306/banksystem";
        String user = "bankuser";
        String password = "avedomov56";
        return DriverManager.getConnection(url, user, password);
    }
}