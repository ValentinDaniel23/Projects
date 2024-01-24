package org.example;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class ActorWindow extends JFrame {
    private JTextField searchField;
    private JPopupMenu popupMenu;
    private JComboBox<String> categoryComboBox;

    public ActorWindow(String name) {
        setTitle("IMDB Actor");
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

        JPanel downPanel = new JPanel();
        downPanel.setBackground(Color.BLACK);
        add(downPanel, BorderLayout.SOUTH);

        Actor actor = IMDB.findActor(name);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        JPanel panel = new JPanel(new GridLayout(1, 2));

        JPanel actorPanel = new JPanel();
        actorPanel.setLayout(new BoxLayout(actorPanel, BoxLayout.Y_AXIS));
        JPanel biographyPanel = new JPanel(new BorderLayout());

        JTextArea biographyTextArea = new JTextArea();
        biographyTextArea.setEditable(false);
        biographyTextArea.setLineWrap(true);
        biographyTextArea.setWrapStyleWord(true);
        biographyTextArea.setText("Biography: " + actor.getBiography());
        Font font2 = new Font(null, Font.ITALIC, 15);
        biographyTextArea.setBackground(Color.DARK_GRAY);
        biographyTextArea.setForeground(Color.WHITE);
        biographyTextArea.setFont(font2);
        biographyTextArea.setMargin(new Insets(0, 0, 0, 0));
        JScrollPane biographyScrollPane = new JScrollPane(biographyTextArea);
        biographyScrollPane.setViewportBorder(null);
        biographyScrollPane.setBorder(null);

        JLabel nameLabel = new JLabel(actor.getActorName());
        JButton addToFavoritesButton = new JButton("Add to favorite");
        addToFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean found = false;
                if (IMDB.user.getFavorites().contains(actor)) found = true;

                if (found == true) {
                    JOptionPane.showMessageDialog(ActorWindow.this,
                            "Already in your favorite list",
                            "Add to favorite list",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    IMDB.user.addToFavorites(actor);
                    JOptionPane.showMessageDialog(ActorWindow.this,
                            "Added to your favorite list",
                            "Add to favorite list",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        JLabel photoLabel = new JLabel("no image available");

        Font font3 = new Font(null, Font.BOLD, 20);
        actorPanel.setBackground(Color.DARK_GRAY);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(font3);
        photoLabel.setForeground(Color.white);

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actorPanel.add(nameLabel);

        addToFavoritesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actorPanel.add(addToFavoritesButton);

        actorPanel.add(Box.createVerticalStrut(25));

        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actorPanel.add(photoLabel);

        biographyPanel.add(biographyScrollPane, BorderLayout.CENTER);

        panel.add(actorPanel);
        panel.add(biographyPanel);
        centerPanel.add(panel);

        DefaultListModel<String> rolesListModel;
        List<String> ttype = new ArrayList<>();
        List<String> pproduction = new ArrayList<>();
        JList<String> rolesList;

        rolesListModel = new DefaultListModel<>();

        for (Map.Entry<String, String> role : actor.getRoles()) {
            rolesListModel.addElement(role.getKey() + ": " + role.getValue());
            ttype.add(role.getValue());
            pproduction.add(role.getKey());
        }
        rolesList = new JList<>(rolesListModel);

        rolesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = rolesList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        String type = ttype.get(selectedIndex);
                        String production = pproduction.get(selectedIndex);

                        dispose();
                        if (type.equals("Movie")) {
                            MovieWindow movieWindow = new MovieWindow(production);
                        }
                        else {
                            SeriesWindow seriesWindow = new SeriesWindow(production);
                        }
                    }
                }
            }
        });

        JScrollPane rolesScrollPane = new JScrollPane(rolesList);

        centerPanel.add(rolesScrollPane, BorderLayout.CENTER);
        add(centerPanel);

        if (IMDB.user.getAccountType() != AccountType.ADMIN) {
            JButton reportButton = new JButton("REPORT PROBLEM");
            reportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RequestTypes requestTypes = RequestTypes.ACTOR_ISSUE;
                    LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                    String username = IMDB.user.getUsername();
                    String movieTitle = name;
                    String to = IMDB.getActorUser(actor).getUsername();
                    Request request = new Request(requestTypes, currentTime, movieTitle, "", username, to);

                    RequestWindow requestWindow = new RequestWindow(request);
                }
            });

            downPanel.add(reportButton);
        }

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