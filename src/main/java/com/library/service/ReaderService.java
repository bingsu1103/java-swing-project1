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

    public String saveReader(Reader reader) {
        String error = validateReader(reader);
        if (error != null)
            return error;

        if (reader.getMaDocGia() == null || reader.getMaDocGia().isEmpty()) {
            reader.setMaDocGia(readerDAO.generateId());
            readerDAO.add(reader);
        } else {
            readerDAO.update(reader);
        }
        return null;
    }

    public void deleteReader(String maDocGia) {
        readerDAO.delete(maDocGia);
    }

    public List<Reader> searchByCmnd(String cmnd) {
        return readerDAO.findByCmnd(cmnd);
    }

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
        if (reader.getNgayLapThe() == null) {
            return "Ngày lập thẻ không hợp lệ";
        }
        return null;
    }
}
