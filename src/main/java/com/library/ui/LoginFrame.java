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
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(30, 39, 46));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("THƯ VIỆN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(titleLabel, gbc);

        JLabel subTitleLabel = new JLabel("HỆ THỐNG QUẢN LÝ", SwingConstants.CENTER);
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitleLabel.setForeground(new Color(189, 195, 199));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 40, 0);
        mainPanel.add(subTitleLabel, gbc);

        JLabel lblUsername = new JLabel("Tên đăng nhập");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsername.setForeground(new Color(210, 218, 226));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(lblUsername, gbc);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUsername.setPreferredSize(new Dimension(0, 45));
        txtUsername.setBackground(new Color(47, 53, 66));
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setCaretColor(Color.WHITE);
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "arc: 8; showClearButton: true");
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(txtUsername, gbc);

        JLabel lblPassword = new JLabel("Mật khẩu");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(new Color(210, 218, 226));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPassword.setPreferredSize(new Dimension(0, 45));
        txtPassword.setBackground(new Color(47, 53, 66));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "arc: 8; showRevealButton: true");
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(txtPassword, gbc);

        StyledButton btnLogin = new StyledButton("ĐĂNG NHẬP", new Color(11, 232, 129), Color.DARK_GRAY);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setPreferredSize(new Dimension(0, 50));
        btnLogin.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        btnLogin.addActionListener(e -> handleLogin());
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 0, 15, 0);
        mainPanel.add(btnLogin, gbc);

        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(30, 39, 46));
        
        JLabel lblNoAccount = new JLabel("Chưa có tài khoản?");
        lblNoAccount.setForeground(new Color(189, 195, 199));
        
        JButton btnRegister = new JButton("Đăng ký ngay");
        btnRegister.setBorderPainted(false);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setForeground(new Color(11, 232, 129));
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            this.dispose();
        });

        footerPanel.add(lblNoAccount);
        footerPanel.add(btnRegister);
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(footerPanel, gbc);

        add(mainPanel);
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (authService.login(username, password)) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            new MainFrame().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không chính xác!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
