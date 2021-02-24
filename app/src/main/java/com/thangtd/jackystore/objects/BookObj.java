package com.thangtd.jackystore.objects;

/**
 * Created by thangtd2016 on 02/03/2018.
 */

public class BookObj {
    public String isbn;
    public String title;
    public String author;
    public String translator;
    public String publisher;
    public Integer pages;
    public String released_date;
    public String added_date;
    public String purchased_date;
    public String finished_date;
    public String location;
    public String note;
    public Integer kind;
    public byte[] cover;

    public BookObj(String isbn, String title, String author, String translator, String publisher, Integer pages,
                   String released_date, String added_date, String purchased_date, String finished_date,
                   String location, String note, Integer kind, byte[] cover) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.translator = translator;
        this.publisher = publisher;
        this.pages = pages;
        this.released_date = released_date;
        this.added_date = added_date;
        this.purchased_date = purchased_date;
        this.finished_date = finished_date;
        this.location = location;
        this.note = note;
        this.kind = kind;
        this.cover = cover;
    }

    public BookObj(String finished_date) {
        this.finished_date = finished_date;
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

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getReleased_date() {
        return released_date;
    }

    public void setReleased_date(String released_date) {
        this.released_date = released_date;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getPurchased_date() {
        return purchased_date;
    }

    public void setPurchased_date(String purchased_date) {
        this.purchased_date = purchased_date;
    }

    public String getFinished_date() {
        return finished_date;
    }

    public void setFinished_date(String finished_date) {
        this.finished_date = finished_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }
}
