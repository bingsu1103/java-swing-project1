package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;
import java.util.List;

public class BookService {
    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    // Tính năng: Xem danh sách sách
    public List<Book> getAllBooks() {
        return bookDAO.readAll();
    }
}
