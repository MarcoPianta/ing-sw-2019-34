package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * This class is used to display a window for spawn
 */
public class SpawnGui extends JFrame {
    private int position;

    public SpawnGui(ArrayList<String> powerUps, MapGui mapGui){
        super("Spawn");
        this.setLayout(new FlowLayout());

        position = 0;
        for (String s: powerUps){
            JLabel label = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("GUI/powerups/" + s + ".png")));

            label.addMouseListener(new MouseListener() {
                int pos = position;
                @Override
                public void mouseClicked(MouseEvent e) {
                    mapGui.sendSpawnMessage(pos);
                    System.out.println(pos);
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
            position++;
            this.add(label);
        }
        this.pack();
        this.setVisible(true);
    }
}
