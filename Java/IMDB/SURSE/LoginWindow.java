package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private static int attempts = 5;
    public LoginWindow() {
        setTitle("IMDB Login");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel usernameLabel = new JLabel("Username:", SwingConstants.RIGHT);
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:", SwingConstants.RIGHT);
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signinButton = new JButton(("Signin"));
        JButton logoutButton = new JButton(("Logout"));

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            boolean found = false;
            User user = IMDB.findUser(username);

            if (user != null) {
                if (user.getUserInfo().getCredentials().getPassword().equals(password)) {
                    found = true;
                }
            }

            if (found) {
                attempts = 5;
                IMDB.user = user;
                JOptionPane.showMessageDialog(LoginWindow.this,
                        "Username: " + username,
                        "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
                MainWindow mainWindow = new MainWindow();
            }
            else {
                if (attempts == 0) {
                    IMDB.user = null;
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(LoginWindow.this,
                            "You have " + attempts + " more attempts",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);
                    attempts--;
                }
            }
        });

        signinButton.addActionListener(e -> {
            dispose();
            SigninWindow SigninWindow = new SigninWindow();
        });

        logoutButton.addActionListener(e -> {
            dispose();
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        add(panel, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("LOG IN");
        headerPanel.add(headerLabel);

        headerPanel.setBackground(Color.BLACK);
        Font font = new Font(null, Font.BOLD, 20);
        headerLabel.setFont(font);
        headerLabel.setForeground(Color.YELLOW);
        add(headerPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        add(leftPanel, BorderLayout.EAST);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);
        add(rightPanel, BorderLayout.WEST);

        JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.setBackground(Color.BLACK);
        if (IMDB.user != null && IMDB.user.getAccountType() == AccountType.ADMIN) {
            downPanel.add(signinButton);
        }

        downPanel.add(loginButton);
        downPanel.add(logoutButton);
        add(downPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
