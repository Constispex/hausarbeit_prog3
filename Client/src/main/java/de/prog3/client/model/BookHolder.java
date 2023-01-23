package de.prog3.client.model;

public class BookHolder {
    private Book book;
    private static final BookHolder INSTANCE = new BookHolder();

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
