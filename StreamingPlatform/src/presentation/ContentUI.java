package presentation;

import javax.swing.*;
import java.awt.*;

public class ContentUI extends JPanel {
    JPanel createMainPanel(){
        JPanel panel = new JPanel();

        // TODO Load media into this view

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // TODO layout needs changing
        panel.setBackground(Color.darkGray);

        return panel;
    }
}
