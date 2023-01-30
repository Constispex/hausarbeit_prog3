package de.prog3.common;

/**
 * Speichert Name und Passwort und Adminrechte von einem Benutzer.
 */
public class User {

    public String name;
    public String password;
    public String isAdmin;

    public User() {
    }

    /**
     * Constuctor zum setzten der Werte
     *
     * @param name     Username
     * @param password Password
     * @param isAdmin  hat Adminrechte
     */
    public User(String name, String password, String isAdmin) {
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getAdmin() {
        return isAdmin;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


}
