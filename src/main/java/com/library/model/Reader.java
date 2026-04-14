package com.library.model;

import java.time.LocalDate;
import com.library.util.DateUtil;

public class Reader {
    private String maDocGia;
    private String hoTen;
    private String cmnd;
    private LocalDate ngaySinh;
    private String gioiTinh;
    private String email;
    private String diaChi;
    private LocalDate ngayLapThe;
    private LocalDate ngayHetHan;

    public Reader() {
    }

    public Reader(String maDocGia, String hoTen, String cmnd, LocalDate ngaySinh,
                  String gioiTinh, String email, String diaChi, LocalDate ngayLapThe) {
        this.maDocGia = maDocGia;
        this.hoTen = hoTen;
        this.cmnd = cmnd;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.email = email;
        this.diaChi = diaChi;
        this.ngayLapThe = ngayLapThe;
        this.ngayHetHan = ngayLapThe.plusMonths(48);
    }

    public String getMaDocGia() {
        return maDocGia;
    }

    public void setMaDocGia(String maDocGia) {
        this.maDocGia = maDocGia;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public LocalDate getNgayLapThe() {
        return ngayLapThe;
    }

    public void setNgayLapThe(LocalDate ngayLapThe) {
        this.ngayLapThe = ngayLapThe;
        this.ngayHetHan = ngayLapThe.plusMonths(48);
    }

    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(LocalDate ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public boolean isConHan() {
        return LocalDate.now().isBefore(ngayHetHan) || LocalDate.now().isEqual(ngayHetHan);
    }

    public String toDataString() {
        return String.join("|",
                maDocGia, hoTen, cmnd,
                DateUtil.formatDate(ngaySinh),
                gioiTinh, email, diaChi,
                DateUtil.formatDate(ngayLapThe),
                DateUtil.formatDate(ngayHetHan));
    }

    public static Reader fromDataString(String dataString) {
        String[] parts = dataString.split("\\|");
        if (parts.length < 9)
            return null;

        Reader reader = new Reader();
        reader.maDocGia = parts[0].trim();
        reader.hoTen = parts[1].trim();
        reader.cmnd = parts[2].trim();
        reader.ngaySinh = DateUtil.parseDate(parts[3].trim());
        reader.gioiTinh = parts[4].trim();
        reader.email = parts[5].trim();
        reader.diaChi = parts[6].trim();
        reader.ngayLapThe = DateUtil.parseDate(parts[7].trim());
        reader.ngayHetHan = DateUtil.parseDate(parts[8].trim());
        return reader;
    }

    @Override
    public String toString() {
        return "Reader{maDocGia='" + maDocGia + "', hoTen='" + hoTen + "', cmnd='" + cmnd + "'}";
    }
}
