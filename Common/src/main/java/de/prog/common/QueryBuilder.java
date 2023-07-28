package de.prog.common;

/**
 * Die Klasse erstellt Querys aus Büchern oder Strings.
 * Sie füllt das Query POJO mit den nötigen Informationen, damit diese dann an den Server geschickt werden können.
 */
public class QueryBuilder {

    /**
     * Erstellt ein POJO zum Ausführen einer Selectanfrage
     *
     * @param select  Spalten von der Select Anfrage
     * @param from    Datenbank
     * @param where   Whereteil der Anfrage
     * @param orderby Orderby der Anfrage
     * @return Query mit allen Informationen zum Ausführen
     */
    public Query buildSelect(String select, String from, String where, String orderBy) {
        Query q = new Query();
        q.setSelect("SELECT " + select);
        q.setFrom(" FROM " + from);
        q.setWhere(" WHERE " + where);
        if (!orderBy.isEmpty()) q.setOrderBy(" ORDER BY " + orderBy);
        return q;
    }

    /**
     * Erstellt eine Query zum Löschen eines Buches.
     *
     * @param book das Buch, welches gelöscht werden soll.
     * @return eine Query zum Löschen des Buches.
     */
    public Query buildDelete(Book book) {
        Query q = new Query();
        q.setDelete(true);
        q.setFrom("buecher ");
        q.setWhere("WHERE Title = \"" + book.getTitle() + "\"");
        return q;
    }

    /**
     * Erstellt eine Query zum Bearbeiten eines Buches.
     *
     * @param book         das Buch, welches Bearbeitet wird.
     * @param oldBookTitle der alte Buchtitel zum Identifizieren.
     * @return die Query, zum Bearbeiten des Buches
     */
    public Query buildUpdate(Book book, String oldBookTitle) {
        Query q = new Query();
        q.setUpdate(true);
        q.setUpdateContent("UPDATE Buecher SET " +
                "title = '" + book.getTitle() + "', " +
                "author = '" + book.getAuthor() + "', " +
                "publisher = '" + book.getPublisher() + "', " +
                "rating = '" + book.getRating() + "', " +
                "subareas = '" + book.getSubareas() + "' " +
                "WHERE title = '" + oldBookTitle + "';");
        return q;
    }

    /**
     * Erstellt eine Query zum Hinzufügen eines Buches.
     *
     * @param book das Buch, welches Hinzugefügt werden soll.
     * @return die Query zum Erstellen des Buches.
     */
    public Query buildAdd(Book book) {
        Query q = new Query();
        q.setInsertInto(true);
        q.setFrom("buecher ");
        q.setInsertIntoContent("Title, Author, Publisher, Rating, Subareas) VALUES (" + book.toSqlQuery(book) + ");");
        return q;
    }

    /**
     * Erstellt aus einer Query den SQL-String zum Ausführen
     *
     * @param q die Query zum Ausführen
     * @return den String zum Ausführen einer SQL Anfrage
     */
    public String createSql(Query q) {
        StringBuilder sb = new StringBuilder();

        if (q.isInsertInto()) {
            sb.append("INSERT INTO buecher (").append(q.getInsertIntoContent());
            return sb.toString();
        }
        if (q.isUpdate()) {
            return sb.append(q.getUpdateContent()).toString();
        }
        if (q.isDelete()) sb.append("DELETE FROM ");
        else sb.append(q.getSelect());
        sb.append(q.getFrom());
        if (!q.getWhere().isBlank()) sb.append(q.getWhere());
        if (q.getOrderBy() != null) sb.append(q.getOrderBy());

        return sb.toString();
    }
}