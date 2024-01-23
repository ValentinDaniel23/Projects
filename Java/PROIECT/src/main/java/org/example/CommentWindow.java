package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CommentWindow extends JFrame {
    private JTextArea commentTextArea;

    public CommentWindow(Production production) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                java.awt.Window win[] = java.awt.Window.getWindows();
                for(int i=0;i<win.length;i++){
                    win[i].dispose();
                    win[i]=null;
                }
                if (production instanceof Series) {
                    SeriesWindow seriesWindow = new SeriesWindow(((Series) production).getTitle());
                }
                else {
                    MovieWindow movieWindow = new MovieWindow(((Movie) production).getTitle());
                }
            }
        });

        setTitle("IMDB Comment");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("IMDB");

        headerPanel.add(headerLabel);

        headerPanel.setBackground(Color.BLACK);
        Font font = new Font(null, Font.BOLD, 20);
        headerLabel.setFont(font);
        headerLabel.setForeground(Color.YELLOW);

        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JPanel ratingPanel = new JPanel(new FlowLayout());
        JSpinner ratingSpinner = createSmallSpinner();
        ratingPanel.add(new JLabel("Rating:"));
        ratingPanel.add(ratingSpinner);

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        commentTextArea = new JTextArea();
        commentTextArea.setEditable(true);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane commentScrollPane = new JScrollPane(commentTextArea);
        commentPanel.add(new JLabel("Comment:"));
        commentPanel.add(commentScrollPane);

        centerPanel.add(ratingPanel);
        centerPanel.add(commentPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        add(leftPanel, BorderLayout.EAST);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);
        add(rightPanel, BorderLayout.WEST);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentTextArea.getText();
                int rating = (int) ratingSpinner.getValue();
                Rating rating1 = new Rating(IMDB.user.getUsername(), rating, comment);
                boolean found = production.addRating(rating1);

                if (found) {
                    JOptionPane.showMessageDialog(CommentWindow.this,
                            "Commented successfully",
                            "Comment",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(CommentWindow.this,
                            "Already commented",
                            "Comment",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean found = production.removeRating(IMDB.user.getUsername());

                if (found) {
                    JOptionPane.showMessageDialog(CommentWindow.this,
                            "Removed successfully",
                            "Comment",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(CommentWindow.this,
                            "Already removed",
                            "Comment",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        JPanel downPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.add(removeButton);
        downPanel.add(sendButton);
        downPanel.setBackground(Color.BLACK);
        add(downPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JSpinner createSmallSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 10, 1);
        JSpinner ratingSpinner = new JSpinner(model);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) ratingSpinner.getEditor();

        editor.getTextField().setFont(new Font("Arial", Font.PLAIN, 12));
        Dimension preferredSize = editor.getTextField().getPreferredSize();
        preferredSize = new Dimension(60, (int) preferredSize.getHeight());
        editor.getTextField().setPreferredSize(preferredSize);

        return ratingSpinner;
    }
}