package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class ProfileWindow extends JFrame {
    private JTextField searchField;
    private JPopupMenu popupMenu;
    private JComboBox<String> categoryComboBox;

    public ProfileWindow() {
        setTitle("IMDB Profile");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("IMDB");
        headerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                MainWindow mainWindow = new MainWindow();
            }
        });
        headerPanel.add(headerLabel);

        headerPanel.setBackground(Color.BLACK);
        Font font = new Font(null, Font.BOLD, 20);
        headerLabel.setFont(font);
        headerLabel.setForeground(Color.YELLOW);

        String[] categories = {"Movie", "Series", "Actor"};
        categoryComboBox = new JComboBox<>(categories);

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        popupMenu = new JPopupMenu();

        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateResults();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateResults();
            }
        });

        headerPanel.add(searchField);
        headerPanel.add(categoryComboBox);
        headerPanel.add(searchButton);

        add(headerPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        add(leftPanel, BorderLayout.EAST);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);
        add(rightPanel, BorderLayout.WEST);

        JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.setBackground(Color.BLACK);
        JButton settings = new JButton("Settings");
        settings.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SettingsWindow settingsWindow = new SettingsWindow();
            }
        });
        if (IMDB.user.getAccountType() != AccountType.REGULAR) {
            JButton staff=new JButton("Staff");
            staff.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    dispose();
                    StaffWindow StaffWindow = new StaffWindow();
                }
            });
            downPanel.add(staff);
        }
        downPanel.add(settings);

        add(downPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        JPanel uppanel = new JPanel();

        uppanel.setLayout(new BoxLayout(uppanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(IMDB.user.getUsername() + " : " + IMDB.user.getAccountType().toString());
        JLabel rating;
        if (IMDB.user.getAccountType() == AccountType.ADMIN) {
            rating = new JLabel("Experience: infinite");
        }
        else {
            rating = new JLabel("Experience: " + IMDB.user.getExperience());
        }
        JButton listFavoritesButton = new JButton("List of Favorites");
        listFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FavoritesWindow favoritesWindow = new FavoritesWindow();
            }
        });
        JButton notificationsButton = new JButton("List of Notifications");
        notificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NotificationsWindow notificationsWindow = new NotificationsWindow();
            }
        });

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Font font1 = new Font(null, Font.BOLD, 20);
        nameLabel.setFont(font1);
        uppanel.add(nameLabel);

        rating.setAlignmentX(Component.CENTER_ALIGNMENT);
        uppanel.add(rating);

        uppanel.add(Box.createVerticalStrut(35));

        listFavoritesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uppanel.add(listFavoritesButton);

        uppanel.add(Box.createVerticalStrut(15));

        notificationsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uppanel.add(notificationsButton);

        uppanel.setBackground(Color.DARK_GRAY);
        nameLabel.setForeground(Color.white);
        rating.setForeground(Color.red);

        centerPanel.add(uppanel);

        JPanel ddownpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if (IMDB.user.getAccountType() == AccountType.ADMIN) centerPanel.add(new JPanel());
        else {
            JButton deleteButton = new JButton("Delete Account");
            JButton reportButton = new JButton("Report Problem");

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RequestTypes requestTypes = RequestTypes.DELETE_ACCOUNT;
                    LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                    String username = IMDB.user.getUsername();
                    String to = "ADMIN";
                    Request request = new Request(requestTypes, currentTime, null, "", username, to);

                    RequestWindow requestWindow = new RequestWindow(request);
                }
            });

            reportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RequestTypes requestTypes = RequestTypes.OTHERS;
                    LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                    String username = IMDB.user.getUsername();
                    String to = "ADMIN";
                    Request request = new Request(requestTypes, currentTime, null, "", username, to);

                    RequestWindow requestWindow = new RequestWindow(request);
                }
            });

            ddownpanel.add(deleteButton);
            ddownpanel.add(reportButton);

            centerPanel.add(ddownpanel);

        }
        add(centerPanel);

        setVisible(true);
    }

    private void updateResults() {
        String searchText = searchField.getText().toLowerCase();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();

        popupMenu.removeAll();

        int count = 0;
        if (selectedCategory.equals("Actor")) {
            for (Actor actor : IMDB.getActors()) {
                if (actor.getActorName().toLowerCase().contains(searchText)) {
                    JMenuItem menuItem=new JMenuItem(actor.getActorName());
                    menuItem.addActionListener(new ResultActionListener("Actor", actor.getActorName()));
                    popupMenu.add(menuItem);
                    count++;
                }
                if (count == 5) {
                    break;
                }
            }
        }
        if (selectedCategory.equals("Movie")) {
            for (Production production : IMDB.getProductions()) {
                if (production instanceof Movie && production.getTitle().toLowerCase().contains(searchText) ) {
                    JMenuItem menuItem=new JMenuItem(production.getTitle());
                    menuItem.addActionListener(new ResultActionListener("Movie", production.getTitle()));
                    popupMenu.add(menuItem);
                    count++;
                }
                if (count == 5) {
                    break;
                }
            }
        }
        if (selectedCategory.equals("Series")) {
            for (Production production : IMDB.getProductions()) {
                if (production instanceof Series && production.getTitle().toLowerCase().contains(searchText) ) {
                    JMenuItem menuItem=new JMenuItem(production.getTitle());
                    menuItem.addActionListener(new ResultActionListener("Series", production.getTitle()));
                    popupMenu.add(menuItem);
                    count++;
                }
                if (count == 5) {
                    break;
                }
            }
        }

        popupMenu.show(searchField, 0, searchField.getHeight());
    }

    private class ResultActionListener implements ActionListener {
        private String result;
        private String type;

        public ResultActionListener(String type, String result) {
            this.result = result;
            this.type = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            if (type.equals("Actor")) {
                ActorWindow actorWindow = new ActorWindow(result);
            }
            if (type.equals("Movie")) {
                MovieWindow movieWindow = new MovieWindow(result);
            }
            if (type.equals("Series")) {
                SeriesWindow seriesWindow = new SeriesWindow(result);
            }
        }
    }
}