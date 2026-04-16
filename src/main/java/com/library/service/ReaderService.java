package com.library.service;

import com.library.dao.ReaderDAO;
import com.library.model.Reader;
import java.util.List;

public class ReaderService {
    private final ReaderDAO readerDAO;

    public ReaderService() {
        this.readerDAO = new ReaderDAO();
    }

    // Tính năng 3.1: Xem danh sách độc giả
    public List<Reader> getAllReaders() {
        return readerDAO.readAll();
    }
}
