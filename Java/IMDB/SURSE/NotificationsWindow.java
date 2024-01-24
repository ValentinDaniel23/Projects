package org.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class NotificationsWindow extends JFrame {
    public NotificationsWindow() {
        setTitle("IMDB Notifications");
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


        DefaultListModel<String> notifications;
        JList<String> notificationsList;

        notifications = new DefaultListModel<>();
        List<String> nnot = IMDB.user.getNotifications();

        for (String notification : nnot) {
            notifications.addElement(notification);
        }

        notificationsList = new JList<>(notifications);

        notificationsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = notificationsList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        IMDB.user.getNotifications().remove(selectedIndex);

                        dispose();
                        NotificationsWindow notificationsWindow = new NotificationsWindow();
                    }
                }
            }
        });

        JScrollPane favoritesScrollPane = new JScrollPane(notificationsList);

        favoritesScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(favoritesScrollPane);

        setVisible(true);
    }
}