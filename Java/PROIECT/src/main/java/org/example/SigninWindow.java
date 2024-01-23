package org.example;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class SigninWindow extends JFrame {
    public SigninWindow() {
        setTitle("IMDB Signin");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2));

        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        JTextField nameField = new JTextField();

        JLabel usernameLabel = new JLabel("Username:", SwingConstants.RIGHT);
        JTextField usernameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:", SwingConstants.RIGHT);
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:", SwingConstants.RIGHT);
        JPasswordField passwordField = new JPasswordField();

        JLabel genderLabel = new JLabel("Gender:", SwingConstants.RIGHT);
        JRadioButton  femaleRadioButton = new JRadioButton("Female");
        JRadioButton  maleRadioButton = new JRadioButton("Male");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(femaleRadioButton);
        genderGroup.add(maleRadioButton);
        JPanel genderPanel = new JPanel();
        genderPanel.add(femaleRadioButton);
        genderPanel.add(maleRadioButton);
        femaleRadioButton.setSelected(true);

        JLabel birthdateLabel = new JLabel("BirthDate:", SwingConstants.RIGHT);
        MaskFormatter dateFormatter = null;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        dateFormatter.setPlaceholderCharacter('_');
        JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
        dateField.setColumns(10);

        JButton signinButton = new JButton(("Sign in"));
        signinButton.addActionListener(e -> {
            String name = nameField.getText();
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String gender = "";
            if (femaleRadioButton.isSelected()) {
                gender =femaleRadioButton.getText();
            }
            else if (maleRadioButton.isSelected()) {
                gender = maleRadioButton.getText();
            }
            String birthdate = dateField.getText();

            // Create account
            if (name.equals("") || username.equals("") || email.equals("") || password.equals("") || gender.equals("")) {
                JOptionPane.showMessageDialog(SigninWindow.this,
                        "all data is mandatory",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                boolean validDate = true;

                byte[] date = birthdate.getBytes();
                if (birthdate.startsWith("_")) {
                    validDate = false;
                }
                else {
                    int year = 0;
                    int month = 0;
                    int day = 0;
                    for (int i = 0; i < 10; i++) {
                        if (i < 4) {
                            year = year * 10 + date[i] - '0';
                        }
                        if (i > 4 && i < 7) {
                            month = month * 10 + date[i] - '0';
                        }
                        if (i > 7) {
                            day = day * 10 + date[i] - '0';
                        }
                    }
                    if (month < 0 || month > 12) {
                        validDate = false;
                    }
                    if (day < 0 || day > 31) {
                        validDate = false;
                    }
                    if (year < 1900 || year > 2006) {
                        validDate = false;
                    }
                }
                if (!validDate) {
                    JOptionPane.showMessageDialog(SigninWindow.this,
                            "not a valid birth date",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    Credentials credentials = new Credentials(email, password);
                    User.Information information = new User.Information.informationBuilder(credentials)
                            .name(name)
                            .gender(gender)
                            .birthDate(birthdate)
                            .age(18)
                            .country("no info")
                            .build();
                    User user = UserFactory.factory(information, 0, AccountType.REGULAR, username);
                    IMDB.getUsers().add(user);
                    JOptionPane.showMessageDialog(SigninWindow.this,
                            "new user created",
                            "Signin Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton loginButton = new JButton(("Login"));
        loginButton.addActionListener(e -> {
            dispose();
            LoginWindow LoginWindow = new LoginWindow();
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(genderLabel);
        panel.add(genderPanel, BorderLayout.EAST);
        panel.add(birthdateLabel);
        panel.add(dateField);
        add(panel, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("SIGN IN");
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
        downPanel.add(loginButton);
        downPanel.add(signinButton);
        add(downPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
