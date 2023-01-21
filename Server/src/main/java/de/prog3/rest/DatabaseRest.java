package de.prog3.rest;

import de.prog3.DbConnection;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

@Path("/sqlquery")
public class DatabaseRest {
    private HashMap<String, ResultSet> queries;

    public DatabaseRest() {

    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getSqlQuery(String res, @Context UriInfo uriInfo) {
        System.out.printf("[%s]: Query: %s%n",
                new SimpleDateFormat("HH:mm:ss").format(new java.util.Date()), res);
        ResultSet rs = DbConnection.execute(res);

        String result = DbConnection.rsToString(Objects.requireNonNull(rs), getColumns(res));
        try {
            rs.close();
            System.out.printf("[%s]: sendToClient: %s%n",
                    new SimpleDateFormat("HH:mm:ss").format(new java.util.Date()), result);
            return Response.ok(result).build();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return Response.serverError().build();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response postQuery(String query) {
        ResultSet curr = queries.get(query);
        String res = DbConnection.rsToString(curr, getColumns(query));

        return Response.ok(res).build();
    }

    int getColumns(String sql){
        if (sql.contains("SELECT  * FROM")) return 5;
        Scanner scanner = new Scanner(sql.toUpperCase());
        int columns=0;
        scanner.next();
        while (!scanner.hasNext("FROM")){
            scanner.next();
            columns++;
        }
        scanner.close();
        return columns;
    }
}