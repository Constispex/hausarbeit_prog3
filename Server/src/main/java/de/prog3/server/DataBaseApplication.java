package de.prog3.server;

import de.prog3.rest.DatabaseRest;
import de.prog3.rest.UserRest;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

/**
 * Die Klasse inkludiert die UserRest und DatabaseRest Klassen für den Empfang von Serveranfragen.
 */
public class DataBaseApplication extends Application {
    private final Set<Class<?>> classes = new HashSet<>();

    public DataBaseApplication() {
        classes.add(UserRest.class);
        classes.add(DatabaseRest.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
