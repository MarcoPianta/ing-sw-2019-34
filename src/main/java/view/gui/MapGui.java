package view.gui;

import Model.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapGui extends JFrame{
    private static final double RATIO = 1.320020;
    private static final int PLAYERBOARDSPERCENTAGE = 39;
    private static final int MAPWIDTHPERCENTAGE = 60;
    private static final int MAPEIGTHPERCENTAGE = 79;
    private static final int PLAYERBOARDHEIGTHERCENTAGE = 19;
    private static final int ENEMYHEIGTHPERCENTAGE = 24;

    private JPanel playerBoards;
    private JLabel map;
    private Colors myColor;
    private JLabel[] players = new JLabel[4];
    private JLabel player;
    private JScrollPane chatPane;
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
        this.setLayout(new GridBagLayout());

        playerBoard = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + myColor.getAbbreviation() + ".png");
        redCross = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "redCross.png");
        mapImage = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map1.png");

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.6;
        c.weighty = 0.4;
        c.insets = new Insets(2,2,2,2);
        c.fill = GridBagConstraints.BOTH;
        map = new JLabel(mapImage);
        this.add(map, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.4;
        c.weighty = 0.4;
        c.insets = new Insets(2,2,2,2);
        c.fill = GridBagConstraints.BOTH;
        playerBoards = new JPanel(new GridLayout(4, 1));
        int index = 0;
        for (int i = 0; i < 5; i++){
            ImageIcon playerBoardIm;
            if (!Colors.values()[i].getAbbreviation().equals(myColor.getAbbreviation())) {
                playerBoardIm = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + Colors.values()[i].getAbbreviation() + ".png");
                players[index] = new JLabel(playerBoardIm);
                players[index].setName(Colors.values()[i].getAbbreviation());
                playerBoards.add(players[index]);
                index++;
            }
        }
        this.add(playerBoards, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.6;
        c.weighty = 0.5;
        c.gridwidth = 1;
        c.insets = new Insets(2,2,2,2);
        c.fill = GridBagConstraints.BOTH;
        player = new JLabel(playerBoard);
        this.add(player, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.4;
        c.weighty = 0.5;
        c.gridwidth = 1;
        c.insets = new Insets(2,2,2,2);
        c.fill = GridBagConstraints.BOTH;
        JPanel chat = new JPanel(new BorderLayout());
        JLabel chatLabel = new JLabel("This is the chat");
        chatLabel.setHorizontalTextPosition(JLabel.LEFT);
        chatLabel.setVerticalTextPosition(JLabel.BOTTOM);
        chatLabel.setForeground(Color.WHITE);
        chatLabel.setOpaque(false);
        JTextField text = new JTextField();

        //chat.add(chatLabel, BorderLayout.CENTER);
        //chat.add(text, BorderLayout.SOUTH);
        chatPane = new JScrollPane(chatLabel);
        chatPane.getViewport().setBackground(Color.BLACK);
        this.add(chatPane, c);


        addComponentListeners();

        //this.add(mainPanel);
        this.pack();
        this.setVisible(true);
        int frameWidth = new Double(600*RATIO).intValue();
        this.setMinimumSize(new Dimension(frameWidth, 600));
    }

    public void addRedCross(List<String> id){
        for (String s: id){
            new Thread(() -> {
                JButton button = new JButton();
                button.setName(s);
                button.setBorderPainted(false);
                button.setOpaque(false);
                button.setIcon(redCross);
                synchronized (lock) {
                    button.setBounds((ViewMap.getXCoordinates(s) * map.getWidth() / 2545), (ViewMap.getYCoordinates(s) * map.getHeight() / 1928), (500 * map.getWidth() / 2545), (440 * map.getHeight() / 1928));
                    redCrosses.add(button);
                    map.add(button);
                }
            }).start();
        }
    }

    public void weaponChosen(String choose){
        System.out.println(choose);
    }

    private void addComponentListeners(){
        MapGui self = this;

        map.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JLabel label = (JLabel) e.getComponent();
                resizeImage(label, mapImage);
            }
        });

        playerBoards.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (int i= 0; i < 4; i++) {
                    int a = i;
                    new Thread(() ->
                            resizeImage(players[a], new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + players[a].getName() + ".png"))
                    ).start();
                }
            }
        });

        player.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JLabel label = (JLabel) e.getComponent();
                resizeImage(label, playerBoard);
            }
        });

        map.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JLabel label = (JLabel) e.getComponent();
                Dimension size = new Dimension(label.getIcon().getIconWidth(), label.getIcon().getIconHeight());
                new Thread(() -> {
                    for(JButton button: redCrosses) {
                        button.setBounds((ViewMap.getXCoordinates(ViewMap.getIds()[Character.getNumericValue(button.getName().charAt(0))]) * size.width / 2545), (ViewMap.getYCoordinates(ViewMap.getIds()[Character.getNumericValue(button.getName().charAt(2))]) * size.height / 1928), (500 * size.width / 2545), (440 * size.height / 1928));
                        ImageIcon im = new ImageIcon(redCross.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_DEFAULT));
                        button.setIcon(im);
                    }
                }).start();
            }
        });


        map.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(map.getWidth());
                System.out.println("Prediction :" + ((411*map.getWidth())/2545) + " actual : " + e.getX());
                System.out.println("Prediction :" + ((399*map.getHeight())/1928) + " actual : " + e.getY());
                addRedCross(Arrays.asList(ViewMap.getIds()));
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

        player.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ( (e.getX() < 67 * player.getWidth()/1120) && ((e.getY() > 60 * player.getHeight()/274) && (e.getY() < (60+32) * player.getHeight()/274))){
                    System.out.println("move");
                }
                else if ( (e.getX() < 67 * player.getWidth()/1120) && ((e.getY() > 104 * player.getHeight()/274) && (e.getY() < (104+32) * player.getHeight()/274))){
                    System.out.println("grab");
                }
                else if ( (e.getX() < 67 * player.getWidth()/1120) && ((e.getY() > 147 * player.getHeight()/274) && (e.getY() < (147+32) * player.getHeight()/274))){
                    ArrayList<String> weaponsName = new ArrayList<>();
                    weaponsName.add("cyberblade");
                    weaponsName.add("electroscyte");
                    new WeaponChooseGui(weaponsName, self);
                }
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

    private void resizeImage(JLabel label, ImageIcon imageIcon){
        Dimension size = label.getSize();
        Image resized = imageIcon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
        label.setIcon(new ImageIcon(resized));
    }

    private Color getPaintingColor(){
        if (myColor.getAbbreviation().equals("blue"))
            return Color.BLUE;
        else if (myColor.getAbbreviation().equals("green"))
            return Color.GREEN;
        else if (myColor.getAbbreviation().equals("white"))
            return Color.GRAY;
        else if (myColor.getAbbreviation().equals("yellow"))
            return Color.YELLOW;
        else
            return new Color(212, 25, 255);

    }

    public static void main(String[] args) {
        MainGuiView.setUIManager();

        new MapGui(Colors.GREEN);
    }
}
