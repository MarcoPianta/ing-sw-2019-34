package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

public class MapGui extends JFrame{
    JLabel imageLabel;

    private static final double RATIO = 1.320020;

    public MapGui(){
        /*
        super("Adrenaline map");
        setSize(500, 500);
        JLayeredPane layeredPane = getLayeredPane();

        ImageIcon icon = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "homeAdrenaline.png");
        imageLabel = new JLabel(icon);
        imageLabel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                new Thread(() -> {
                    ImageIcon im = new ImageIcon(icon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_DEFAULT));
                    imageLabel.setIcon(im);
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
        });

        JButton button = new JButton();
        button.setBackground(Color.red);
        button.setBounds(60, 60, 50, 50);

        layeredPane.add(imageLabel, new Integer(0));
        //layeredPane.add(button, new Integer(100));

        super.repaint();

        this.setVisible(true);

        */
        super("LayeredPane Example");
        setSize(new Double(500*RATIO).intValue(), 500);
        JLayeredPane pane = getLayeredPane();
        //creating buttons
        JButton top = new JButton();
        top.setBackground(Color.white);
        top.setBounds(20, 20, 50, 50);

        /*
        ImageIcon map1 = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map1.png");
        ImageIcon map2 = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map2.png");
        ImageIcon map3 = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map3.png");
        ImageIcon map4 = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map4.png");
        */
        ImageIcon icon = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map4.png");

        imageLabel = new JLabel(icon);
        imageLabel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                new Thread(() -> {
                    ImageIcon im = new ImageIcon(icon.getImage().getScaledInstance(new Double(imageLabel.getWidth()*RATIO).intValue(), imageLabel.getHeight(), Image.SCALE_DEFAULT));
                    imageLabel.setIcon(im);
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
        });

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                new Thread(() -> {
                    imageLabel.setSize(getWidth(), getHeight());
                }).start();
                new Thread(()-> {
                    Rectangle b = e.getComponent().getBounds();
                    e.getComponent().setBounds(b.x, b.y, new Double(b.height * RATIO).intValue(), b.height);
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
        });
        imageLabel.setBounds(0, 0, getWidth(), getHeight());

        JButton middle = new JButton();
        middle.setBackground(Color.red);
        middle.setBounds(40, 40, 50, 50);
        JButton bottom = new JButton();
        bottom.setBackground(Color.cyan);
        bottom.setBounds(60, 60, 50, 50);
        //adding buttons on pane
        pane.add(imageLabel, new Integer(0));
        //pane.add(middle, new Integer(2));
        pane.add(bottom, new Integer(100));
        this.setVisible(true);

    }
}
