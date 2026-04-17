package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.BorrowSlipDAO;
import com.library.dao.ReaderDAO;
import com.library.model.Book;
import com.library.model.BorrowSlip;
import com.library.model.Reader;
import com.library.util.Constants;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BorrowSlipService {
    private final BorrowSlipDAO borrowSlipDAO;
    private final ReaderDAO readerDAO;
    private final BookDAO bookDAO;

    public BorrowSlipService() {
        this.borrowSlipDAO = new BorrowSlipDAO();
        this.readerDAO = new ReaderDAO();
        this.bookDAO = new BookDAO();
    }

    public List<BorrowSlip> getAllSlips() {
        return borrowSlipDAO.readAll();
    }

    public String createBorrowSlip(String maDocGia, List<String> isbns) {
        // 1. Kiểm tra độc giả
        Reader reader = readerDAO.readAll().stream()
                .filter(r -> r.getMaDocGia().equals(maDocGia))
                .findFirst()
                .orElse(null);

        if (reader == null) {
            return "Không tìm thấy mã độc giả này!";
        }

        // 2. Kiểm tra hạn thẻ độc giả
        if (reader.getNgayHetHan().isBefore(LocalDate.now())) {
            return "Thẻ độc giả đã hết hạn, không thể mượn sách!";
        }

        if (isbns.isEmpty()) {
            return "Vui lòng chọn ít nhất một quyển sách để mượn!";
        }

        // 3. Kiểm tra sách
        List<Book> allBooks = bookDAO.readAll();
        for (String isbn : isbns) {
            Book book = allBooks.stream()
                    .filter(b -> b.getIsbn().equals(isbn))
                    .findFirst()
                    .orElse(null);
            
            if (book == null) {
                return "Không tìm thấy sách có mã ISBN: " + isbn;
            }
            if (book.getSoLuong() <= 0) {
                return "Sách '" + book.getTenSach() + "' đã hết trong kho!";
            }
        }

        // 4. Tạo phiếu mượn
        String maPhieu = Constants.PREFIX_BORROW_SLIP + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        BorrowSlip slip = new BorrowSlip(maPhieu, maDocGia, LocalDate.now(), isbns);
        borrowSlipDAO.add(slip);

        // 5. Cập nhật số lượng sách trong kho
        for (String isbn : isbns) {
            Book book = allBooks.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().get();
            book.setSoLuong(book.getSoLuong() - 1);
            bookDAO.update(book);
        }

        return null; // Thành công
    }

    public String getNextSlipId() {
        return Constants.PREFIX_BORROW_SLIP + String.format("%04d", borrowSlipDAO.readAll().size() + 1);
    }
}
