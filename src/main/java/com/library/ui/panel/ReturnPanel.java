package com.library.ui.panel;

import com.library.model.BorrowSlip;
import com.library.service.BorrowSlipService;
import com.library.ui.component.StyledButton;
import com.library.util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ReturnPanel extends JPanel {
    private final BorrowSlipService borrowSlipService;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearchReader;

    private final Color BACKGROUND_COLOR = new Color(30, 39, 46);
    private final Color SECONDARY_COLOR = new Color(47, 53, 66);
    private final Color TEXT_COLOR = Color.WHITE;

    public ReturnPanel() {
        this.borrowSlipService = new BorrowSlipService();
        initUI();
        refreshTable();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("QUẢN LÝ TRẢ SÁCH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.add(new JLabel("Mã ĐG:") {{ setForeground(TEXT_COLOR); }});
        txtSearchReader = new JTextField(15);
        txtSearchReader.setBackground(SECONDARY_COLOR);
        txtSearchReader.setForeground(TEXT_COLOR);
        
        StyledButton btnSearch = new StyledButton("Tìm phiếu", new Color(45, 52, 54), Color.WHITE);
        btnSearch.addActionListener(e -> handleSearch());
        
        searchPanel.add(txtSearchReader);
        searchPanel.add(btnSearch);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Content: table
        tableModel = new DefaultTableModel(new Object[]{
                "Mã phiếu", "Mã ĐG", "Ngày mượn", "Hạn trả", "Số lượng sách", "Trạng thái"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setBackground(SECONDARY_COLOR);
        table.setForeground(TEXT_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(scrollPane, BorderLayout.CENTER);

        // Footer: Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(BACKGROUND_COLOR);

        StyledButton btnReturn = new StyledButton("TRẢ SÁCH", new Color(11, 232, 129), Color.DARK_GRAY);
        StyledButton btnLost = new StyledButton("BÁO MẤT SÁCH", new Color(255, 71, 87), Color.WHITE);
        StyledButton btnRefresh = new StyledButton("Làm mới");

        btnReturn.addActionListener(e -> handleReturn());
        btnLost.addActionListener(e -> handleLost());
        btnRefresh.addActionListener(e -> refreshTable());

        actionPanel.add(btnReturn);
        actionPanel.add(btnLost);
        actionPanel.add(btnRefresh);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void handleReturn() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu mượn để trả!");
            return;
        }

        String maPhieu = (String) table.getValueAt(row, 0);
        int choice = JOptionPane.showConfirmDialog(this, "Xác nhận trả sách cho phiếu " + maPhieu + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            String result = borrowSlipService.returnBooks(maPhieu);
            JOptionPane.showMessageDialog(this, result, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }

    private void handleLost() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu mượn để báo mất!");
            return;
        }

        String maPhieu = (String) table.getValueAt(row, 0);
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn báo mất sách cho phiếu " + maPhieu + "?\nĐộc giả sẽ bị phạt 200% giá trị sách.", "Cảnh báo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            String result = borrowSlipService.reportLost(maPhieu);
            JOptionPane.showMessageDialog(this, result, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }

    private void handleSearch() {
        String readerId = txtSearchReader.getText().trim();
        List<BorrowSlip> list = borrowSlipService.getAllSlips().stream()
                .filter(s -> s.getTrangThai() == BorrowSlip.TrangThai.DANG_MUON)
                .filter(s -> readerId.isEmpty() || s.getMaDocGia().equalsIgnoreCase(readerId))
                .collect(Collectors.toList());
        updateTableData(list);
    }

    private void updateTableData(List<BorrowSlip> list) {
        tableModel.setRowCount(0);
        for (BorrowSlip s : list) {
            tableModel.addRow(new Object[]{
                    s.getMaPhieu(), s.getMaDocGia(),
                    DateUtil.formatDate(s.getNgayMuon()),
                    DateUtil.formatDate(s.getNgayTraDuKien()),
                    s.getDanhSachISBN().size(),
                    s.getTrangThai().name()
            });
        }
    }

    public void refreshTable() {
        List<BorrowSlip> list = borrowSlipService.getAllSlips().stream()
                .filter(s -> s.getTrangThai() == BorrowSlip.TrangThai.DANG_MUON)
                .collect(Collectors.toList());
        updateTableData(list);
    }
}
