package views;

import models.Movie;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;

public class MovieCellRenderer implements ListCellRenderer {

    public java.util.Set<models.Movie> actorMovies = null;

    @Override
    public Component getListCellRendererComponent(JList list, Object obj,
            int ind, boolean isSelected, boolean hasFocus) {

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        panel.setBackground(Color.white);
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        Movie movie = (Movie) obj;

        JLabel title = new JLabel(movie.getTitle());
        JLabel year = new JLabel(String.valueOf(movie.getYear()));

        panel.add(title);
        panel.add(Box.createHorizontalGlue()); // horiz. fill
        panel.add(year);

        if (isSelected) {
            panel.setBackground(Color.decode("#cc9933"));
            title.setForeground(Color.white);
            year.setForeground(Color.yellow);
        }

        return panel;
    }
}
