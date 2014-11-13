package views;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;

public class ActorCellRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object obj,
            int ind, boolean isSelected, boolean hasFocus) {
        JLabel label = new JLabel();

        models.Actor actor = (models.Actor) obj;
        Color fg = list.getForeground();
        Color bg = list.getBackground();
        if (isSelected) {
            fg = list.getSelectionForeground();
            bg = list.getSelectionBackground();
        }
        label.setText(actor.getName());

        // make label height larger
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        label.setBackground(bg);
        label.setForeground(fg);
        label.setOpaque(true);

        return label;
    }
}
