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
        
        Reader reader = readerDAO.readAll().stream()
                .filter(r -> r.getMaDocGia().equals(maDocGia))
                .findFirst()
                .orElse(null);

        if (reader == null) {
            return "Không tìm thấy mã độc giả này!";
        }

        
        if (reader.getNgayHetHan().isBefore(LocalDate.now())) {
            return "Thẻ độc giả đã hết hạn, không thể mượn sách!";
        }

        if (isbns.isEmpty()) {
            return "Vui lòng chọn ít nhất một quyển sách để mượn!";
        }

        
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

        
        String maPhieu = Constants.PREFIX_BORROW_SLIP + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        BorrowSlip slip = new BorrowSlip(maPhieu, maDocGia, LocalDate.now(), isbns);
        borrowSlipDAO.add(slip);

        
        for (String isbn : isbns) {
            Book book = allBooks.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().get();
            book.setSoLuong(book.getSoLuong() - 1);
            bookDAO.update(book);
        }

        return null; 
    }

    public String returnBooks(String maPhieu) {
        BorrowSlip slip = borrowSlipDAO.readAll().stream()
                .filter(s -> s.getMaPhieu().equals(maPhieu))
                .findFirst()
                .orElse(null);

        if (slip == null) return "Không tìm thấy mã phiếu mượn!";
        if (slip.getTrangThai() != BorrowSlip.TrangThai.DANG_MUON) return "Phiếu này đã được xử lý rồi!";

        
        slip.setNgayTraThucTe(LocalDate.now());
        slip.setTrangThai(BorrowSlip.TrangThai.DA_TRA);
        
        double tienPhat = slip.tinhTienPhatQuaHan();
        borrowSlipDAO.update(slip);

        
        List<Book> allBooks = bookDAO.readAll();
        for (String isbn : slip.getDanhSachISBN()) {
            allBooks.stream()
                    .filter(b -> b.getIsbn().equals(isbn))
                    .findFirst()
                    .ifPresent(book -> {
                        book.setSoLuong(book.getSoLuong() + 1);
                        bookDAO.update(book);
                    });
        }

        if (tienPhat > 0) {
            return "Trả sách thành công! Độc giả bị phạt " + String.format("%,.0f", tienPhat) + " VNĐ do quá hạn.";
        }
        return "Trả sách thành công!";
    }

    public String reportLost(String maPhieu) {
        BorrowSlip slip = borrowSlipDAO.readAll().stream()
                .filter(s -> s.getMaPhieu().equals(maPhieu))
                .findFirst()
                .orElse(null);

        if (slip == null) return "Không tìm thấy mã phiếu mượn!";
        if (slip.getTrangThai() != BorrowSlip.TrangThai.DANG_MUON) return "Phiếu này đã được xử lý rồi!";

        
        double tongTienPhat = 0;
        List<Book> allBooks = bookDAO.readAll();
        for (String isbn : slip.getDanhSachISBN()) {
            Book book = allBooks.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElse(null);
            if (book != null) {
                tongTienPhat += book.getGiaSach() * Constants.TY_LE_PHAT_MAT_SACH;
            }
        }

        
        slip.setTrangThai(BorrowSlip.TrangThai.MAT_SACH);
        slip.setNgayTraThucTe(LocalDate.now());
        borrowSlipDAO.update(slip);

        return "Báo mất thành công! Độc giả phải bồi thường " + String.format("%,.0f", tongTienPhat) + " VNĐ (200% giá trị sách).";
    }

    public String getNextSlipId() {
        return Constants.PREFIX_BORROW_SLIP + String.format("%04d", borrowSlipDAO.readAll().size() + 1);
    }
}
