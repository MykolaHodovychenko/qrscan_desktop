package com.fitness_ua.Configuration;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by salterok on 03.03.2015.
 */
public class PropertyRenderer extends JPanel implements ListCellRenderer {
    private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);
    private Map.Entry<String, String> option;

    private JLabel label;
    private JTextField field;


    public PropertyRenderer() {
        super(new GridLayout());
        SpringLayout.Constraints cc = new SpringLayout.Constraints();
        Spring spring = cc.getConstraint(SpringLayout.WEST);
        cc.setConstraint(SpringLayout.WEST, spring);
        label = new JLabel();
        add(label, cc);
        field = new JTextField();
        field.setEditable(true);
        spring = cc.getConstraint(SpringLayout.EAST);
        cc.setConstraint(SpringLayout.EAST, spring);
        add(field, cc);


        setOpaque(true);
//        setIconTextGap(12);
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        option = (Map.Entry<String, String>)value;

        label.setText(option.getKey());
        field.setText(option.getValue());


//        if (isSelected) {
//            setBackground(HIGHLIGHT_COLOR);
//            setForeground(Color.white);
//        } else {
//            setBackground(Color.white);
//            setForeground(Color.black);
//        }
        return this;
    }
}