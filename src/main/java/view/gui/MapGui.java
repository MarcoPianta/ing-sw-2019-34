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
import java.util.concurrent.ExecutionException;

import static view.gui.MainGuiView.GOLDENRATIO;

public class MapGui extends JFrame{
    private static final double RATIO = 1.320020;
    private static final int PLAYERBOARDSPERCENTAGE = 40;
    private JPanel mainPanel;
    private JPanel playerBoards;
    private JLabel map;
    private Colors myColor;
    private JLabel[] players = new JLabel[4];
    private JLabel player;
    private ArrayList<JButton> redCrosses;
    private ImageIcon playerBoard;
    private ImageIcon redCross;
    private ImageIcon mapImage;

    private static final Object lock = new Object();

    public MapGui(Colors myColor){
        super("Adrenaline");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.myColor = myColor;
        this.redCrosses = new ArrayList<>();
        playerBoard = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + myColor.getAbbreviation() + ".png");
        redCross = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "redCross.png");
        mapImage = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map1.png");


        mainPanel = new JPanel(new BorderLayout());
        playerBoards = new JPanel(new GridLayout(4,1));
        playerBoards.setSize(this.getWidth()*PLAYERBOARDSPERCENTAGE/100, this.getHeight());
        playerBoards.setBackground(new Color(0,0,0));

        map = new JLabel(mapImage);

        mainPanel.add(map, BorderLayout.CENTER);
        mainPanel.setSize(this.getWidth(), this.getHeight()*65/100);

        player = new JLabel(playerBoard);
        player.setSize(this.getWidth(), this.getHeight()*20/100);

        mainPanel.add(player, BorderLayout.PAGE_END);

        int index = 0;
        for (int i = 0; i < 5; i++){
            ImageIcon playerBoardIm;
            if (!Colors.values()[i].getAbbreviation().equals(myColor.getAbbreviation())) {
                playerBoardIm = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + Colors.values()[i].getAbbreviation() + ".png");
                players[index] = new JLabel(playerBoardIm);
                players[index].setSize(playerBoards.getWidth(), playerBoards.getHeight()*24/100);
                playerBoards.add(players[index]);
                index++;
            }
        }
        mainPanel.add(playerBoards, BorderLayout.LINE_END);

        addComponentListeners();

        addRedCross(Arrays.asList(ViewMap.getIds()));

        this.add(mainPanel);

        int frameWidth = new Double(600*RATIO).intValue();
        setSize(new Dimension(frameWidth, 600));
        this.setVisible(true);
    }

    public void addRedCross(List<String> id){
        for (String s: id){
            JButton button = new JButton();
            button.setName(s);
            button.setBounds((ViewMap.getXCoordinates(s) * map.getWidth() / 2545), (ViewMap.getYCoordinates(s) * map.getHeight() / 1928), (500 * map.getWidth() / 2545), (440 * map.getHeight() / 1928));
            button.setBorderPainted(false);
            button.setOpaque(false);
            button.setIcon(redCross);
            redCrosses.add(button);
            map.add(button);
        }
    }

    private void addComponentListeners(){
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                new SwingWorker<Void, Void>(){
                    @Override
                    protected Void doInBackground() {
                        int frameWidth = new Double(getHeight() *RATIO).intValue();
                        setSize(new Dimension(frameWidth, getHeight()));
                        return null;
                    }
                }.execute();
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

        mainPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                synchronized (lock) {
                    while (playerBoards.getWidth() == mainPanel.getWidth()) {
                        try {lock.wait();}catch (InterruptedException i) {}
                    }
                    playerBoards.setSize(mainPanel.getWidth() * PLAYERBOARDSPERCENTAGE / 100, mainPanel.getHeight());
                    player.setSize(mainPanel.getWidth(), mainPanel.getHeight() * 20 / 100);
                    lock.notifyAll();
                }
                new Thread(() -> {
                    synchronized (lock) {
                        ImageIcon im = new ImageIcon(playerBoard.getImage().getScaledInstance(player.getWidth(), player.getHeight(), Image.SCALE_DEFAULT));
                        player.setIcon(im);
                        lock.notifyAll();
                    }
                }).start();
                new Thread(() -> {
                    synchronized (lock) {
                        for (int i = 0; i < 4; i++) {
                            int a = i;
                            new Thread(() -> {
                                players[a].setSize(playerBoards.getWidth(), playerBoards.getHeight() * 24 / 100);
                                ImageIcon playerBoardIm = new ImageIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + Colors.values()[a].getAbbreviation() + ".png").getImage().getScaledInstance(players[a].getWidth(), players[a].getHeight(), Image.SCALE_DEFAULT));
                                players[a].setIcon(playerBoardIm);
                            }).start();
                        }
                        lock.notifyAll();
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
    }

    public static void main(String[] args) {
        MainGuiView.setUIManager();

        new MapGui(Colors.GREEN);
    }
}
