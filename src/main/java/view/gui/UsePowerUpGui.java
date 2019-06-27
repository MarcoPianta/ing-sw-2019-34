package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

public class UsePowerUpGui extends JFrame {

    public UsePowerUpGui(ArrayList<String> powerUps , MapGui mapGui, boolean tagBack){
        this.setLayout(new FlowLayout());
        int i = 0;
        for (String s: powerUps){
            JLabel label = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "powerups" + File.separatorChar + s + ".png"));
            int index = i;
            label.addMouseListener(new MouseListener() {
                int choose = index;
                @Override
                public void mouseClicked(MouseEvent e) {
                    mapGui.usePowerUp(choose, tagBack);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            this.add(label);

            i++;
        }
        this.setVisible(true);
    }
}