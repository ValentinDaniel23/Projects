package org.example;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SettingsWindow extends JFrame {
    public SettingsWindow() {
        setTitle("Settings");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(8, 2));

        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        JTextField nameField = new JTextField();
        nameField.setText(IMDB.user.getUserInfo().getName());

        JLabel usernameLabel = new JLabel("Username:", SwingConstants.RIGHT);
        JTextField usernameField = new JTextField();
        usernameField.setText(IMDB.user.getUsername());
        usernameField.setEnabled(false);

        JLabel emailLabel = new JLabel("Email:", SwingConstants.RIGHT);
        JTextField emailField = new JTextField();
        emailField.setText(IMDB.user.getUserInfo().getCredentials().getEmail());

        JLabel passwordLabel = new JLabel("Password:", SwingConstants.RIGHT);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setText(IMDB.user.getUserInfo().getCredentials().getPassword());

        JLabel countryLabel = new JLabel("Country:", SwingConstants.RIGHT);
        JTextField countryField = new JTextField();
        countryField.setText(IMDB.user.getUserInfo().getCountry());

        JLabel ageLabel = new JLabel("Age:", SwingConstants.RIGHT);
        JTextField ageField = new JTextField();
        ageField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        ageField.setText(String.valueOf(IMDB.user.getUserInfo().getAge()));

        JLabel genderLabel = new JLabel("Gender:", SwingConstants.RIGHT);
        JRadioButton  femaleRadioButton = new JRadioButton("Female");
        JRadioButton  maleRadioButton = new JRadioButton("Male");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(femaleRadioButton);
        genderGroup.add(maleRadioButton);
        JPanel genderPanel = new JPanel();
        genderPanel.add(femaleRadioButton);
        genderPanel.add(maleRadioButton);

        femaleRadioButton.setEnabled(false);
        maleRadioButton.setEnabled(false);
        if (IMDB.user.getUserInfo().getGender().equals("Female")) {
            femaleRadioButton.setSelected(true);
        }
        else {
            maleRadioButton.setSelected(true);
        }


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
        dateField.setText(IMDB.user.getUserInfo().getBirthDate().toString());

        JButton signinButton = new JButton(("Save"));
        signinButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String country = countryField.getText();
            int age = 0;
            if (!ageField.getText().equals("")) {
                age = Integer.valueOf(ageField.getText());
            }
            String birthdate = dateField.getText();

            if (name.equals("") || email.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(SettingsWindow.this,
                        "name, email and password should be filled",
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
                    if (year < 1994 || year > 2006) {
                        validDate = false;
                    }
                }
                if (!validDate) {
                    JOptionPane.showMessageDialog(SettingsWindow.this,
                            "not a valid birth date",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    IMDB.user.getUserInfo().getCredentials().setEmail(email);
                    IMDB.user.getUserInfo().getCredentials().setPassword(password);
                    IMDB.user.getUserInfo().setAge(age);
                    IMDB.user.getUserInfo().setCountry(country);
                    IMDB.user.getUserInfo().setName(name);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate birthDate2 = LocalDate.parse(birthdate, formatter);
                    IMDB.user.getUserInfo().setBirthDate(birthDate2);
                    JOptionPane.showMessageDialog(SettingsWindow.this,
                            "settings updated",
                            "Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton loginButton = new JButton(("Reset"));
        loginButton.addActionListener(e -> {
            dispose();
            SettingsWindow settingsWindow = new SettingsWindow();
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(countryLabel);
        panel.add(countryField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(genderLabel);
        panel.add(genderPanel, BorderLayout.EAST);
        panel.add(birthdateLabel);
        panel.add(dateField);
        add(panel, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("SETTINGS");
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
