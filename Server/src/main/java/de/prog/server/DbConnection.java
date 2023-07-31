package de.prog.server;

import de.prog.common.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Beinhaltet sämtliche Methoden zum Verwalten der Datenbankverbindung.
 * Es wird die Database Informatik verwendet.
 */
public class DbConnection {

    private static final Logger logger = LogManager.getRootLogger();
    private static final String CURR_DATABASE = "Informatik";

    /**
     * privater Constructor, da die Klasse nur aus static Methoden besteht und diese Klasse nicht instanziiert werden soll
     */
    private DbConnection() {
    }

    /**
     * Führt eine SQL Query aus
     *
     * @param res mySql Query
     * @return ResultSet mit dem Ergebnis
     */
    public static ResultSet execute(String res) {
        Statement statement = null;
        try {
            statement = getConnection(CURR_DATABASE).createStatement();
            statement.closeOnCompletion();
            return statement.executeQuery(res);
        } catch (SQLException e) {
            logger.error("Error in Query {}", e.getMessage());

        } finally {
            try {
                Objects.requireNonNull(statement).close();
            } catch (SQLException e) {
                logger.error("Error in Query {}", e.getMessage());
            } catch (NullPointerException e) {
                logger.error("Connection not found: {}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * Führt die Query aus und erstellt aus dem erstellten ResultSet eine Liste mit den Büchern.
     *
     * @param sql     die auszuführende Query
     * @param columns Liste mit Spalten
     * @return Liste mit Büchern
     */
    public static LinkedList<Book> getList(String sql, List<String> columns) {
        ResultSet resultSet = Objects.requireNonNull(execute(sql));
        LinkedList<Book> books = new LinkedList<>();
        try {
            while (resultSet.next()) {
                Book curr = new Book();
                for (int i = 1; i <= columns.size(); i++) {
                    switch (columns.get(i - 1)) {
                        case "title" -> curr.setTitle(resultSet.getString(i));
                        case "author" -> curr.setAuthor(resultSet.getString(i));
                        case "publisher" -> curr.setPublisher(resultSet.getString(i));
                        case "rating" -> curr.setRating(resultSet.getString(i));
                        case "subareas" -> curr.setSubareas(resultSet.getString(i));
                        default -> logger.debug(new RuntimeException("no cols found!"));
                    }
                }
                books.add(curr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error in Query {}", e.getMessage());
        }
        return books;
    }

    /**
     * Stellt die Verbindung mit einer bestimmten Datenbank her. Dafür wird der JDBC Treiber verwendet.
     *
     * @param database Die Datenbank, die verwendet wird
     * @return Datenbankverbindung
     */
    public static java.sql.Connection getConnection(String database) {
        java.sql.Connection con = null;
        try {
            DriverManager.setLogWriter(new PrintWriter(System.out));
            con = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/" + database, "root", "");

        } catch (SQLException e) {
            logger.error("Error while connecting to Database: {}", e.getMessage());
        }
        Objects.requireNonNull(con);

        return con;
    }

    /**
     * Überprüft, ob Tabelle bereits existiert (hat SHOW TABLES min. 1 Ergebnis?)
     *
     * @throws SQLException Wirft eine Fehlermeldung, falls die Anfrage falsch ist, oder es einen Fehler mit der Datenbank gibt
     * @throws IOException  Falls der Scanner einen Fehler wirft
     */
    private static void createTable() throws SQLException, IOException {
        String sql = "SHOW TABLES LIKE 'buecher'";
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
                        logger.info("Query executed.");
                        logger.debug("Query: {}", query);
                        stCreateAndInsert.execute(query.toString());
                        query = new StringBuilder();
                    } else {
                        query.append(next);
                    }
                }
            }
        }
    }

    /**
     * Erstellt die Database, falls diese nicht existiert.
     *
     * @throws SQLException Wirft eine Fehlermeldung, falls die Anfrage falsch ist, oder es einen Fehler mit der Datenbank gibt
     */
    private static void createDatabase() throws SQLException, NullPointerException {
        Statement statement = null;
        String sql = "CREATE DATABASE IF NOT EXISTS " + CURR_DATABASE;
        try {
            statement = getConnection("").createStatement();
            statement.execute(sql);
        } finally {
            Objects.requireNonNull(statement).close();
        }
    }

    /**
     * Erstellt Datenbank, falls diese noch nicht existiert. Bei einem Fehler wird der Server gestoppt.
     *
     * @return True, wenn Datenbank erfolgreich aufgesetzt worden ist.
     */
    public static boolean setUpDatabase() {
        try {
            createDatabase();
            createTable();
        } catch (SQLException e) {
            logger.error("Error while setting up Database: {}", e.getMessage());
            return false;
        } catch (IOException e) {
            logger.error("sql file not found {}", e.getMessage());
            return false;
        } catch (NullPointerException e) {
            logger.error("Connection not found: {}", e.getMessage());
            return false;
        }
        return true;
    }
}