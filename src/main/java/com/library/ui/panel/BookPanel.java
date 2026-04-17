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
    private JTextField txtSearch;
    private JComboBox<String> cbSearchType;

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

        // Header section (Title + Search)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("QUẢN LÝ SÁCH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(BACKGROUND_COLOR);

        cbSearchType = new JComboBox<>(new String[]{"Tên sách", "Mã ISBN"});
        txtSearch = new JTextField(20);
        txtSearch.setBackground(SECONDARY_COLOR);
        txtSearch.setForeground(TEXT_COLOR);
        txtSearch.setCaretColor(TEXT_COLOR);

        StyledButton btnSearch = new StyledButton("Tìm kiếm", new Color(45, 52, 54), Color.WHITE);
        btnSearch.addActionListener(e -> handleSearch());

        searchPanel.add(cbSearchType);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

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

    private void handleSearch() {
        String query = txtSearch.getText().trim();
        List<Book> result;
        if (query.isEmpty()) {
            result = bookService.getAllBooks();
        } else if (cbSearchType.getSelectedIndex() == 0) {
            result = bookService.searchByName(query);
        } else {
            result = bookService.searchByIsbn(query);
        }
        updateTableData(result);
    }

    private void updateTableData(List<Book> list) {
        tableModel.setRowCount(0);
        for (Book b : list) {
            tableModel.addRow(new Object[]{
                    b.getIsbn(), b.getTenSach(), b.getTacGia(),
                    b.getTheLoai(), b.getNamXB(), b.getGiaSach(), b.getSoLuong()
            });
        }
    }

    public void refreshTable() {
        updateTableData(bookService.getAllBooks());
    }
}
