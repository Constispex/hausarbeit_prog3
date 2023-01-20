package de.prog3;

import java.sql.*;
import java.util.Objects;

public class DbConnection {
    private static final String CURR_DATABASE = "Informatik";
    private static boolean dbExists = false;
    private static boolean tableExist = false;

    private DbConnection(){

    }

    public static ResultSet execute(String res) {
        if (!dbExists) createDatabase();
        if (!tableExist) createTable();

        Statement statement = null;
        try {
            statement = getConnection(CURR_DATABASE).createStatement();
            statement.closeOnCompletion();
            return statement.executeQuery(res);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                Objects.requireNonNull(statement).close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    public static java.sql.Connection getConnection(String database) {
        java.sql.Connection con = null;
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + database, "root", "");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        Objects.requireNonNull(con);

        return con;
    }


    private static void createTable() {
        Statement statement = null;
        try {
            if (!dbExists) createDatabase();
            statement = getConnection(CURR_DATABASE).createStatement();
            String sql = """
                create table IF NOT EXISTS Buecher
                (
                    Title     varchar(150) not null,
                    Author    varchar(600) null,
                    Publisher varchar(200) null,
                    Rating    int(1)       null,
                    Subareas  varchar(200) null,
                    constraint Informatik_pk
                        primary key (Title)
                );
                """;
            statement.executeUpdate(sql);
            statement.close();
            tableExist = true;
            //insertTestColumn();

        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 1007) { // Database already exist
                System.out.println(sqlException.getMessage());
            } else {
                sqlException.printStackTrace();
            }
        } finally {
            try {
                Objects.requireNonNull(statement).close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void createDatabase() {
        Statement statement = null;
        try {
            statement = getConnection("").createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS " + CURR_DATABASE;
            statement.execute(sql);
            dbExists = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                Objects.requireNonNull(statement).close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void insertTestColumn() {
        Statement statement = null;
        try {
            statement = getConnection(CURR_DATABASE).createStatement();
            String sql = """
            INSERT INTO buecher(Title, Author, Publisher, Rating, Subareas) 
            VALUES ('Harry Potter 1', 'J.K. Rowling', 'Carlsen Verlag', '5', 'Magic, Fantasy') 
            """;
            statement.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                Objects.requireNonNull(statement).close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
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
        System.out.println(sb + " " + columns);
        return sb.toString();
    }
}
