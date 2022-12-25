package de.prog3.server;


import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <Janik> <m29127>
 * @author <Christina> <m29268>
 * date:2021-11-22
 * Programmierung 3,  Aufgabe:9
 * Beschreibung der Klasse:
 * Main Klasse des Server-Moduls,
 * Setzt mit Hilfe von jersey und grizzly einen webserver auf.
 * besitzt nur eine main-Methode, die den server startet.
 * <p>
 * Info :das programm wurde mit java 15 geschrieben, da wir die java 17 version
 * mit gradle nicht zum laufen bekommen haben
 */

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        final String BASE_URI = "http://localhost:8080/rest";
        Logger.getLogger("org.glassfish").setLevel(Level.SEVERE);

        URI baseURI = new URI(BASE_URI);
        ResourceConfig config =
                ResourceConfig.forApplicationClass(DataBaseApplication.class);
        HttpServer server =
                GrizzlyHttpServerFactory.createHttpServer(baseURI, config);
        StaticHttpHandler handler = new StaticHttpHandler("web");
        handler.setFileCacheEnabled(false);
        ServerConfiguration serverConfig = server.getServerConfiguration();
        serverConfig.addHttpHandler(handler, "/");

        if (!server.isStarted()) server.start();

        System.out.println("ENTER stoppt den Server");
        System.in.read();
        server.shutdownNow();

    }
}
