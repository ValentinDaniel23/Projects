package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainWindow extends JFrame {
    private JTextField searchField;
    private JPopupMenu popupMenu;
    private JComboBox<String> categoryComboBox;

    public MainWindow() {
        setTitle("IMDB Main");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("IMDB");
        headerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                LoginWindow loginWindow = new LoginWindow();
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

        JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));;
        downPanel.setBackground(Color.BLACK);
        JButton profile = new JButton("Your Profile");
        profile.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                ProfileWindow profileWindow = new ProfileWindow();
            }
        });
        downPanel.add(profile);
        add(downPanel, BorderLayout.SOUTH);

        Collections.sort(IMDB.getActors());
        Collections.sort(IMDB.getProductions());

        JPanel centerPanel = new JPanel(new GridLayout(1, 3));

        List<Object> list1 = new ArrayList();
        List<Object> list2 = new ArrayList();
        List<Object> list3 = new ArrayList();
        for (Actor actor : IMDB.getActors()) {
            list1.add(actor);
        }
        for (Production production : IMDB.getProductions()) {
            if (production instanceof Movie) list2.add(production);
        }
        for (Production production : IMDB.getProductions()) {
            if (production instanceof Series) list3.add(production);
        }

        JScrollPane scrollPane1 = createScrollableList(list1, "Actor");
        centerPanel.add(scrollPane1);
        JScrollPane scrollPane2 = createScrollableList(list2, "Movie");
        centerPanel.add(scrollPane2);
        JScrollPane scrollPane3 = createScrollableList(list3, "Series");
        centerPanel.add(scrollPane3);

        add(centerPanel, BorderLayout.CENTER);

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
    private JScrollPane createScrollableList(List<Object> list, String type) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Object object : list) {
            if (object instanceof Actor) {
                listModel.addElement(((Actor) object).getActorName() + " : Actor");
            }
            if (object instanceof Movie) {
                listModel.addElement(((Movie) object).getTitle() + " : Movie");
            }
            if (object instanceof Series) {
                listModel.addElement(((Series) object).getTitle() + " : Series");
            }
        }

        JList<String> llist = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(llist);

        scrollPane.setBorder(BorderFactory.createTitledBorder(type));

        return scrollPane;
    }
}