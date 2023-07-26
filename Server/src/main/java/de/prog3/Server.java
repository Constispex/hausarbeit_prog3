package de.prog3;

import de.prog3.server.Main;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.core.LoggerContext;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.IOException;
import java.net.URISyntaxException;


public class Server {

    private static final Logger logger = LogManager.getLogger(Server.class);

    public static void main(String[] args) {
        final Level LOGLEVEL = Level.ALL;
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig loggerConfig =
                config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(LOGLEVEL);
        context.updateLoggers();


        logger.info("Nutzer-Info");
        logger.debug("Nutzer-Debug");
        logger.warn("Nutzer-Warnung");
        logger.error("Nutzer-Fehler");
        logger.fatal("Nutzer-Fatal");

        try {
            Main.main(args);
        } catch (IOException | URISyntaxException e) {
            logger.fatal("Server konnte nicht gestartet werden: {}", e.getMessage());
        }
    }
}
