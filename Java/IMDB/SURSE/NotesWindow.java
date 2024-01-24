package org.example;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class NotesWindow extends JFrame {
    public NotesWindow(List<Rating> ratings) {
        setTitle("IMDB Notes");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel headerPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel=new JLabel("IMDB");

        headerPanel.add(headerLabel);

        headerPanel.setBackground(Color.BLACK);
        Font font=new Font(null, Font.BOLD, 20);
        headerLabel.setFont(font);
        headerLabel.setForeground(Color.YELLOW);

        add(headerPanel, BorderLayout.NORTH);

        JPanel leftPanel=new JPanel();
        leftPanel.setBackground(Color.BLACK);
        add(leftPanel, BorderLayout.EAST);

        JPanel rightPanel=new JPanel();
        rightPanel.setBackground(Color.BLACK);
        add(rightPanel, BorderLayout.WEST);

        JPanel downPanel=new JPanel();
        downPanel.setBackground(Color.BLACK);
        add(downPanel, BorderLayout.SOUTH);


        JScrollPane scrollPane = new JScrollPane();
        JPanel notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));

        for (Rating rating : ratings) {
            StringBuilder htmlText = new StringBuilder("<html>");
            htmlText.append("<b>").append(rating.getNote()).append("/10</b><br>");
            htmlText.append(rating.getUsername()).append(" :<br>");
            htmlText.append(rating.getComments());
            htmlText.append("</html>");

            JLabel noteLabel = new JLabel(htmlText.toString());
            noteLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
            notesPanel.add(noteLabel);
        }

        scrollPane.setViewportView(notesPanel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}