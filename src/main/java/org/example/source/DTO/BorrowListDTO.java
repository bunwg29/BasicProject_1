package org.example.source.DTO;

import java.sql.Timestamp;

public class BorrowListDTO {
    private int idBorrow;
    private String idUser;
    private int bookId;
    private Timestamp dateBorrow;
    private String userName;
    private String bookName;
    private Timestamp dayEx;
    private int overduetax;

    public Timestamp getDayEx() {
        return dayEx;
    }
    public void setDayEx(Timestamp dayEx) {
        this.dayEx = dayEx;
    }

    public int getIdBorrow() {
        return idBorrow;
    }

    public void setIdBorrow(int idBorrow) {
        this.idBorrow = idBorrow;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Timestamp getDateBorrow() {
        return dateBorrow;
    }

    public void setDateBorrow(Timestamp dateBorrow) {
        this.dateBorrow = dateBorrow;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getOverduetax() {
        return overduetax;
    }

    public void setOverduetax(int overduetax) {
        this.overduetax = overduetax;
    }
}
