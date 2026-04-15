package com.library.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.library.service.AuthService;
import com.library.ui.component.StyledButton;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final AuthService authService;
    private JTextField txtUsername;
    private JTextField txtFullName;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    public RegisterFrame() {
        this.authService = new AuthService();
        initUI();
    }

    private void initUI() {
        setTitle("Đăng ký - Hệ thống quản lý thư viện");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(30, 39, 46));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("ĐĂNG KÝ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(titleLabel, gbc);

        JLabel subTitleLabel = new JLabel("Tạo tài khoản thủ thư mới", SwingConstants.CENTER);
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitleLabel.setForeground(new Color(189, 195, 199));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(subTitleLabel, gbc);

        addField(mainPanel, "Họ và tên", txtFullName = new JTextField(), 2);
        addField(mainPanel, "Tên đăng nhập", txtUsername = new JTextField(), 4);
        addField(mainPanel, "Mật khẩu", txtPassword = new JPasswordField(), 6);
        addField(mainPanel, "Xác nhận mật khẩu", txtConfirmPassword = new JPasswordField(), 8);
        StyledButton btnRegister = new StyledButton("ĐĂNG KÝ NGAY", new Color(11, 232, 129), Color.DARK_GRAY);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegister.setPreferredSize(new Dimension(0, 45));
        btnRegister.addActionListener(e -> handleRegister());
        gbc.gridy = 10;
        gbc.insets = new Insets(20, 0, 10, 0);
        mainPanel.add(btnRegister, gbc);

        JButton btnBack = new JButton("Quay lại Đăng nhập");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(new Color(52, 152, 219));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(btnBack, gbc);

        add(mainPanel);
    }

    private void addField(JPanel panel, String labelText, JTextField field, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(210, 218, 226));
        gbc.gridy = gridy;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(label, gbc);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(0, 45));
        field.setBackground(new Color(47, 53, 66));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);

        if (field instanceof JPasswordField) {
            field.putClientProperty(FlatClientProperties.STYLE, "arc: 8; showRevealButton: true");
        } else {
            field.putClientProperty(FlatClientProperties.STYLE, "arc: 8; showClearButton: true");
        }

        gbc.gridy = gridy + 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(field, gbc);
    }

    private void handleRegister() {
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authService.register(username, password, fullName)) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
