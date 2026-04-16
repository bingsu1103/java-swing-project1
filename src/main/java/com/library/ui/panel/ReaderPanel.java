package com.library.ui.panel;

import com.library.model.Reader;
import com.library.service.ReaderService;
import com.library.util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReaderPanel extends JPanel {
    private final ReaderService readerService;
    private JTable table;
    private DefaultTableModel tableModel;

    public ReaderPanel() {
        this.readerService = new ReaderService();
        initUI();
        refreshTable();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tiêu đề
        JLabel titleLabel = new JLabel("QUẢN LÝ ĐỘC GIẢ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Bảng dữ liệu
        tableModel = new DefaultTableModel(new Object[]{
                "Mã ĐG", "Họ tên", "CMND", "Ngày sinh", "Giới tính", "Email", "Địa chỉ", "Hết hạn"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshTable() {
        List<Reader> list = readerService.getAllReaders();
        tableModel.setRowCount(0);
        for (Reader r : list) {
            tableModel.addRow(new Object[]{
                    r.getMaDocGia(), r.getHoTen(), r.getCmnd(),
                    DateUtil.formatDate(r.getNgaySinh()),
                    r.getGioiTinh(), r.getEmail(), r.getDiaChi(),
                    DateUtil.formatDate(r.getNgayHetHan())
            });
        }
    }
}
