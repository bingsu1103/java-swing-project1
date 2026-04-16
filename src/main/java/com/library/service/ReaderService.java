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

    public String addReader(Reader reader) {
        String error = validateReader(reader);
        if (error != null) return error;

        reader.setMaDocGia(readerDAO.generateId());
        readerDAO.add(reader);
        return null;
    }

    public String updateReader(Reader reader) {
        String error = validateReader(reader);
        if (error != null) return error;

        readerDAO.update(reader);
        return null;
    }

    public void deleteReader(String maDocGia) {
        readerDAO.delete(maDocGia);
    }

    // Tính năng: Tìm kiếm độc giả theo CMND/CCCD
    public List<Reader> searchByCmnd(String cmnd) {
        return readerDAO.findByCmnd(cmnd);
    }

    // Tính năng: Tìm kiếm độc giả theo họ tên
    public List<Reader> searchByName(String name) {
        return readerDAO.findByName(name);
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
