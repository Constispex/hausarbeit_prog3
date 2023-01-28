package de.prog3.common;

/**
 * Speichert Name und Passwort und Adminrechte von einem Benutzer.
 */
public class User {
    private final String name;
    private final String password;
    private final boolean isAdmin;

    /**
     * Constuctor zum setzten der Werte
     *
     * @param name     Username
     * @param password Password
     * @param isAdmin  hat Adminrechte
     */
    public User(String name, String password, boolean isAdmin) {
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
