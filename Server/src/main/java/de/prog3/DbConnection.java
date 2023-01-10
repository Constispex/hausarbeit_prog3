package de.prog3;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class DbConnection {

    public DbConnection() {
        checkIfDatabaseExist();
    }

    public static void main(String[] args) {
        new DbConnection().getConnection();
    }

    public java.sql.Connection getConnection() {
        java.sql.Connection con = null;
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/", "root", "");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        Objects.requireNonNull(con);

        return con;
    }

    private boolean checkIfDatabaseExist() {
        boolean exist = false;
        try {
            Statement statement = getConnection().createStatement();
            String sql = "CREATE DATABASE Informatik";
            statement.executeUpdate(sql);
            statement.close();
            System.out.println("Database had to be created");
        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 1007) { // Database already exist
                exist = true;
                System.out.println(sqlException.getMessage());
            } else {
                sqlException.printStackTrace();
            }
        }
        return exist;
    }

    public PreparedStatement getPreparedStatement() {
        try {
            return getConnection().prepareStatement("SELECT ? FROM Informatik WHERE ?");
        } catch (SQLException e) {
            System.err.println(e.getSQLState());
        }
        return null;
    }

}
