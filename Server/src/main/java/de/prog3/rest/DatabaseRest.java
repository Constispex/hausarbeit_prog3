package de.prog3.rest;

import de.prog3.common.Book;
import de.prog3.common.Query;
import de.prog3.common.QueryBuilder;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Die Klasse verarbeitet Client Anfragen, die auf die Adresse "sqlquery" geleitet werden
 */
@Path("/sqlquery")
public class DatabaseRest {
    private static final Logger logger = LogManager.getLogger(DatabaseRest.class);

    /**
     * Methode bekommt eine SQL Query, führte diese aus und schickt eine LinkedList mit den Büchern an den Client zurück
     *
     * @param query die Query, die ausgeführt werden soll
     * @return Response mit dem Inhalt der Query Ergebnisse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSqlQuery(Query query) {
        String sql = new QueryBuilder().createSql(query);
        System.out.printf("[%s]: Query: %s%n", new SimpleDateFormat("HH:mm:ss").format(new java.util.Date()), sql);

        if (query.isDelete() || query.isUpdate() || query.isInsertInto()) {
            DbConnection.execute(sql);
            return Response.ok().build();
        }
        LinkedList<Book> books = DbConnection.getList(sql, getColumns(sql));

        System.out.printf("[%s]: Sent %s books%n", new SimpleDateFormat("HH:mm:ss").format(new java.util.Date()), books.size());
        return Response.ok(books).build();
    }

    /**
     * Zählt die Spalten und gibt diese als Liste zurück
     *
     * @param sql die Query, zum Ausgeben der Spalten
     * @return eine Liste mit Spalten
     */
    public List<String> getColumns(String sql) {
        List<String> cols = new ArrayList<>();
        if (sql.contains("SELECT *  FROM")) {
            cols.add("title");
            cols.add("author");
            cols.add("publisher");
            cols.add("rating");
            cols.add("subareas");
            return cols;
        }
        Scanner scanner = new Scanner(sql.toLowerCase());

        scanner.next();
        while (!scanner.hasNext("from")) {
            cols.add(scanner.next().replace(",", "").replace(" ", ""));

        }
        scanner.close();
        return cols;
    }
}