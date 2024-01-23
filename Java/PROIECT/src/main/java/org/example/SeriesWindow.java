package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeriesWindow extends JFrame {
    private JTextField searchField;
    private JPopupMenu popupMenu;
    private JComboBox<String> categoryComboBox;

    public SeriesWindow(String name) {
        setTitle("IMDB Series");
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

        Series series = (Series) IMDB.findProduction(name);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        JPanel panel = new JPanel(new GridLayout(1, 2));

        JPanel moviePanel = new JPanel();
        moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.Y_AXIS));
        JPanel descriptionPanel = new JPanel(new BorderLayout());

        JTextArea descriptionTextArea = new JTextArea();
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Plot: " + series.getDescription() + "\n\n");
        stringBuilder.append(series.getReleaseYear() + " : " + series.getNumSeasons() + " seasons");
        List<String> directors = series.getDirectorsName();
        stringBuilder.append("\n\n" + "Directors: \n");
        for (String director : directors) {
            stringBuilder.append(director + "\n");
        }

        descriptionTextArea.setText(String.valueOf(stringBuilder));

        Font font2 = new Font(null, Font.ITALIC, 15);
        descriptionTextArea.setBackground(Color.DARK_GRAY);
        descriptionTextArea.setForeground(Color.WHITE);
        descriptionTextArea.setFont(font2);
        descriptionTextArea.setMargin(new Insets(0, 0, 0, 0));
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
        descriptionScrollPane.setViewportBorder(null);
        descriptionScrollPane.setBorder(null);

        JLabel nameLabel = new JLabel(series.getTitle());

        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton plusButton = new JButton();
        plusButton.setPreferredSize(new Dimension(20, 20));
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommentWindow commentWindow = new CommentWindow(series);
            }
        });
        JLabel noteLabel = new JLabel("Avg rating: " + series.getAverageRating().toString() + "/10");
        noteLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Collections.sort(series.getRatings());
                NotesWindow notesWindow = new NotesWindow(series.getRatings());
            }
        });

        JButton addToFavoritesButton = new JButton("+ Add to My List");
        addToFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean found = false;
                if (IMDB.user.getFavorites().contains(series)) found = true;

                if (found == true) {
                    JOptionPane.showMessageDialog(SeriesWindow.this,
                            "Already in your favorite list",
                            "Add to favorite list",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    IMDB.user.addToFavorites(series);
                    JOptionPane.showMessageDialog(SeriesWindow.this,
                            "Added to your favorite list",
                            "Add to favorite list",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        addToFavoritesButton.setPreferredSize(new Dimension(20, 20));

        JLabel photoLabel = new JLabel("no image available");

        JPanel genresPanel = new JPanel();
        genresPanel.setBackground(Color.DARK_GRAY);

        for (Genre genre : series.getGenres()) {
            String stringgenre = genre.name();
            JLabel genreLabel = new JLabel(stringgenre);
            genreLabel.setForeground(Color.WHITE);
            genreLabel.setBackground(Color.BLACK);
            genreLabel.setOpaque(true);

            genreLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            genresPanel.add(genreLabel);
        }

        JLabel episodesLabel = new JLabel("Episodes >");
        episodesLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EpisodesWindow episodesWindow = new EpisodesWindow(series.getFranchise(), series.getNumSeasons());
            }
        });
        episodesLabel.setForeground(Color.white);

        moviePanel.setBackground(Color.DARK_GRAY);

        Font font3 = new Font(null, Font.BOLD, 20);
        moviePanel.setBackground(Color.DARK_GRAY);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(font3);
        photoLabel.setForeground(Color.white);
        noteLabel.setForeground(Color.RED);

        ratingPanel.setBackground(Color.DARK_GRAY);
        ratingPanel.add(plusButton);
        ratingPanel.add(noteLabel);

        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        moviePanel.add(nameLabel);

        ratingPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        moviePanel.add(ratingPanel);

        addToFavoritesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        moviePanel.add(addToFavoritesButton);

        moviePanel.add(Box.createVerticalStrut(25));

        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        moviePanel.add(photoLabel);

        moviePanel.add(Box.createVerticalStrut(25));

        genresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        moviePanel.add(genresPanel);

        episodesLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        moviePanel.add(episodesLabel);

        descriptionPanel.add(descriptionScrollPane, BorderLayout.CENTER);

        panel.add(moviePanel);
        panel.add(descriptionPanel);
        centerPanel.add(panel);

        DefaultListModel<String> actorsModel;
        List<String> actors = new ArrayList<>();
        JList<String> actorsList;

        actorsModel = new DefaultListModel<>();

        for (String actor : series.getActorsName()) {
            actorsModel.addElement(actor + " : Actor");
            actors.add(actor);
        }
        actorsList = new JList<>(actorsModel);

        actorsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = actorsList.getSelectedIndex();

                    if (selectedIndex != -1) {
                        String actor = actors.get(selectedIndex);

                        dispose();
                        ActorWindow actorWindow = new ActorWindow(actor);
                    }
                }
            }
        });

        JScrollPane rolesScrollPane = new JScrollPane(actorsList);

        centerPanel.add(rolesScrollPane, BorderLayout.CENTER);
        add(centerPanel);

        if (IMDB.user.getAccountType() != AccountType.ADMIN) {
            JButton reportButton = new JButton("REPORT PROBLEM");
            reportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RequestTypes requestTypes = RequestTypes.MOVIE_ISSUE;
                    LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                    String username = IMDB.user.getUsername();
                    String movieTitle = name;
                    String to = IMDB.getProductionUser(series).getUsername();
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