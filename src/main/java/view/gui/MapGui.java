package view.gui;

import Model.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static view.gui.MainGuiView.GOLDENRATIO;

public class MapGui extends JFrame{
    private static final double RATIO = 1.320020;
    private JPanel mainPanel;
    private JPanel playerBoards;
    private JLabel map;
    private Colors myColor;
    private JLabel[] players = new JLabel[5];
    private ArrayList<JButton> redCrosses;

    public MapGui(Colors myColor){
        super("Adrenaline");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.myColor = myColor;
        this.redCrosses = new ArrayList<>();

        mainPanel = new JPanel(new BorderLayout());
        playerBoards = new JPanel(new FlowLayout());
        playerBoards.setSize(this.getWidth(), this.getHeight()*15/100);
        playerBoards.setBackground(new Color(0,0,0));

        ImageIcon mapImage = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map1.png");
        map = new JLabel(mapImage);

        ArrayList<String> ids = new ArrayList<>();
        ids.add("1,0");
        addRedCross(ids);

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

        mainPanel.add(map, BorderLayout.CENTER);

        ImageIcon playerBoard = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + myColor.getAbbreviation() + ".png");
        JLabel player = new JLabel(playerBoard);

        player.setSize(this.getWidth(), this.getHeight()*20/100);

        mainPanel.add(player, BorderLayout.PAGE_END);

        for (int i = 0; i < 5; i++){
            ImageIcon playerBoardIm;
            if (!Colors.values()[i].getAbbreviation().equals(myColor.getAbbreviation())) {
                playerBoardIm = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + Colors.values()[i].getAbbreviation() + ".png");
                System.out.println("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + Colors.values()[i].getAbbreviation() + ".png");
                players[i] = new JLabel(playerBoardIm);
                players[i].setSize(playerBoards.getWidth()*24/100, playerBoards.getHeight());
                playerBoards.add(players[i]);
            }
        }
        mainPanel.add(playerBoards, BorderLayout.PAGE_START);

        mainPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                playerBoards.setSize(mainPanel.getWidth(), mainPanel.getHeight()*15/100);
                player.setSize(mainPanel.getWidth(), mainPanel.getHeight()*20/100);
                new Thread(() -> {
                    ImageIcon im = new ImageIcon(playerBoard.getImage().getScaledInstance(player.getWidth(), player.getHeight(), Image.SCALE_DEFAULT));
                    player.setIcon(im);
                }).start();
                new Thread(() -> {
                    for (int i = 0; i < 5; i++){
                        int a = i;
                        new Thread(() -> {
                            System.out.println(a);
                            players[a].setSize(playerBoards.getWidth()*24/100, playerBoards.getHeight());
                            ImageIcon playerBoardIm = new ImageIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + Colors.values()[a].getAbbreviation() + ".png").getImage().getScaledInstance(players[a].getWidth(), players[a].getHeight(), Image.SCALE_DEFAULT));
                            players[a].setIcon(playerBoardIm);
                        }).start();
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

        this.add(mainPanel);

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

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                int frameHeight = new Double(getWidth() *0.7584579).intValue();
                setSize(new Dimension(getWidth(), frameHeight));
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

        int frameHeight = new Double(600 *0.7584579).intValue();
        this.setMinimumSize(new Dimension(600, frameHeight));
        this.setVisible(true);
    }

    public void addRedCross(List<String> id){
        /*
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
        });*/

        ImageIcon redCross = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "redCross.png");

        for (String s: id){
            JButton button = new JButton(redCross);
            button.setName(s);
            button.setBounds((ViewMap.getXCoordinates(ViewMap.getIds()[Character.getNumericValue(s.charAt(0))]) * map.getWidth() / 2545), (ViewMap.getYCoordinates(ViewMap.getIds()[Character.getNumericValue(s.charAt(2))]) * map.getHeight() / 1928), (500 * map.getWidth() / 2545), (440 * map.getHeight() / 1928));
            button.setBorderPainted(false);
            button.setOpaque(false);
            redCrosses.add(button);
        }

        map.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                new Thread(() -> {
                    for(JButton button: redCrosses) {
                        button.setBounds((ViewMap.getXCoordinates(ViewMap.getIds()[Character.getNumericValue(button.getName().charAt(0))]) * map.getWidth() / 2545), (ViewMap.getYCoordinates(ViewMap.getIds()[Character.getNumericValue(button.getName().charAt(2))]) * map.getHeight() / 1928), (500 * map.getWidth() / 2545), (440 * map.getHeight() / 1928));
                        ImageIcon im = new ImageIcon(redCross.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_DEFAULT));
                        button.setIcon(im);
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
    }

    public static void main(String[] args) {
        MainGuiView.setUIManager();

        MapGui mapGui = new MapGui(Colors.GREEN);
    }
}
