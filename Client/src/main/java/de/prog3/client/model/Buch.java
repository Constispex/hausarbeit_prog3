package de.prog3.client.model;

public class Buch {

    String title;
    String author;
    String publisher;
    String rating;
    String subareas;

    public Buch(String title, String author, String publisher, String rating, String subareas) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.rating = rating;
        this.subareas = subareas;
    }

    public Buch() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSubareas() {
        return subareas;
    }

    public void setSubareas(String subareas) {
        this.subareas = subareas;
    }

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
