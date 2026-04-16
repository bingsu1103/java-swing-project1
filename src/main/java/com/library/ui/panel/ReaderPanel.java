package com.library.ui.panel;

import com.library.model.Reader;
import com.library.service.ReaderService;
import com.library.util.DateUtil;
import com.library.ui.component.StyledButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReaderPanel extends JPanel {
    private final ReaderService readerService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cbSearchType;

    public ReaderPanel() {
        this.readerService = new ReaderService();
        initUI();
        refreshTable();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header section (Title + Search)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("QUẢN LÝ ĐỘC GIẢ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);

        cbSearchType = new JComboBox<>(new String[]{"Họ tên", "CMND/CCCD"});
        txtSearch = new JTextField(20);
        StyledButton btnSearch = new StyledButton("Tìm kiếm", new Color(45, 52, 54), Color.WHITE);
        btnSearch.addActionListener(e -> handleSearch());

        searchPanel.add(cbSearchType);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

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

    private void handleSearch() {
        String query = txtSearch.getText().trim();
        List<Reader> result;
        if (query.isEmpty()) {
            result = readerService.getAllReaders();
        } else if (cbSearchType.getSelectedIndex() == 0) {
            result = readerService.searchByName(query);
        } else {
            result = readerService.searchByCmnd(query);
        }
        updateTableData(result);
    }

    private void updateTableData(List<Reader> list) {
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

    public void refreshTable() {
        updateTableData(readerService.getAllReaders());
    }
}
