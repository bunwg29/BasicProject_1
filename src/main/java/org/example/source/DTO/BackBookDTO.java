package org.example.source.DTO;

import java.sql.Timestamp;

public class BackBookDTO {
    private int borrowId;
    private String name;
    private int bookId;
    private String bookName;
    private Timestamp dateBack;
    private int taxes_Late;
    // Constructors
    public BackBookDTO() {
        // Default constructor
    }

    // Getters and Setters

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Timestamp getDateBack() {
        return dateBack;
    }

    public void setDateBack(Timestamp dateBack) {
        this.dateBack = dateBack;
    }

    public int getTaxes_Late() {
        return taxes_Late;
    }

    public void setTaxes_Late(int taxes_Late) {
        this.taxes_Late = taxes_Late;
    }
}
