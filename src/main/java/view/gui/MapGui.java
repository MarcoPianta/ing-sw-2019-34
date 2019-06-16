package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapGui extends JFrame{

    private static final double RATIO = 1.320020;
    private JFrame frame;
    private JLayeredPane layeredPane;
    private JLabel map;

    public MapGui(){
        frame = new JFrame("Prova");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon mapImage = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map1.png");
        map = new JLabel(mapImage);

        addRedCross(new ArrayList<>());

        map.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                new Thread(() -> {
                    ImageIcon im = new ImageIcon(mapImage.getImage().getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT));
                    map.setIcon(im);
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

        frame.add(map);

        map.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(map.getWidth());
                System.out.println("Prediction :" + ((411*map.getWidth())/2545) + " actual : " + e.getX());
                System.out.println("Prediction :" + ((399*map.getHeight())/1928) + " actual : " + e.getY());
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

        frame.pack();
        frame.setVisible(true);
    }

    public void addRedCross(List<String> id){
        JButton bottone = new JButton();
        bottone.setBackground(Color.GREEN);
        bottone.setBounds((ViewMap.getXCoordinates(ViewMap.getIds()[0]) * map.getWidth() / 2545), (ViewMap.getYCoordinates(ViewMap.getIds()[0]) * map.getHeight() / 1928), (100 * map.getWidth() / 2545), (100 * map.getHeight() / 1928));
        map.add(bottone);

        ImageIcon redCross = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "redCross.png");
        ArrayList<JButton> button = new ArrayList<>();
        button.add(bottone);
        for(int i = 1; i< ViewMap.getIds().length; i++) {
            button.add(new JButton());
            map.add(button.get(i));
            button.get(i).setBounds((ViewMap.getXCoordinates(ViewMap.getIds()[i]) * map.getWidth() / 2545), (ViewMap.getYCoordinates(ViewMap.getIds()[i]) * map.getHeight() / 1928), (500 * map.getWidth() / 2545), (440 * map.getHeight() / 1928));
            button.get(i).setBorderPainted(false);
            button.get(i).setOpaque(false);
            button.get(i).setIcon(redCross);
        }

        map.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                new Thread(() -> {
                    bottone.setBounds((ViewMap.getXCoordinates(ViewMap.getIds()[0]) * map.getWidth() / 2545), (ViewMap.getYCoordinates(ViewMap.getIds()[0]) * map.getHeight() / 1928), (100 * map.getWidth() / 2545), (100 * map.getHeight() / 1928));
                    for(int i = 1; i< ViewMap.getIds().length; i++) {
                        button.get(i).setBounds((ViewMap.getXCoordinates(ViewMap.getIds()[i]) * map.getWidth() / 2545), (ViewMap.getYCoordinates(ViewMap.getIds()[i]) * map.getHeight() / 1928), (315 * map.getWidth() / 2545), (315 * map.getHeight() / 1928));
                        ImageIcon im = new ImageIcon(redCross.getImage().getScaledInstance(button.get(i).getWidth(), button.get(i).getHeight(), Image.SCALE_DEFAULT));
                        button.get(i).setIcon(im);
                    }
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

        for (String s: id){}
    }

    public static void main(String[] args) {
        MainGuiView.setUIManager();

        MapGui mapGui = new MapGui();
    }
}
