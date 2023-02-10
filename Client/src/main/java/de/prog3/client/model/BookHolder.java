package de.prog3.client.model;

import de.prog3.common.Book;

/**
 * Singleton fürs Speichern eines ausgewählten Buches.
 * Wird verwendet, um bei einem Szenenwechsel das ausgewählte Buch abzurufen
 */
public class BookHolder {
    private Book book;
    private static final BookHolder INSTANCE = new BookHolder();

    /**
     * privater Constructor, damit nur eine Instanz existiert
     */
    private BookHolder() {
    }
    public static BookHolder getInstance() {
        return INSTANCE;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
}
