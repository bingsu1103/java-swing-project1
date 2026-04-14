package com.library.model;

public class Book {
    private String isbn;
    private String tenSach;
    private String tacGia;
    private String nxb;
    private int namXB;
    private String theLoai;
    private double giaSach;
    private int soLuong;

    public Book() {
    }

    public Book(String isbn, String tenSach, String tacGia, String nxb,
                int namXB, String theLoai, double giaSach, int soLuong) {
        this.isbn = isbn;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.nxb = nxb;
        this.namXB = namXB;
        this.theLoai = theLoai;
        this.giaSach = giaSach;
        this.soLuong = soLuong;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getNxb() {
        return nxb;
    }

    public void setNxb(String nxb) {
        this.nxb = nxb;
    }

    public int getNamXB() {
        return namXB;
    }

    public void setNamXB(int namXB) {
        this.namXB = namXB;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public double getGiaSach() {
        return giaSach;
    }

    public void setGiaSach(double giaSach) {
        this.giaSach = giaSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public boolean isCoSan() {
        return soLuong > 0;
    }

    public String toDataString() {
        return String.join("|",
                isbn, tenSach, tacGia, nxb,
                String.valueOf(namXB), theLoai,
                String.valueOf(giaSach), String.valueOf(soLuong));
    }

    public static Book fromDataString(String dataString) {
        String[] parts = dataString.split("\\|");
        if (parts.length < 8)
            return null;

        Book book = new Book();
        book.isbn = parts[0].trim();
        book.tenSach = parts[1].trim();
        book.tacGia = parts[2].trim();
        book.nxb = parts[3].trim();
        book.namXB = Integer.parseInt(parts[4].trim());
        book.theLoai = parts[5].trim();
        book.giaSach = Double.parseDouble(parts[6].trim());
        book.soLuong = Integer.parseInt(parts[7].trim());
        return book;
    }

    @Override
    public String toString() {
        return "Book{isbn='" + isbn + "', tenSach='" + tenSach + "', tacGia='" + tacGia + "'}";
    }
}
