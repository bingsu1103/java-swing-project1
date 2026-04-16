package com.library.ui.panel;

import com.library.model.Book;
import com.library.service.BookService;
import com.library.ui.component.StyledButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookPanel extends JPanel {
    private final BookService bookService;
    private JTable table;
    private DefaultTableModel tableModel;

    private final Color BACKGROUND_COLOR = new Color(30, 39, 46);
    private final Color SECONDARY_COLOR = new Color(47, 53, 66);
    private final Color TEXT_COLOR = Color.WHITE;

    public BookPanel() {
        this.bookService = new BookService();
        initUI();
        refreshTable();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel titleLabel = new JLabel("QUẢN LÝ SÁCH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        add(titleLabel, BorderLayout.NORTH);

        // Bảng dữ liệu
        tableModel = new DefaultTableModel(new Object[]{
                "ISBN", "Tên sách", "Tác giả", "Thể loại", "Năm XB", "Giá", "Số lượng"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setBackground(SECONDARY_COLOR);
        table.setForeground(TEXT_COLOR);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshTable() {
        List<Book> list = bookService.getAllBooks();
        tableModel.setRowCount(0);
        for (Book b : list) {
            tableModel.addRow(new Object[]{
                    b.getIsbn(), b.getTenSach(), b.getTacGia(),
                    b.getTheLoai(), b.getNamXB(), b.getGiaSach(), b.getSoLuong()
            });
        }
    }
}
