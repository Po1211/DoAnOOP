/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.maybannuocuong;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private boolean authenticated = false;
    private String authenticatedUsername;

    public LoginDialog(JFrame parent) {
        super(parent, "Admin Login", true);
        setSize(300, 150);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (DatabaseManager.authenticateUser(username, password) && DatabaseManager.isAdmin(username)) {
                authenticated = true;
                authenticatedUsername = username;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or not an admin");
            }
        });

        setLocationRelativeTo(parent);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getAuthenticatedUsername() {
        return authenticatedUsername;
    }
}
