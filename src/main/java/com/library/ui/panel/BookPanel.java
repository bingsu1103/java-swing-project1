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

        // Action section
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(BACKGROUND_COLOR);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        StyledButton btnAdd = new StyledButton("Thêm mới", new Color(11, 232, 129), Color.DARK_GRAY);
        StyledButton btnEdit = new StyledButton("Chỉnh sửa", new Color(0, 122, 255), Color.WHITE);
        StyledButton btnDelete = new StyledButton("Xóa", new Color(255, 71, 87), Color.WHITE);
        StyledButton btnRefresh = new StyledButton("Làm mới");

        btnAdd.addActionListener(e -> handleAdd());
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());
        btnRefresh.addActionListener(e -> refreshTable());

        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        actionPanel.add(btnRefresh);

        add(actionPanel, BorderLayout.SOUTH);
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

    private void handleAdd() {
        String autoIsbn = bookService.generateNextIsbn();
        BookForm form = new BookForm(null, autoIsbn);
        com.library.ui.component.FormDialog dialog = new com.library.ui.component.FormDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Sách mới", form);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String error = bookService.addBook(form.getBook());
            if (error != null) JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            else refreshTable();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một quyển sách để sửa!");
            return;
        }
        String isbn = (String) table.getValueAt(row, 0);
        Book book = bookService.getAllBooks().stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElse(null);
        
        if (book != null) {
            BookForm form = new BookForm(book, isbn);
            com.library.ui.component.FormDialog dialog = new com.library.ui.component.FormDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa thông tin Sách", form);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                String error = bookService.updateBook(form.getBook());
                if (error != null) JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                else refreshTable();
            }
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sách này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            bookService.deleteBook((String) table.getValueAt(row, 0));
            refreshTable();
        }
    }

    private static class BookForm extends JPanel {
        private final JTextField txtIsbn, txtTen, txtTacGia, txtNhaXB, txtNamXB, txtTheLoai, txtGia, txtSoLuong;
        private Book currentBook;

        public BookForm(Book book, String isbn) {
            this.currentBook = book;
            setLayout(new GridLayout(0, 2, 10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(new JLabel("Mã ISBN:")); 
            txtIsbn = new JTextField(isbn); 
            txtIsbn.setEditable(false); // Luôn luôn không cho sửa ISBN
            txtIsbn.setBackground(new Color(47, 53, 66));
            txtIsbn.setForeground(Color.LIGHT_GRAY);
            add(txtIsbn);

            add(new JLabel("Tên sách:")); txtTen = new JTextField(); add(txtTen);
            add(new JLabel("Tác giả:")); txtTacGia = new JTextField(); add(txtTacGia);
            add(new JLabel("Nhà xuất bản:")); txtNhaXB = new JTextField(); add(txtNhaXB);
            add(new JLabel("Năm xuất bản:")); txtNamXB = new JTextField(); add(txtNamXB);
            add(new JLabel("Thể loại:")); txtTheLoai = new JTextField(); add(txtTheLoai);
            add(new JLabel("Giá sách:")); txtGia = new JTextField(); add(txtGia);
            add(new JLabel("Số lượng quyển:")); txtSoLuong = new JTextField(); add(txtSoLuong);

            if (book != null) {
                txtTen.setText(book.getTenSach());
                txtTacGia.setText(book.getTacGia());
                txtNhaXB.setText(book.getNxb());
                txtNamXB.setText(String.valueOf(book.getNamXB()));
                txtTheLoai.setText(book.getTheLoai());
                txtGia.setText(String.valueOf(book.getGiaSach()));
                txtSoLuong.setText(String.valueOf(book.getSoLuong()));
            }
        }

        public Book getBook() {
            Book b = currentBook != null ? currentBook : new Book();
            b.setIsbn(txtIsbn.getText().trim());
            b.setTenSach(txtTen.getText().trim());
            b.setTacGia(txtTacGia.getText().trim());
            b.setNxb(txtNhaXB.getText().trim());
            try {
                b.setNamXB(Integer.parseInt(txtNamXB.getText().trim()));
                b.setGiaSach(Double.parseDouble(txtGia.getText().trim()));
                b.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
            } catch (NumberFormatException e) {
                // Sẽ được validate ở Service
            }
            b.setTheLoai(txtTheLoai.getText().trim());
            return b;
        }
    }
}
