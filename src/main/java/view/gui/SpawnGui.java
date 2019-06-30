package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

public class SpawnGui extends JFrame {
    private int position;

    public SpawnGui(ArrayList<String> powerUps, MapGui mapGui){
        this.setLayout(new FlowLayout());

        position = 0;
        for (String s: powerUps){
            JLabel label = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "powerups" + File.separatorChar + s + ".png"));
            label.addMouseListener(new MouseListener() {
                int pos = position;
                @Override
                public void mouseClicked(MouseEvent e) {
                    mapGui.sendSpawnMessage(pos);
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
            position++;
        }
        this.setVisible(true);
    }
}
