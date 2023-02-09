package de.prog3.common;

public class QueryBuilder {

    public Query buildSelect(String select, String from, String where, String orderby) {
        Query q = new Query();
        q.setSelect("SELECT " + select);
        q.setFrom(" FROM " + from);
        q.setWhere(" WHERE " + where);
        if (!orderby.isEmpty()) q.setOrderBy("ORDER BY " + orderby);
        return q;
    }

    public Query buildDelete(Book book) {
        Query q = new Query();
        q.setDelete(true);
        q.setFrom("buecher ");
        q.setWhere("WHERE Title = \"" + book.getTitle() + "\"");
        return q;
    }

    public Query buildUpdate(Book book) {
        Query q = new Query();
        q.setUpdate(true);
        q.setInsertIntoContent("UPDATE Buecher SET " +
                "title = '" + book.getTitle() + "', " +
                "author = '" + book.getAuthor() + "', " +
                "publisher = '" + book.getPublisher() + "', " +
                "rating = '" + book.getRating() + "', " +
                "subareas = '" + book.getSubareas() + "' " +
                "WHERE title = '" + book.getTitle() + "'");
        return q;
    }

    public Query buildAdd(Book book) {
        Query q = new Query();
        q.setInsertInto(true);
        q.setFrom("buecher ");
        q.setInsertIntoContent("Title, Author, Publisher, Rating, Subareas) VALUES (" + book.toSqlQuery(book) + ");");
        return q;
    }

    public String createSql(Query q) {
        StringBuilder sb = new StringBuilder();

        if (q.isInsertInto()) {
            sb.append(q.getInsertIntoContent());
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