package de.prog3.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Speichert Name und Passwort und Adminrechte von einem Benutzer.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @JsonProperty("name")
    public String name;
    @JsonProperty("password")
    public String password;
    @JsonProperty("isAdmin")
    public boolean isAdmin;

    public User() {
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
