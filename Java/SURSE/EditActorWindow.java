package org.example;

import javax.swing.*;
import java.awt.*;

public class EditActorWindow extends JFrame {
    public EditActorWindow(Actor actor) {
        setTitle("Settings");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2));

        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        JTextField nameField = new JTextField();
        nameField.setText(actor.getActorName());
        nameField.setEnabled(false);

        JLabel biographyLabel = new JLabel("Biography:", SwingConstants.RIGHT);
        JTextField biographyField = new JTextField();
        biographyField.setText(actor.getBiography());

        JButton saveButton = new JButton(("Save"));
        saveButton.addActionListener(e -> {
            String biography = biographyField.getText();
            if (biography.equals("")) {
                actor.setBiography("no info");
            }
            else {
                actor.setBiography(biography);
            }
            dispose();
            EditActorWindow editActor = new EditActorWindow(actor);
        });

        JButton resetButton = new JButton(("Reset"));
        resetButton.addActionListener(e -> {
            dispose();
            EditActorWindow editActor = new EditActorWindow(actor);
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(biographyLabel);
        panel.add(biographyField);

        add(panel, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("EDIT");
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
        downPanel.add(saveButton);
        downPanel.add(resetButton);
        add(downPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}