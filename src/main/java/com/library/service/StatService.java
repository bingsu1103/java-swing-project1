package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.BorrowSlipDAO;
import com.library.dao.ReaderDAO;
import com.library.model.Book;
import com.library.model.BorrowSlip;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatService {
    private final BookDAO bookDAO;
    private final ReaderDAO readerDAO;
    private final BorrowSlipDAO borrowSlipDAO;

    public StatService() {
        this.bookDAO = new BookDAO();
        this.readerDAO = new ReaderDAO();
        this.borrowSlipDAO = new BorrowSlipDAO();
    }

    public Map<String, Object> getOverviewStats() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Book> allBooks = bookDAO.readAll();
        List<BorrowSlip> allSlips = borrowSlipDAO.readAll();

        
        stats.put("totalReaders", readerDAO.readAll().size());

        
        stats.put("totalBookTypes", allBooks.size());
        stats.put("totalBookCopies", allBooks.stream().mapToInt(Book::getSoLuong).sum());

        
        long activeLoans = allSlips.stream()
                .filter(s -> s.getTrangThai() == BorrowSlip.TrangThai.DANG_MUON)
                .count();
        long totalReturns = allSlips.stream()
                .filter(s -> s.getTrangThai() == BorrowSlip.TrangThai.DA_TRA)
                .count();
        long lostBooks = allSlips.stream()
                .filter(s -> s.getTrangThai() == BorrowSlip.TrangThai.MAT_SACH)
                .count();
        
        stats.put("activeLoans", activeLoans);
        stats.put("totalReturns", totalReturns);
        stats.put("lostBooks", lostBooks);

        
        long overdueCount = allSlips.stream()
                .filter(s -> s.getTrangThai() == BorrowSlip.TrangThai.DANG_MUON)
                .filter(s -> s.getNgayTraDuKien().isBefore(LocalDate.now()))
                .count();
        stats.put("overdueCount", overdueCount);

        return stats;
    }
}
