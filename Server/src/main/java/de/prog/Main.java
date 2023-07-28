package de.prog;

import de.prog.server.Server;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.IOException;
import java.net.URISyntaxException;


public class Main {
    private static final Logger logger = LogManager.getRootLogger();
    public static void main(String[] args) {
        final Level LOGLEVEL = Level.DEBUG;
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        LoggerConfig loggerConfig =
                config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(LOGLEVEL);
        context.updateLoggers();

        context.start();

        logger.info("Server wird gestartet");
        logger.info("Logger Level: {}", LOGLEVEL);
        try {
            Server.main(args);
        } catch (IOException | URISyntaxException e) {
            logger.fatal("Server konnte nicht gestartet werden: {}", e.getMessage());
        }
    }


}
