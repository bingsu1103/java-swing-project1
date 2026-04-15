package com.library.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.library.service.AuthService;
import com.library.ui.component.StyledButton;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final AuthService authService;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginFrame() {
        this.authService = new AuthService();
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập - Hệ thống quản lý thư viện");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyPadding(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        JLabel subTitleLabel = new JLabel("Vui lòng đăng nhập để tiếp tục", SwingConstants.CENTER);
        subTitleLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        mainPanel.add(subTitleLabel, gbc);

        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tên đăng nhập");
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 5, 0);
        mainPanel.add(txtUsername, gbc);

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mật khẩu");
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 20, 0);
        mainPanel.add(txtPassword, gbc);

        StyledButton btnLogin = new StyledButton("Đăng nhập", new Color(0, 122, 255), Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.addActionListener(e -> handleLogin());
        gbc.gridy = 4;
        mainPanel.add(btnLogin, gbc);

        add(mainPanel);
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (authService.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            // Sau này sẽ mở MainFrame tại đây
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
