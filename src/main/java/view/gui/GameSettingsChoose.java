package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

public class GameSettingsChoose extends JFrame{
    private JFrame frame;
    private JPanel mainPanel;
    JLabel[] mapLabels;

    public GameSettingsChoose(){
        JPanel maps = new JPanel(new GridLayout(4, 5));
        mapLabels = new JLabel[20];

        frame = new JFrame("Game settings");

        mainPanel = new JPanel(new BorderLayout());

        for (int i = 0; i < 20; i++) {
            if (i == 6 || i == 8 || i == 16|| i == 18) {
                mapLabels[i] = new JLabel();
                ImageIcon map = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map" + (i + 1) + ".png");
                mapLabels[i].setIcon(map);
            }
            else
                mapLabels[i] = new JLabel();
            maps.add(mapLabels[i]);
        }

        mainPanel.add(maps, BorderLayout.CENTER);
        mainPanel.addComponentListener(new ResizeMaps());

        frame.add(mainPanel);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    class ResizeMaps implements ComponentListener{

        @Override
        public void componentResized(ComponentEvent e) {
            new Thread(() -> {
                ImageIcon map = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map1.png");
                ImageIcon im = new ImageIcon(map.getImage().getScaledInstance(mapLabels[6].getWidth(), mapLabels[6].getHeight(), Image.SCALE_DEFAULT));
                mapLabels[6].setIcon(im);
            }).start();

            new Thread(() -> {
                ImageIcon map = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map2.png");
                ImageIcon im = new ImageIcon(map.getImage().getScaledInstance(mapLabels[6].getWidth(), mapLabels[6].getHeight(), Image.SCALE_DEFAULT));
                mapLabels[8].setIcon(im);
            }).start();

            new Thread(() -> {
                ImageIcon map = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map3.png");
                ImageIcon im = new ImageIcon(map.getImage().getScaledInstance(mapLabels[6].getWidth(), mapLabels[6].getHeight(), Image.SCALE_DEFAULT));
                mapLabels[16].setIcon(im);
            }).start();

            new Thread(() -> {
                ImageIcon map = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map4.png");
                ImageIcon im = new ImageIcon(map.getImage().getScaledInstance(mapLabels[6].getWidth(), mapLabels[6].getHeight(), Image.SCALE_DEFAULT));
                mapLabels[18].setIcon(im);
            }).start();
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }
}