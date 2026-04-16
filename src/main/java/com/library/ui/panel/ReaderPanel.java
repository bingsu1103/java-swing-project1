package com.library.ui.panel;

import com.library.model.Reader;
import com.library.service.ReaderService;
import com.library.ui.component.FormDialog;
import com.library.ui.component.StyledButton;
import com.library.util.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
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

        // Action section
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
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

    private void handleAdd() {
        // Tự động lấy mã mới khi nhấn Thêm
        String nextId = readerService.getNextReaderId();
        ReaderForm form = new ReaderForm(null, nextId);
        FormDialog dialog = new FormDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Độc giả", form);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String error = readerService.addReader(form.getReader());
            if (error != null) JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            else refreshTable();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một độc giả để sửa!");
            return;
        }
        String id = (String) table.getValueAt(row, 0);
        Reader reader = readerService.getAllReaders().stream().filter(r -> r.getMaDocGia().equals(id)).findFirst().orElse(null);
        
        if (reader != null) {
            ReaderForm form = new ReaderForm(reader, id);
            FormDialog dialog = new FormDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Độc giả", form);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                String error = readerService.updateReader(form.getReader());
                if (error != null) JOptionPane.showMessageDialog(this, error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                else refreshTable();
            }
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa độc giả này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            readerService.deleteReader((String) table.getValueAt(row, 0));
            refreshTable();
        }
    }

    private static class ReaderForm extends JPanel {
        private final JTextField txtHoTen, txtCmnd, txtNgaySinh, txtEmail, txtDiaChi, txtMa;
        private final JComboBox<String> cbGioiTinh;
        private Reader currentReader;

        public ReaderForm(Reader reader, String id) {
            this.currentReader = reader;
            setLayout(new GridLayout(0, 2, 10, 10));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            add(new JLabel("Mã ĐG (Tự động):"));
            txtMa = new JTextField(id); 
            txtMa.setEditable(false); // Khóa ô nhập mã
            txtMa.setBackground(new Color(236, 240, 241)); // Thêm màu nền để phân biệt ô bị khóa
            add(txtMa);

            add(new JLabel("Họ tên:")); txtHoTen = new JTextField(); add(txtHoTen);
            add(new JLabel("CMND:")); txtCmnd = new JTextField(); add(txtCmnd);
            add(new JLabel("Ngày sinh (dd/MM/yyyy):")); txtNgaySinh = new JTextField(); add(txtNgaySinh);
            add(new JLabel("Giới tính:")); cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"}); add(cbGioiTinh);
            add(new JLabel("Email:")); txtEmail = new JTextField(); add(txtEmail);
            add(new JLabel("Địa chỉ:")); txtDiaChi = new JTextField(); add(txtDiaChi);

            if (reader != null) {
                txtHoTen.setText(reader.getHoTen());
                txtCmnd.setText(reader.getCmnd());
                txtNgaySinh.setText(DateUtil.formatDate(reader.getNgaySinh()));
                cbGioiTinh.setSelectedItem(reader.getGioiTinh());
                txtEmail.setText(reader.getEmail());
                txtDiaChi.setText(reader.getDiaChi());
            }
        }

        public Reader getReader() {
            Reader r = currentReader != null ? currentReader : new Reader();
            r.setMaDocGia(txtMa.getText()); // Mã lấy từ ô khóa
            r.setHoTen(txtHoTen.getText());
            r.setCmnd(txtCmnd.getText());
            r.setNgaySinh(DateUtil.parseDate(txtNgaySinh.getText()));
            r.setGioiTinh((String) cbGioiTinh.getSelectedItem());
            r.setEmail(txtEmail.getText());
            r.setDiaChi(txtDiaChi.getText());
            if (currentReader == null) r.setNgayLapThe(LocalDate.now());
            return r;
        }
    }
}
