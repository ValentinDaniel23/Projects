package org.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class ViewRequestsWindow extends JFrame {
    public ViewRequestsWindow(List<Request> rrequests) {
        setTitle("IMDB Requests");
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


        DefaultListModel<String> requests;
        JList<String> requestsList;

        requests = new DefaultListModel<>();

        for (Request request : rrequests) {
            if (request.getType() == RequestTypes.MOVIE_ISSUE || request.getType() == RequestTypes.ACTOR_ISSUE) {
                requests.addElement(request.getRequesterUsername() + ": " + request.getDescription() + " - " + request.getTitle() + " - " + request.getCreationDate().toString());
            }
            else {
                requests.addElement(request.getRequesterUsername() + ": " + request.getDescription() + " - " + request.getCreationDate().toString());
            }
        }

        requestsList = new JList<>(requests);

        requestsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = requestsList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        Request request = rrequests.get(selectedIndex);
                        int choice = JOptionPane.showOptionDialog(
                                ViewRequestsWindow.this,
                                "Do you want to accept or reject?",
                                "Request",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                new Object[]{"Accept", "Reject"},
                                "Accept"
                        );

                        if (choice == JOptionPane.YES_OPTION) {
                            if (request.getResolverUsername().equals("ADMIN")) {
                                Admin.RequestsHolder.solveRequest(request, "Accept");
                            }
                            else {
                                ((Staff) IMDB.user).solveRequest(request, "Accept");
                            }

                            dispose();
                            ViewRequestsWindow viewRequestsWindow = new ViewRequestsWindow(rrequests);
                        } else if (choice == JOptionPane.NO_OPTION) {
                            if (request.getResolverUsername().equals("ADMIN")) {
                                Admin.RequestsHolder.solveRequest(request, "Reject");
                            }
                            else {
                                ((Staff) IMDB.user).solveRequest(request, "Reject");
                            }

                            dispose();
                            ViewRequestsWindow viewRequestsWindow = new ViewRequestsWindow(rrequests);
                        }
                    }
                }
            }
        });

        JScrollPane favoritesScrollPane = new JScrollPane(requestsList);

        favoritesScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(favoritesScrollPane);

        setVisible(true);
    }
}