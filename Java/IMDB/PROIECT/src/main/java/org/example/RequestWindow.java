package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RequestWindow extends JFrame {
    private JTextArea commentTextArea;

    public RequestWindow(Request request) {
        setTitle("IMDB Requests");
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

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        commentTextArea = new JTextArea();
        commentTextArea.setEditable(true);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane commentScrollPane = new JScrollPane(commentTextArea);
        commentPanel.add(new JLabel("Create request:"));
        commentPanel.add(commentScrollPane);

        centerPanel.add(commentPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        add(leftPanel, BorderLayout.EAST);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);
        add(rightPanel, BorderLayout.WEST);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                request.setDescription(commentTextArea.getText());
                User user = IMDB.findUser(request.getRequesterUsername());
                boolean found = false;
                if (user instanceof Regular) {
                    found = ((Regular) user).createRequest(request);
                }
                if (user instanceof Contributor) {
                    found = ((Contributor) user).createRequest(request);
                }

                if (found) {
                    JOptionPane.showMessageDialog(RequestWindow.this,
                            "Request sent",
                            "Request",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(RequestWindow.this,
                            "Already sent",
                            "Request",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = IMDB.findUser(request.getRequesterUsername());
                boolean found = false;
                if (user instanceof Regular) {
                    found = ((Regular) user).removeRequest(request);
                }
                if (user instanceof Contributor) {
                    found = ((Contributor) user).removeRequest(request);
                }

                if (found) {
                    JOptionPane.showMessageDialog(RequestWindow.this,
                            "Removed request",
                            "Request",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(RequestWindow.this,
                            "Already removed",
                            "Request",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.add(removeButton);
        downPanel.add(sendButton);
        downPanel.setBackground(Color.BLACK);
        add(downPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}