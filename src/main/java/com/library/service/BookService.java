package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.util.Validator;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    public List<Book> getAllBooks() {
        return bookDAO.readAll();
    }

    public String addBook(Book book) {
        String error = validateBook(book);
        if (error != null) return error;

        if (bookDAO.readAll().stream().anyMatch(b -> b.getIsbn().equals(book.getIsbn()))) {
            return "Mã ISBN này đã tồn tại trong hệ thống";
        }

        bookDAO.add(book);
        return null;
    }

    public String updateBook(Book book) {
        String error = validateBook(book);
        if (error != null) return error;

        bookDAO.update(book);
        return null;
    }

    public void deleteBook(String isbn) {
        bookDAO.delete(isbn);
    }

    public String generateNextIsbn() {
        return "B" + (System.currentTimeMillis() % 100000L);
    }

    // Sửa lỗi: Gọi searchByIsbn để trả về danh sách List<Book>
    public List<Book> searchByIsbn(String isbn) {
        return bookDAO.searchByIsbn(isbn);
    }

    public List<Book> searchByName(String name) {
        return bookDAO.searchByName(name);
    }

    private String validateBook(Book book) {
        if (!Validator.isNotEmpty(book.getTenSach())) return "Tên sách không được để trống";
        if (!Validator.isValidISBN(book.getIsbn())) return "ISBN không đúng định dạng";
        if (book.getGiaSach() <= 0) return "Giá sách phải lớn hơn 0";
        if (book.getSoLuong() < 0) return "Số lượng không được âm";
        return null;
    }
}
