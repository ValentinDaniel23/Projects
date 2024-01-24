package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.TreeSet;


public class StaffWindow extends JFrame {
    private JTextField searchField;
    private JPopupMenu popupMenu;
    private JComboBox<String> categoryComboBox;

    public StaffWindow() {
        setTitle("IMDB Staff");
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
        JButton profile = new JButton("Your Profile");
        profile.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                ProfileWindow profileWindow = new ProfileWindow();
            }
        });
        JButton delete = new JButton("Delete User");
        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                String username = JOptionPane.showInputDialog("Introduceți username-ul:");

                if (username != null && !username.isEmpty()) {
                    boolean found = ((Admin) IMDB.user).deleteUser(username);

                    if (!found) {
                        JOptionPane.showMessageDialog(StaffWindow.this,
                                "No user deleted",
                                "Message",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(StaffWindow.this,
                                "User " + username + " deleted",
                                "Message",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(StaffWindow.this,
                            "No user deleted",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        downPanel.add(profile);
        if (IMDB.user.getAccountType() == AccountType.ADMIN) downPanel.add(delete);
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
        JButton listFavoritesButton = new JButton("List of Your Requests");
        listFavoritesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewRequestsWindow viewRequestsWindow = new ViewRequestsWindow( ((Staff) IMDB.user).getAssignedRequests() );
            }
        });
        JButton adminButton = new JButton("List of Admin Requests");
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewRequestsWindow viewRequestsWindow = new ViewRequestsWindow( Admin.RequestsHolder.getRequestsList() );
            }
        });
        JButton notificationsButton = new JButton("List of Your Added Materials");
        notificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewMaterialWindow viewMaterialWindow = new ViewMaterialWindow((TreeSet) ((Staff) IMDB.user).getAdded(), 0);
            }
        });
        JButton commonadminButton = new JButton("List of Admin Materials");
        commonadminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewMaterialWindow viewMaterialWindow = new ViewMaterialWindow((TreeSet) ((Staff) IMDB.admin).getAdded(), 0);
            }
        });


        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Font font1 = new Font(null, Font.BOLD, 20);
        nameLabel.setFont(font1);
        uppanel.add(nameLabel);

        rating.setAlignmentX(Component.CENTER_ALIGNMENT);
        uppanel.add(rating);

        if (IMDB.user instanceof Contributor) {
            uppanel.add(Box.createVerticalStrut(35));
        }
        else {
            uppanel.add(Box.createVerticalStrut(15));
        }

        if (IMDB.user instanceof Admin) {
            adminButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            uppanel.add(adminButton);
        }

        listFavoritesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uppanel.add(listFavoritesButton);

        uppanel.add(Box.createVerticalStrut(15));

        notificationsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uppanel.add(notificationsButton);

        if (IMDB.user instanceof Admin) {
            commonadminButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            uppanel.add(commonadminButton);
        }

        uppanel.setBackground(Color.DARK_GRAY);
        nameLabel.setForeground(Color.white);
        rating.setForeground(Color.red);

        JPanel downpanel = new JPanel();

        downpanel.setLayout(new BoxLayout(downpanel, BoxLayout.Y_AXIS));

        JButton createMaterial = new JButton("Create Material");
        createMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateMaterialWindow createMaterial1 = new CreateMaterialWindow();
            }
        });
        JButton removeMaterial = new JButton("Remove Material");
        removeMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewMaterialWindow viewMaterialWindow = new ViewMaterialWindow((TreeSet) ((Staff) IMDB.user).getAdded(), 1);
            }
        });
        JButton editMaterial = new JButton("Edit Material");
        editMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditMaterialWindow editMaterial1 = new EditMaterialWindow((TreeSet) ((Staff) IMDB.user).getAdded());
            }
        });

        JButton removeMaterial1 = new JButton("Remove Admin Material");
        removeMaterial1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewMaterialWindow viewMaterialWindow = new ViewMaterialWindow((TreeSet) ((Staff) IMDB.admin).getAdded(), 2);
            }
        });
        JButton editMaterial1 = new JButton("Edit Admin Material");
        editMaterial1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditMaterialWindow editMaterial1 = new EditMaterialWindow((TreeSet) ((Staff) IMDB.admin).getAdded());
            }
        });

        downpanel.add(Box.createVerticalStrut(15));

        createMaterial.setAlignmentX(Component.CENTER_ALIGNMENT);
        downpanel.add(createMaterial);

        removeMaterial.setAlignmentX(Component.CENTER_ALIGNMENT);
        downpanel.add(removeMaterial);

        editMaterial.setAlignmentX(Component.CENTER_ALIGNMENT);
        downpanel.add(editMaterial);

        if (IMDB.user instanceof Admin) {
            downpanel.add(Box.createVerticalStrut(15));

            removeMaterial1.setAlignmentX(Component.CENTER_ALIGNMENT);
            downpanel.add(removeMaterial1);

            editMaterial1.setAlignmentX(Component.CENTER_ALIGNMENT);
            downpanel.add(editMaterial1);
        }

        centerPanel.add(uppanel);
        centerPanel.add(downpanel);
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