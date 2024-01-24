package org.example;

import javax.swing.*;
import java.awt.*;

public class EditProductionWindow extends JFrame {
    public EditProductionWindow(Production production) {
        setTitle("Settings");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel nameLabel = new JLabel("Name:", SwingConstants.RIGHT);
        JTextField nameField = new JTextField();
        nameField.setText(production.getTitle());
        nameField.setEnabled(false);

        JLabel plotLabel = new JLabel("Plot:", SwingConstants.RIGHT);
        JTextField plotField = new JTextField();
        plotField.setText(production.getDescription());

        JLabel releaseYearLabel = new JLabel("ReleaseYear:", SwingConstants.RIGHT);
        JTextField releaseYearField = new JTextField();
        String date;
        if (production instanceof Movie) date = ((Movie) production).getReleaseYear();
        else date = ((Series) production).getReleaseYear();
        releaseYearField.setText(date);

        JButton saveButton = new JButton(("Save"));
        saveButton.addActionListener(e -> {
            String plot = plotField.getText();
            String releaseYear = releaseYearField.getText();
            if (plot.equals("")) {
                production.setDescription("no info");
            }
            else {
                production.setDescription(plot);
            }
            if (releaseYear.equals("")) {
                if (production instanceof Movie) {
                    ((Movie) production).setReleaseYear("no info");
                }
                else {
                    ((Series) production).setReleaseYear("no info");
                }
            }
            else {
                if (production instanceof Movie) {
                    ((Movie) production).setReleaseYear(releaseYear);
                }
                else {
                    ((Series) production).setReleaseYear(releaseYear);
                }
            }
            dispose();
            EditProductionWindow editProduction = new EditProductionWindow(production);
        });

        JButton resetButton = new JButton(("Reset"));
        resetButton.addActionListener(e -> {
            dispose();
            EditProductionWindow editProduction = new EditProductionWindow(production);
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(plotLabel);
        panel.add(plotField);
        panel.add(releaseYearLabel);
        panel.add(releaseYearField);

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