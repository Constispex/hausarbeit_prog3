package de.prog3.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



/**
 * Die Main Klasse startet den REST-Server und startet eine Überprüfung der Datenbankverbindung.
 * Mit Enter kann der Server wieder gestoppt werden.
 */
public class Main {
    private static final Logger logger = LogManager.getRootLogger();
    /**
     * Startet den Server und überprüft die Datenbankverbindung
     *
     * @param args keine
     * @throws IOException        falls der Server nicht gestartet werden kann
     * @throws URISyntaxException falls die URI nicht stimmt
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        final String BASE_URI = "http://localhost:8080/rest";


        URI baseURI = new URI(BASE_URI);
        ResourceConfig config =
                ResourceConfig.forApplicationClass(DataBaseApplication.class);
        HttpServer server =
                GrizzlyHttpServerFactory.createHttpServer(baseURI, config);

        StaticHttpHandler handler = new StaticHttpHandler("web");
        handler.setFileCacheEnabled(false);
        ServerConfiguration serverConfig = server.getServerConfiguration();
        serverConfig.addHttpHandler(handler, "/");

        // set up Database
        if (DbConnection.setUpDatabase()) {
            logger.info("Database set up completed");
        } else {
            logger.fatal("Server shut down - Database set up failed");
            server.shutdown();
        }
        if (!server.isStarted()) server.start();

        logger.info("Enter stops the server");
        System.in.read();
        server.shutdownNow();

    }
}
