package de.prog3.client;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Connection {
    public java.sql.Connection getConnection() {
        java.sql.Connection con = null;
        try {
            con = DriverManager.getConnection(
                    "jdbc:mariadb://localhost/Informatik", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            System.err.println(con.getWarnings());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return con;
    }

    public PreparedStatement getPreparedStatement() {
        try {
            return getConnection().prepareStatement("SELECT ? FROM Informatik WHERE ?");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
