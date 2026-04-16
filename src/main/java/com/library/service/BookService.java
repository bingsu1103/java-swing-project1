package com.library.service;

import com.library.dao.BookDAO;
import com.library.model.Book;
import com.library.util.Validator;
import java.util.List;

public class BookService {
    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    public List<Book> getAllBooks() {
        return bookDAO.readAll();
    }

    // Tính năng: Thêm sách mới
    public String addBook(Book book) {
        String error = validateBook(book);
        if (error != null) return error;

        // Kiểm tra xem ISBN đã tồn tại chưa
        if (bookDAO.readAll().stream().anyMatch(b -> b.getIsbn().equals(book.getIsbn()))) {
            return "Mã ISBN này đã tồn tại trong hệ thống";
        }

        bookDAO.add(book);
        return null;
    }

    private String validateBook(Book book) {
        if (!Validator.isNotEmpty(book.getTenSach())) return "Tên sách không được để trống";
        if (!Validator.isValidISBN(book.getIsbn())) return "ISBN không đúng định dạng";
        if (book.getGiaSach() <= 0) return "Giá sách phải lớn hơn 0";
        if (book.getSoLuong() < 0) return "Số lượng không được âm";
        return null;
    }
}
