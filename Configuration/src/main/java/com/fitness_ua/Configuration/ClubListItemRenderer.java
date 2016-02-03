package com.fitness_ua.Configuration;

import javax.swing.*;
import java.awt.*;

/**
 * Created by salterok on 02.03.2015.
 */
public class ClubListItemRenderer extends JLabel implements ListCellRenderer {
    private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

    public ClubListItemRenderer() {
        setOpaque(true);
        setIconTextGap(12);
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        ClubStuffDescription entry = (ClubStuffDescription) value;
        setText(entry.title);
        if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.white);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }
        return this;
    }
}