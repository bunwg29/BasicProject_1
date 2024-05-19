package org.example.source.Sever_Client;
import org.example.source.DAO.bookDataDAO;
public class databaseThread implements Runnable{

    @Override
    public void run() {
        bookDataDAO bookDataDAO = new bookDataDAO();
        bookDataDAO.getBookData();
    }
}
