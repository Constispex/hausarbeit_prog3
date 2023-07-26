package de.prog3.rest;

import de.prog3.server.DbConnection;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Die Klasse verarbeitet Client Anfragen, die auf die Adresse "sqlquery" geleitet werden
 */
@Path("/sqlquery")
public class DatabaseRest {
    private static final Logger logger = LogManager.getLogger(DatabaseRest.class);
    /**
     * Die Methode wartet auf eine SQL Query und gibt das Ergebnis zurück. Zudem wird die Query in der Konsole ausgegeben
     *
     * @param res die SQL Query
     * @return die SQL Query bzw der Status
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getSqlQuery(String res) {
        logger.info("Query executed: {}", res);
        ResultSet rs = DbConnection.execute(res);
        if (!res.contains("SELECT")) return Response.ok().build();

        String result = DbConnection.rsToString(Objects.requireNonNull(rs), getColumns(res));
        try {
            rs.close();
            return Response.ok(result).build();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * Zählt die Spalten
     *
     * @param sql die Query, bei der die Spalten gezählt werden sollen
     * @return die Anzahl der Spalten
     */
    int getColumns(String sql) {
        if (sql.contains("SELECT  * FROM")) return 5;
        Scanner scanner = new Scanner(sql.toUpperCase());
        int columns = 0;
        scanner.next();
        while (!scanner.hasNext("FROM")) {
            scanner.next();
            columns++;
        }
        scanner.close();
        return columns;
    }
}