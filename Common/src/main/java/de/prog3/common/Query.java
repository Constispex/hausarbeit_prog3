package de.prog3.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Query {
    @JsonProperty("select")
    String select;
    @JsonProperty("delete")
    boolean delete;
    @JsonProperty("update")
    boolean update;
    @JsonProperty("updateContent")
    String updateContent;
    @JsonProperty("insertInto")
    boolean insertInto;
    @JsonProperty("insertIntoContent")
    String insertIntoContent;
    @JsonProperty("from")
    String from;
    @JsonProperty("where")
    String where;
    @JsonProperty("orderBy")
    String orderBy;

    public String getInsertIntoContent() {
        return insertIntoContent;
    }

    public void setInsertIntoContent(String insertIntoContent) {
        this.insertIntoContent = insertIntoContent;
    }

    public boolean isInsertInto() {
        return insertInto;
    }

    public void setInsertInto(boolean insertInto) {
        this.insertInto = insertInto;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "Query{" +
                "select='" + select + '\'' +
                ", delete=" + delete +
                ", update=" + update +
                ", updateContent='" + updateContent + '\'' +
                ", insertInto=" + insertInto +
                ", insertIntoContent='" + insertIntoContent + '\'' +
                ", from='" + from + '\'' +
                ", where='" + where + '\'' +
                ", orderBy='" + orderBy + '\'' +
                '}';
    }
}
