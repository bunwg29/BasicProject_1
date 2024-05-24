package org.example.source.DAO;

import javafx.collections.ObservableList;
import org.example.source.DTO.BorrowListDTO;
import org.example.source.DTO.BackBookDTO;
import org.example.source.model.borrowModel;

import java.sql.Timestamp;
import java.util.List;

public interface borrowDAO {
    public int insertBookBorrow(String iduser, int bookId);

    public ObservableList<borrowModel> listBorrow(String iduser);
    public void backBook(String name, int bookId, String bookName);
    public String findNameBook(int bookId);
    public int checkBookQuantity(int bookId);

    public boolean checkRequestBack(int bookId);

    public List<BorrowListDTO> getBorrowListData();

    public void insertToBorrowListTotal(int idborrow, String usernameLogin, int bookId, Timestamp borrowDate, String username, String bookName);
    public ObservableList<BackBookDTO> getBackBookData();

    public void inserttoBackListTotal(int idborrow, String usernameLogin, int bookId, String bookName);
}
