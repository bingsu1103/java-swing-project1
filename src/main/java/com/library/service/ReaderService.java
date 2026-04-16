package com.library.service;

import com.library.dao.ReaderDAO;
import com.library.model.Reader;
import com.library.util.Validator;
import java.util.List;

public class ReaderService {
    private final ReaderDAO readerDAO;

    public ReaderService() {
        this.readerDAO = new ReaderDAO();
    }

    public List<Reader> getAllReaders() {
        return readerDAO.readAll();
    }

    // Tính năng: Thêm độc giả
    public String addReader(Reader reader) {
        String error = validateReader(reader);
        if (error != null) return error;

        reader.setMaDocGia(readerDAO.generateId());
        readerDAO.add(reader);
        return null; // Thành công
    }

    private String validateReader(Reader reader) {
        if (!Validator.isNotEmpty(reader.getHoTen())) {
            return "Họ tên không được để trống";
        }
        if (!Validator.isValidCMND(reader.getCmnd())) {
            return "CMND/CCCD phải là 9 hoặc 12 chữ số";
        }
        if (!Validator.isValidEmail(reader.getEmail())) {
            return "Email không đúng định dạng";
        }
        if (reader.getNgaySinh() == null) {
            return "Ngày sinh không hợp lệ";
        }
        return null;
    }
}
