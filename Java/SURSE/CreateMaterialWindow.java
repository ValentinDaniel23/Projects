package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateMaterialWindow extends JFrame {
    private JTextArea commentTextArea;
    private JComboBox<String> typeComboBox;

    public CreateMaterialWindow() {
        setTitle("IMDB Create Material");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("IMDB");

        headerPanel.add(headerLabel);

        headerPanel.setBackground(Color.BLACK);
        Font font = new Font(null, Font.BOLD, 20);
        headerLabel.setFont(font);
        headerLabel.setForeground(Color.YELLOW);

        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel typePanel = new JPanel(new FlowLayout());
        typeComboBox = new JComboBox<>(new String[]{"Actor", "Movie"});
        typePanel.add(new JLabel("Type:"));
        typePanel.add(typeComboBox);

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        commentTextArea = new JTextArea();
        commentTextArea.setEditable(true);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane commentScrollPane = new JScrollPane(commentTextArea);
        commentPanel.add(new JLabel("Material name:"));
        commentPanel.add(commentScrollPane);

        centerPanel.add(typePanel);
        centerPanel.add(commentPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        add(leftPanel, BorderLayout.EAST);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);
        add(rightPanel, BorderLayout.WEST);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) typeComboBox.getSelectedItem();
                String name = commentTextArea.getText();

                if (selectedType.equals("Actor")) {
                    Actor actor = new Actor(name, "no info");
                    ((Staff) IMDB.user).addActorSystem(actor);
                }
                else {
                    Movie movie = new Movie(name, "no info", 0.0, "0 minutes", "2000");
                    ((Staff) IMDB.user).addProductionSystem(movie);
                }
                dispose();
            }
        });

        JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.add(createButton);
        downPanel.setBackground(Color.BLACK);
        add(downPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
