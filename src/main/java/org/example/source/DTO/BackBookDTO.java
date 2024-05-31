package org.example.source.DTO;

import java.sql.Timestamp;

public class BackBookDTO {
    private int borrowId;
    private String name;
    private int bookId;
    private String bookName;
    private Timestamp dateBack;

    // Constructors
    public BackBookDTO() {
        // Default constructor
    }

    public BackBookDTO(int borrowId, String name, int bookId, String bookName, Timestamp dateBack) {
        this.borrowId = borrowId;
        this.name = name;
        this.bookId = bookId;
        this.bookName = bookName;
        this.dateBack = dateBack;
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
}
