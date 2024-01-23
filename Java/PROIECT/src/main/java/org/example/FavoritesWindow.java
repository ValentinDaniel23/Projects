package org.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class FavoritesWindow extends JFrame {
    public FavoritesWindow() {
        setTitle("IMDB Favorites");
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


        DefaultListModel<String> favorites;
        List<String> names = new ArrayList<>();
        List<String> types = new ArrayList<>();
        JList<String> favoritesList;

        favorites = new DefaultListModel<>();

        for (Object object : IMDB.user.getFavorites()) {
            if (object instanceof Actor) {
                Actor actor = (Actor) object;
                favorites.addElement(actor.getActorName() + ": " + "Actor");
                names.add(actor.getActorName());
                types.add("Actor");
            }
            else {
                Production production = (Production) object;
                if (production instanceof Movie) {
                    favorites.addElement(((Movie) production).getTitle() + ": " + "Movie");
                    names.add(((Movie) production).getTitle());
                    types.add("Movie");
                }
                else {
                    favorites.addElement(((Series) production).getTitle() + ": " + "Series");
                    names.add(((Series) production).getTitle());
                    types.add("Series");
                }
            }
        }

        favoritesList = new JList<>(favorites);

        favoritesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = favoritesList.getSelectedIndex();
                    if (selectedIndex != -1) {

                        String ttype = types.get(selectedIndex);
                        String nname = names.get(selectedIndex);
                        if (ttype.equals("Actor")) {
                            Actor actor = IMDB.findActor(nname);
                            IMDB.user.removeFromFavorites(actor);
                        }
                        if (ttype.equals("Movie") || ttype.equals("Series")) {
                            Production production = IMDB.findProduction(nname);
                            IMDB.user.removeFromFavorites(production);
                        }

                        dispose();
                        FavoritesWindow favoritesWindow = new FavoritesWindow();
                    }
                }
            }
        });

        JScrollPane favoritesScrollPane = new JScrollPane(favoritesList);

        favoritesScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(favoritesScrollPane);

        setVisible(true);
    }
}