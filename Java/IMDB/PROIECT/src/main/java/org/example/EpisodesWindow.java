package org.example;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class EpisodesWindow extends JFrame {
    public EpisodesWindow(Map<String, List<Episode> > seasons, int numSeasons) {
        setTitle("IMDB Episodes");
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


        JScrollPane scrollPane = new JScrollPane();
        JPanel episodesPanel = new JPanel();
        episodesPanel.setLayout(new BoxLayout(episodesPanel, BoxLayout.Y_AXIS));

        for (int i = 1; i <= numSeasons; i++) {
            String season = "Season " + i;
            List<Episode> episodes = seasons.get(season);
            StringBuilder htmlText = new StringBuilder("<html>");
            htmlText.append("<b>").append(season + ":").append("</b><br>");
            for (Episode episode : episodes) {
                htmlText.append(episode.getEpisodeName() + " : " + episode.getDuration()).append("<br>");
            }
            htmlText.append("</html>");

            JLabel seasonLabel = new JLabel(htmlText.toString());
            seasonLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
            episodesPanel.add(seasonLabel);
        }

        scrollPane.setViewportView(episodesPanel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}