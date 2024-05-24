package org.example.source.DAO;

import javafx.collections.ObservableList;
import org.example.source.DTO.BorrowListDTO;
import org.example.source.DTO.BackBookDTO;
import org.example.source.model.borrowModel;

import java.sql.Timestamp;
import java.util.List;
// This class use for handle data of book between actions: borrow, back, accept borrow, accept back, check...
public interface borrowDAO {
    // Method use for insert data of user and book to borrow list
    public int insertBookBorrow(String iduser, int bookId);

    // Method use for handle display data of borrow list on table
    public ObservableList<borrowModel> listBorrow(String iduser);

    //Method use for add data of user and book to return book list
    public void backBook(String name, int bookId, String bookName);

    // Method use for find name of book support for other method
    public String findNameBook(int bookId);

    // Method use for check quantity of book and consider to borrow
    public int checkBookQuantity(int bookId);

    /*
       Method use for check request of user when user want to back book if user have already sent request
       it will be wrong
    */
    public boolean checkRequestBack(int bookId);

    // Method use for get borrow list data of user
    public List<BorrowListDTO> getBorrowListData();

    // Method use for add list of borrow book of user when finish process borrow to admin check data
    public void insertToBorrowListTotal(int idborrow, String usernameLogin, int bookId, Timestamp borrowDate, String username, String bookName);

    // Method use for display data of return book list of user
    public ObservableList<BackBookDTO> getBackBookData();

    // Method use for insert return book list when user finish process return book
    public void inserttoBackListTotal(int idborrow, String usernameLogin, int bookId, String bookName);
}
