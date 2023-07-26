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

        final Level loglevel = Level.getLevel(String.valueOf(getLevel(args[0])));
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig loggerConfig =
                config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(loglevel);
        context.updateLoggers();



        try {
            Main.main(args);
        } catch (IOException | URISyntaxException e) {
            logger.fatal("Server konnte nicht gestartet werden: {}", e.getMessage());
        }
    }

    private static Level getLevel(String arg) {
        return switch (arg) {
            case "info" -> Level.INFO;
            case "debug" -> Level.DEBUG;
            case "warn" -> Level.WARN;
            case "error" -> Level.ERROR;
            case "fatal" -> Level.FATAL;
            default -> Level.ALL;
        };
    }
}
