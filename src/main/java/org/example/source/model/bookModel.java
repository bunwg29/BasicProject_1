package org.example.source.model;

// class use for set data of book content each variable corresponding in table of database
public class bookModel {
    private String bookId;
    private String imageSrc;
    private String bookName;

    private String bookQuantity;
    public bookModel() {

    }
    public bookModel(String bookId, String imageSrc, String bookName, String bookQuantity) {
        this.bookId = bookId;
        this.imageSrc = imageSrc;
        this.bookName = bookName;
        this.bookQuantity = bookQuantity;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(String bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    @Override
    public String toString() {
        return "bookModel{" +
                "bookId='" + bookId + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookQuantity='" + bookQuantity + '\'' +
                '}';
    }
}
