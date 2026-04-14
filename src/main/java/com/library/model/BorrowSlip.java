package com.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.library.util.DateUtil;

public class BorrowSlip {

    public enum TrangThai {
        DANG_MUON,
        DA_TRA,
        MAT_SACH
    }

    private String maPhieu;
    private String maDocGia;
    private LocalDate ngayMuon;
    private LocalDate ngayTraDuKien;
    private LocalDate ngayTraThucTe;
    private List<String> danhSachISBN;
    private TrangThai trangThai;

    public BorrowSlip() {
        this.danhSachISBN = new ArrayList<>();
    }

    public BorrowSlip(String maPhieu, String maDocGia, LocalDate ngayMuon, List<String> danhSachISBN) {
        this.maPhieu = maPhieu;
        this.maDocGia = maDocGia;
        this.ngayMuon = ngayMuon;
        this.ngayTraDuKien = ngayMuon.plusDays(7);
        this.ngayTraThucTe = null;
        this.danhSachISBN = new ArrayList<>(danhSachISBN);
        this.trangThai = TrangThai.DANG_MUON;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaDocGia() {
        return maDocGia;
    }

    public void setMaDocGia(String maDocGia) {
        this.maDocGia = maDocGia;
    }

    public LocalDate getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(LocalDate ngayMuon) {
        this.ngayMuon = ngayMuon;
        this.ngayTraDuKien = ngayMuon.plusDays(7);
    }

    public LocalDate getNgayTraDuKien() {
        return ngayTraDuKien;
    }

    public void setNgayTraDuKien(LocalDate ngayTraDuKien) {
        this.ngayTraDuKien = ngayTraDuKien;
    }

    public LocalDate getNgayTraThucTe() {
        return ngayTraThucTe;
    }

    public void setNgayTraThucTe(LocalDate ngayTraThucTe) {
        this.ngayTraThucTe = ngayTraThucTe;
    }

    public List<String> getDanhSachISBN() {
        return danhSachISBN;
    }

    public void setDanhSachISBN(List<String> danhSachISBN) {
        this.danhSachISBN = danhSachISBN;
    }

    public TrangThai getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThai trangThai) {
        this.trangThai = trangThai;
    }

    public boolean isQuaHan() {
        if (trangThai == TrangThai.DA_TRA && ngayTraThucTe != null) {
            return ngayTraThucTe.isAfter(ngayTraDuKien);
        }
        if (trangThai == TrangThai.DANG_MUON) {
            return LocalDate.now().isAfter(ngayTraDuKien);
        }
        return false;
    }

    public double tinhTienPhatQuaHan() {
        if (!isQuaHan())
            return 0;

        LocalDate ngayTra = (ngayTraThucTe != null) ? ngayTraThucTe : LocalDate.now();
        long soNgayTre = ChronoUnit.DAYS.between(ngayTraDuKien, ngayTra);
        return soNgayTre * 5000.0;
    }

    public String toDataString() {
        String ngayTraStr = (ngayTraThucTe != null) ? DateUtil.formatDate(ngayTraThucTe) : "null";
        String isbnStr = String.join(",", danhSachISBN);

        return String.join("|",
                maPhieu, maDocGia,
                DateUtil.formatDate(ngayMuon),
                DateUtil.formatDate(ngayTraDuKien),
                ngayTraStr,
                isbnStr,
                trangThai.name());
    }

    public static BorrowSlip fromDataString(String dataString) {
        String[] parts = dataString.split("\\|");
        if (parts.length < 7)
            return null;

        BorrowSlip slip = new BorrowSlip();
        slip.maPhieu = parts[0].trim();
        slip.maDocGia = parts[1].trim();
        slip.ngayMuon = DateUtil.parseDate(parts[2].trim());
        slip.ngayTraDuKien = DateUtil.parseDate(parts[3].trim());
        slip.ngayTraThucTe = parts[4].trim().equals("null") ? null : DateUtil.parseDate(parts[4].trim());

        String isbnStr = parts[5].trim();
        if (!isbnStr.isEmpty()) {
            slip.danhSachISBN = new ArrayList<>(Arrays.asList(isbnStr.split(",")));
        } else {
            slip.danhSachISBN = new ArrayList<>();
        }

        slip.trangThai = TrangThai.valueOf(parts[6].trim());
        return slip;
    }

    @Override
    public String toString() {
        return "BorrowSlip{maPhieu='" + maPhieu + "', maDocGia='" + maDocGia +
                "', trangThai=" + trangThai + ", soSach=" + danhSachISBN.size() + "}";
    }
}
