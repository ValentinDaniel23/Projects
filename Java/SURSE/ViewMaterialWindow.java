package org.example;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ViewMaterialWindow extends JFrame {
    public ViewMaterialWindow(TreeSet<Object> mmaterials, int type) {
        setTitle("IMDB Materials");
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


        DefaultListModel<String> materials;
        List<Object> elements = new ArrayList<>();
        JList<String> materialsList;

        materials = new DefaultListModel<>();

        for (Object object : mmaterials) {
            if (object instanceof Actor) {
                Actor actor = (Actor) object;
                materials.addElement(actor.getActorName() + " : Actor");
                elements.add(actor);
            }
            if (object instanceof Movie) {
                Movie movie = (Movie) object;
                materials.addElement(movie.getTitle() + " : Movie");
                elements.add(movie);
            }
            if (object instanceof Series) {
                Series series = (Series) object;
                materials.addElement(series.getTitle() + " : Series");
                elements.add(series);
            }
        }

        materialsList = new JList<>(materials);

        materialsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    if (type == 0) {
                        int selectedIndex = materialsList.getSelectedIndex();
                        if (selectedIndex != -1) {
                            java.awt.Window win[] = java.awt.Window.getWindows();
                            for(int i=0;i<win.length;i++){
                                win[i].dispose();
                                win[i]=null;
                            }
                            if (elements.get(selectedIndex) instanceof Actor) {
                                Actor actor = (Actor) elements.get(selectedIndex);
                                ActorWindow actorWindow = new ActorWindow(actor.getActorName());
                            }
                            if (elements.get(selectedIndex) instanceof Movie) {
                                Movie movie = (Movie) elements.get(selectedIndex);
                                MovieWindow movieWindow = new MovieWindow(movie.getTitle());
                            }
                            if (elements.get(selectedIndex) instanceof Series) {
                                Series series = (Series) elements.get(selectedIndex);
                                SeriesWindow seriesWindow = new SeriesWindow(series.getTitle());
                            }
                        }
                    }
                    else if (type == 1) {
                        int selectedIndex = materialsList.getSelectedIndex();
                        if (selectedIndex != -1) {
                            if (elements.get(selectedIndex) instanceof Actor) {
                                Actor actor = (Actor) elements.get(selectedIndex);
                                ((Staff) IMDB.user).removeActorSystem(actor.getActorName());
                            }
                            if (elements.get(selectedIndex) instanceof Movie) {
                                Movie movie = (Movie) elements.get(selectedIndex);
                                ((Staff) IMDB.user).removeProductionSystem(movie.getTitle());
                            }
                            if (elements.get(selectedIndex) instanceof Series) {
                                Series series = (Series) elements.get(selectedIndex);
                                ((Staff) IMDB.user).removeProductionSystem(series.getTitle());
                            }
                            JOptionPane.showMessageDialog(ViewMaterialWindow.this,
                                "Material deleted",
                                "Successful",
                                JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            ViewMaterialWindow viewMaterialWindow = new ViewMaterialWindow(mmaterials, type);
                        }
                    }
                    else {
                        int selectedIndex = materialsList.getSelectedIndex();
                        if (selectedIndex != -1) {
                            if (elements.get(selectedIndex) instanceof Actor) {
                                Actor actor = (Actor) elements.get(selectedIndex);
                                ((Staff) IMDB.admin).removeActorSystem(actor.getActorName());
                            }
                            if (elements.get(selectedIndex) instanceof Movie) {
                                Movie movie = (Movie) elements.get(selectedIndex);
                                ((Staff) IMDB.admin).removeProductionSystem(movie.getTitle());
                            }
                            if (elements.get(selectedIndex) instanceof Series) {
                                Series series = (Series) elements.get(selectedIndex);
                                ((Staff) IMDB.admin).removeProductionSystem(series.getTitle());
                            }
                            JOptionPane.showMessageDialog(ViewMaterialWindow.this,
                                    "Material deleted",
                                    "Successful",
                                    JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            ViewMaterialWindow viewMaterialWindow = new ViewMaterialWindow(mmaterials, type);
                        }
                    }
            }
        });

        JScrollPane favoritesScrollPane = new JScrollPane(materialsList);

        favoritesScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(favoritesScrollPane);

        setVisible(true);
    }
}