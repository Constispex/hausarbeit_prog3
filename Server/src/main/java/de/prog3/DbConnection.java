package de.prog3;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class DbConnection {
    private static final String CURR_DATABASE = "Informatik";

    private DbConnection(){

    }

    public static ResultSet execute(String res) {
        Statement statement = null;
        try {
            statement = getConnection(CURR_DATABASE).createStatement();
            statement.closeOnCompletion();
            return statement.executeQuery(res);
        } catch (SQLException e) {
            System.err.println("Error in execute: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(statement).close();
            } catch (SQLException e) {
                System.err.println("Error in execute: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    public static java.sql.Connection getConnection(String database) {
        java.sql.Connection con = null;
        try {
            con = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/" + database, "root", "");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        Objects.requireNonNull(con);
        return con;
    }


    private static void createTable() throws SQLException, IOException {
        String sql = "SHOW TABLES LIKE 'Buecher'";
        ResultSet rs;
        try (Statement stIfExists = getConnection(CURR_DATABASE).createStatement()) {
            stIfExists.execute(sql);
            rs = stIfExists.getResultSet();
        }
        if (!rs.next()) {
            try (Scanner scanner = new Scanner(Path.of("sql/Informatik.sql"));
                 Statement stCreateAndInsert = getConnection(CURR_DATABASE).createStatement()) {
                StringBuilder query = new StringBuilder();

                while (scanner.hasNext()) {
                    String next = scanner.nextLine();
                    if (next.contains(";")) {
                        query.append(next);
                        System.out.println("execute: " + query);
                        stCreateAndInsert.execute(query.toString());
                        query = new StringBuilder();
                    } else {
                        query.append(next);
                    }
                }
            }
        }
    }

    private static void createDatabase() throws SQLException {
        Statement statement = null;
        String sql = "CREATE DATABASE IF NOT EXISTS " + CURR_DATABASE;
        try {
            statement = getConnection("").createStatement();
            statement.execute(sql);
        } finally {
            Objects.requireNonNull(statement).close();
        }
    }

    public static String rsToString(ResultSet rs, int columns) {
        StringBuilder sb = new StringBuilder();

        try {
            while (rs.next()){
                for (int i = 1; i <= columns; i++) {
                    sb.append(rs.getString(i)).append("; ");
                }
                sb.append("// ");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return sb.toString();
    }

    public static boolean setUpDatabase() {
        try {
            createDatabase();
            createTable();
        } catch (SQLException e) {
            System.err.println("Error while setting up Database");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("sql file not found");
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}