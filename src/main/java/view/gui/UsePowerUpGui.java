package view.gui;

import Model.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

/**
 * This class is used to display a window for choose powerup to use
 */
public class UsePowerUpGui extends JFrame {

    public UsePowerUpGui(ArrayList<String> powerUps , MapGui mapGui, boolean tagBack, boolean scoop, Colors colors){
        this.setLayout(new FlowLayout());
        int i = 0;
        for (String s: powerUps){
            JLabel label = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "powerups" + File.separatorChar + s + ".png"));
            int index = i;
            label.addMouseListener(new MouseListener() {
                int choose = index;
                @Override
                public void mouseClicked(MouseEvent e) {
                    mapGui.usePowerUp(choose, tagBack, scoop, colors);
                    dispose();
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
        this.pack();
        this.setVisible(true);
    }
}
