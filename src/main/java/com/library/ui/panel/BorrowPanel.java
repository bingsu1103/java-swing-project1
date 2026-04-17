package com.library.ui.panel;

import com.library.service.BookService;
import com.library.service.BorrowSlipService;
import com.library.ui.component.StyledButton;
import com.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowPanel extends JPanel {
    private final BorrowSlipService borrowSlipService;
    private final BookService bookService;
    
    private JTextField txtReaderId;
    private JTextField txtIsbn;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<String> selectedIsbns;

    private final Color BACKGROUND_COLOR = new Color(30, 39, 46);
    private final Color SECONDARY_COLOR = new Color(47, 53, 66);
    private final Color TEXT_COLOR = Color.WHITE;

    public BorrowPanel() {
        this.borrowSlipService = new BorrowSlipService();
        this.bookService = new BookService();
        this.selectedIsbns = new ArrayList<>();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        
        JLabel titleLabel = new JLabel("LẬP PHIẾU MƯỢN SÁCH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        add(titleLabel, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(BACKGROUND_COLOR);

        
        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);

        JPanel readerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        readerPanel.setBackground(BACKGROUND_COLOR);
        readerPanel.add(createLabel("Mã độc giả (DGxxxx):"));
        txtReaderId = createTextField(15);
        readerPanel.add(txtReaderId);
        inputPanel.add(readerPanel);

        JPanel bookInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookInputPanel.setBackground(BACKGROUND_COLOR);
        bookInputPanel.add(createLabel("Mã ISBN Sách:"));
        txtIsbn = createTextField(15);
        StyledButton btnAddBook = new StyledButton("Thêm vào danh sách", new Color(0, 122, 255), Color.WHITE);
        btnAddBook.addActionListener(e -> handleAddBookToList());
        bookInputPanel.add(txtIsbn);
        bookInputPanel.add(btnAddBook);
        inputPanel.add(bookInputPanel);

        centerPanel.add(inputPanel, BorderLayout.NORTH);

        
        tableModel = new DefaultTableModel(new Object[]{"ISBN", "Tên sách", "Tác giả", "Thể loại"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setBackground(SECONDARY_COLOR);
        table.setForeground(TEXT_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Danh sách sách mượn", 0, 0, null, TEXT_COLOR));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(BACKGROUND_COLOR);

        StyledButton btnClear = new StyledButton("Làm mới", new Color(149, 165, 166), Color.WHITE);
        btnClear.addActionListener(e -> clearForm());
        
        StyledButton btnSubmit = new StyledButton("XÁC NHẬN MƯỢN", new Color(11, 232, 129), Color.DARK_GRAY);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setPreferredSize(new Dimension(200, 40));
        btnSubmit.addActionListener(e -> handleSubmit());

        footerPanel.add(btnClear);
        footerPanel.add(btnSubmit);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBackground(SECONDARY_COLOR);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        return field;
    }

    private void handleAddBookToList() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) return;

        if (selectedIsbns.contains(isbn)) {
            JOptionPane.showMessageDialog(this, "Sách này đã có trong danh sách mượn!");
            return;
        }

        List<Book> allBooks = bookService.getAllBooks();
        Book book = allBooks.stream().filter(b -> b.getIsbn().equals(isbn)).findFirst().orElse(null);

        if (book != null) {
            if (book.getSoLuong() <= 0) {
                JOptionPane.showMessageDialog(this, "Sách này đã hết trong kho!");
                return;
            }
            selectedIsbns.add(isbn);
            tableModel.addRow(new Object[]{book.getIsbn(), book.getTenSach(), book.getTacGia(), book.getTheLoai()});
            txtIsbn.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sách có mã ISBN này!");
        }
    }

    private void handleSubmit() {
        String readerId = txtReaderId.getText().trim();
        if (readerId.isEmpty() || selectedIsbns.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã độc giả và chọn ít nhất 1 quyển sách!");
            return;
        }

        String result = borrowSlipService.createBorrowSlip(readerId, selectedIsbns);
        if (result == null) {
            JOptionPane.showMessageDialog(this, "Lập phiếu mượn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtReaderId.setText("");
        txtIsbn.setText("");
        selectedIsbns.clear();
        tableModel.setRowCount(0);
    }
}
