package de.prog3.client.model;

/**
 * Die Buchklasse speichert alle wichtigen Informationen zu einem Buch. So können die Daten des Servers beim Client
 * besser verarbeitet werden.
 */
public class Book {
    private String title;
    private String author;
    private String publisher;
    private String rating;
    private String subareas;

    /**
     * Gibt alle Attribute zurück, fürs einfügen in eine SQL Query
     * Reihenfolge: Titel, Autor, Publisher, Rating, Subareas
     *
     * @param b Das Buch, welches umgewandelt werden soll
     * @return die umgewandelte Query
     */
    public String toSqlQuery(Book b) {
        return addQuotes(b.title) + ", " +
                addQuotes(b.author) + ", " +
                addQuotes(b.publisher) + ", " +
                addQuotes(b.rating) + ", " +
                addQuotes(b.subareas);
    }

    /**
     * Fügt Apostrophe vor und nach einem Attribut ein.
     *
     * @param s Das Attribut
     * @return Das Attribut mit Apostrophe
     */
    private String addQuotes(String s) {
        return "'" + s + "'";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setSubareas(String subareas) {
        this.subareas = subareas;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getRating() {
        return rating;
    }

    public String getSubareas() {
        return subareas;
    }

    /**
     * Überschriebene toString Methode zum Ausgeben in der Konsole
     *
     * @return gibt alle Attribute der Klasse Buch zurück
     */
    @Override
    public String toString() {
        return "Buch{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", rating='" + rating + '\'' +
                ", subareas='" + subareas + '\'' +
                '}';
    }
}
