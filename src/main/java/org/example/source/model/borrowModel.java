package org.example.source.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;


public class borrowModel {
    private int idborrow;
    private String iduser;
    private String bookId;
    private Timestamp databorrow;
    private Timestamp dateExpiration;

    public borrowModel(int idborrow, String iduser, String bookId, Timestamp databorrow, Timestamp dateExpiration) {
        this.idborrow = idborrow;
        this.iduser = iduser;
        this.bookId = bookId;
        this.databorrow = databorrow;
        this.dateExpiration = dateExpiration;
    }

    public borrowModel() {}
    public borrowModel(int idborrow, String iduser, String bookId, Timestamp databorrow) {
        this.idborrow = idborrow;
        this.iduser = iduser;
        this.bookId = bookId;
        this.databorrow = databorrow;
    }



    public int getIdborrow() {
        return idborrow;
    }

    public void setIdborrow(int idborrow) {
        this.idborrow = idborrow;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Timestamp getDataborrow() {
        return databorrow;
    }

    public void setDataborrow(Timestamp databorrow) {
        this.databorrow = databorrow;
    }

    public Date setDataborrow() {
        return new Date(System.currentTimeMillis());
    }

}