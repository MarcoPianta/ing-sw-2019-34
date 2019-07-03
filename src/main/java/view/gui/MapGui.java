package view.gui;

import Model.CardAmmo;
import Model.CardWeapon;
import Model.Colors;
import network.Client.Client;
import network.messages.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * This is the main window, it contains the map, the player's boards and the chat.
 * */
public class MapGui extends JFrame{
    private static final double RATIO = 1.320020;
    private Client client;
    private String actionType = ""; //can assume values move indicating a move action. For shot action it assumes values: room, square. For move other players it assumes value: movep. Or "null" if no action is monitored right now
    private int[] damagePosition = {110, 172, 240, 302, 364, 432, 494, 556, 618, 680, 748, 810};
    private int[] marksPosition = {535, 575, 615, 655, 695, 735, 775, 815};

    private int[] ammos = new int[3];
    private JPanel playerBoards;
    private JLabel map;
    private Colors myColor;
    private JLabel[] players = new JLabel[4];
    private JLabel player;
    private JPanel chatArea;
    private JTextField text;
    private JScrollPane chatPane;
    private File playerBoard;
    private File redCross;
    private File mapImage;
    private File[] otherPlayerBoards = new File[4];
    private BufferedImage currentPlayerBoard;
    private BufferedImage currentRedCross;
    private BufferedImage currentMapImage;
    private BufferedImage currentPlayerBoardModified;
    private BufferedImage[] currentOtherPlayerBoards = new BufferedImage[4];
    private static HashMap<Colors, Integer> enemies; //The string is the color name of the player
    private List<String> redCrosses;
    private String myPosition;
    private String[] othersPosition = {"","","",""};
    private HashMap<String, String[]> spawnSquareWeapon;
    private HashMap<String, CardAmmo> ammosOnMap;
    private boolean myTurn;
    private ArrayList<Colors> marks;
    private ArrayList<CardWeapon> cardsWeapon;
    private ArrayList<String> powerUps;
    private int selectedPowerUp;
    private Colors targetPowerup;
    private int actionNumber;
    private int points = 0;

    /**
     * This constructor creates the new windows
     * */
    public MapGui(Colors myColor, Client client, String mapImageFile){
        super("Adrenaline");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.close();
            }
        });
        this.myColor = myColor;
        this.enemies = new HashMap<>();
        this.setLayout(new GridBagLayout());
        this.client = client;
        this.redCrosses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.cardsWeapon = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.ammosOnMap = new HashMap<>();
        initializeSpawnWeapon();

        playerBoard = new File("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + myColor.getAbbreviation() + ".png");
        redCross = new File("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "redCross.png");
        mapImage = new File("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + mapImageFile +".png");
        try{
            currentPlayerBoard = ImageIO.read(playerBoard);
            currentRedCross = ImageIO.read(redCross);
            currentMapImage = ImageIO.read(mapImage);
        }catch (IOException e){}

        try {map = new JLabel(new ImageIcon(ImageIO.read(mapImage)));} catch (IOException e){}

        playerBoards = new JPanel(new GridLayout(4, 1));
        int index = 0;
        for (int i = 0; i < 5; i++){
            ImageIcon playerBoardIm;
            if (!Colors.values()[i].getAbbreviation().equals(myColor.getAbbreviation())) {
                otherPlayerBoards[index] = new File("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + Colors.values()[i].getAbbreviation() + ".png");
                try {
                    currentOtherPlayerBoards[index] = ImageIO.read(otherPlayerBoards[index]);
                } catch (IOException e){}
                players[index] = new JLabel(new ImageIcon(currentOtherPlayerBoards[index]));
                players[index].setName(Colors.values()[i].getAbbreviation());
                enemies.put(Colors.values()[i], index);
                playerBoards.add(players[index]);
                index++;
            }
        }

        try {player = new JLabel(new ImageIcon(ImageIO.read(playerBoard)));} catch (IOException e){}

        chatArea = new JPanel();
        //chatArea.setBackground(Color.black);
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.PAGE_AXIS));

        JLabel chatLabel = new JLabel("This is the chat, be kind with other players");
        chatLabel.setHorizontalTextPosition(JLabel.LEFT);
        chatLabel.setVerticalTextPosition(JLabel.BOTTOM);
        //chatLabel.setForeground(Color.WHITE);
        chatLabel.setOpaque(false);
        chatArea.add(chatLabel);
        chatPane = new JScrollPane(chatArea);
        chatPane.getVerticalScrollBar().addAdjustmentListener(e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));

        text = new JTextField();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chatPane, text);
        splitPane.setDividerLocation(0.5);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.6;
        c.weighty = 0.4;
        c.insets = new Insets(2,2,2,2);
        c.fill = GridBagConstraints.BOTH;
        this.add(map, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.4;
        c.weighty = 0.4;
        c.insets = new Insets(2,2,2,2);
        c.fill = GridBagConstraints.BOTH;
        this.add(playerBoards, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weighty = 0.5;
        c.insets = new Insets(2,2,2,2);
        c.fill = GridBagConstraints.BOTH;
        this.add(player, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.4;
        c.weighty = 0.5;
        c.insets = new Insets(2,2,2,2);
        c.fill = GridBagConstraints.BOTH;
        this.add(splitPane, c);

        addComponentListeners();

        this.pack();
        this.setVisible(true);
        int frameWidth = new Double(600*RATIO).intValue();
        this.setMinimumSize(new Dimension(frameWidth, 600));
    }

    /**
     * This method add red crosses in unreachable squares
     * */
    public void addRedCross(List<String> id){
        redCrosses = id;
        BufferedImage currentMapWithCross = cloneImage(currentMapImage);
        for (String s: id){
            Graphics2D g = currentMapWithCross.createGraphics();
            //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            BufferedImage cross = currentRedCross;
            Image crosResized = cross.getScaledInstance(350 * currentMapWithCross.getWidth() / 2545, 340 * currentMapWithCross.getHeight() / 1928, Image.SCALE_DEFAULT);
            g.drawImage(crosResized, ViewMap.getXCoordinates(s) * currentMapWithCross.getWidth() / 2545, (ViewMap.getYCoordinates(s) * currentMapWithCross.getHeight() / 1928), null);
            g.dispose();

            Image mapResized = currentMapWithCross.getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT);
            map.setIcon(new ImageIcon(mapResized));
        }
    }

    /**
     * This method add ammos on the squares
     * */
    public void addAmmoToMap(String id, CardAmmo card){
        if (ammosOnMap.containsKey(id)) ammosOnMap.replace(id, card);
        else ammosOnMap.put(id, card);
        String path;
        if (card.isWithPowerUp()) path = "cardnotonlyammo";
        else  path = "cardonlyammo";
        String cardName;
        if (card.getName().equals("back")) cardName = "back"; else cardName = card.getName();

        File file = new File("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "cardammo" + File.separatorChar + path + File.separatorChar + cardName + ".png");
        try {
            BufferedImage ammo = ImageIO.read(file);
            Graphics2D g = currentMapImage.createGraphics();
            //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            Image ammoScaledInstance = ammo.getScaledInstance(150 * currentMapImage.getWidth() / 2545, 150 * currentMapImage.getHeight() / 1928, Image.SCALE_DEFAULT);
            g.drawImage(ammoScaledInstance, ViewMap.getXCoordinates(id), ViewMap.getYCoordinates(id)+175, null);
            g.dispose();

            Image mapResized = currentMapImage.getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT);
            map.setIcon(new ImageIcon(mapResized));
        }catch (IOException e){}
    }

    /**
     * This method add a weapon to the specified spawn square at the specified position
     * */
    public void addWeaponToMap(String id, int position, String weapon){
        double rotationRequired;
        double rotationBack;
        int backWidth;
        int backHeight;
        int backX;
        int backY;
        int xOffset;
        int yOffset;
        spawnSquareWeapon.get(id)[position] = weapon;
        if (Character.getNumericValue(id.charAt(0)) == 0){
            rotationRequired = 1.0472; //60 degree in radiant
            backWidth = 237;
            backHeight = 358;
            backX = ViewMap.getxWeapon(id, position);
            backY = ViewMap.getyWeapon(id, position);
            rotationBack = 0.0;
            xOffset = 90; yOffset = 150;
        }
        else {
            rotationRequired = 0.5236; //30 degree in radiant
            backWidth = 358;
            backHeight = 237 * 2 + 100;
            backX = ViewMap.getxWeapon(id, position) - 9;
            backY = ViewMap.getyWeapon(id, position) - 350;
            rotationBack = 1.5707;
            xOffset = 65; yOffset = 100;
        }

        try {
            BufferedImage back = ImageIO.read(new File("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "weapons" + File.separatorChar + "back.png"));
            double locationXBack = 0;
            double locationYBack = back.getHeight(null);
            AffineTransform txBack = AffineTransform.getRotateInstance(rotationBack, locationXBack, locationYBack);
            AffineTransformOp opBack = new AffineTransformOp(txBack, AffineTransformOp.TYPE_BILINEAR);

            BufferedImage text = new BufferedImage(400, 240, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = text.createGraphics();
            g2d.setPaint(Color.WHITE);
            Font font = new Font("Arial", Font.BOLD, 50);
            g2d.setFont(font);
            g2d.drawString(weapon, 0, text.getHeight() / 2);
            g2d.dispose();
            double locationX = 0;
            double locationY = text.getHeight();
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

            Graphics2D g = currentMapImage.createGraphics();
            g.drawImage(opBack.filter(back, null), backX, backY, backWidth, backHeight, null);
            g.drawImage(op.filter(text, null), ViewMap.getxWeapon(id, position) - xOffset, ViewMap.getyWeapon(id, position) - yOffset, null);
            g.dispose();

            Image mapResized = currentMapImage.getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT);
            map.setIcon(new ImageIcon(mapResized));
        }catch (IOException e){}
    }

    public void setPoints(int points){
        this.points = points;
    }

    /**
     * This method add damage marks in player's playerboard
     * */
    public void addDamage(List<Colors> damageBar){
        int i = 0;
        currentPlayerBoardModified = cloneImage(currentPlayerBoard);
        for (Colors c: damageBar){
            Image imageColor = createColorMarker(c, currentPlayerBoard.getWidth(), currentPlayerBoard.getHeight());

            Graphics2D g = currentPlayerBoardModified.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g.drawImage(imageColor, damagePosition[i] * currentPlayerBoardModified.getWidth() / 1120, 120 * currentPlayerBoardModified.getHeight() / 274, null);
            g.dispose();
            Image playerBoardResized = currentPlayerBoardModified.getScaledInstance(player.getWidth(), player.getHeight(), Image.SCALE_DEFAULT);
            player.setIcon(new ImageIcon(playerBoardResized));
            i++;
        }
    }

    /**
     * This method add marks in player's playerboard
     * */
    public void addMarks(ArrayList<Colors> marks){
        System.out.println("Marchi in gui:");
        marks.forEach(System.out::println);
        int i = 0;
        for (Colors mark: marks) {
            Image imageColor = createColorMarker(mark, currentPlayerBoardModified.getWidth(), currentPlayerBoardModified.getHeight());

            Graphics2D g = currentPlayerBoardModified.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g.drawImage(imageColor, marksPosition[i] * currentPlayerBoard.getWidth() / 1120, 1, null);
            g.dispose();
            Image playerBoardResized = currentPlayerBoardModified.getScaledInstance(player.getWidth(), player.getHeight(), Image.SCALE_DEFAULT);
            player.setIcon(new ImageIcon(playerBoardResized));
            i++;
        }
    }

    /**
     * This method add a power up to the player's powerup
     * */
    public void addPowerUp(String powerUp){
        powerUps.add(powerUp);
    }

    public void sendReload(int position){
        client.send(new ReloadMessage(client.getToken(), position));
    }

    public void substituteWeaponRequest(){
        System.out.println("devo entrare nel metodo efnwieonboiedvbourwbsobrgfwiobe");
        new SubstituteWeaponGui(cardsWeapon, this);
    }

        /**
         * This method is called when the player had chosen which weapon want to discard
         * */
    public void substituteWeapon(int position){
        client.send(new SubstituteWeaponResponse(client.getToken(), position));
        System.out.println(position);
    }

    public void spawn(){
        new SpawnGui(powerUps, this);
    }

    public void sendSpawnMessage(int position){
        client.send(new RespawnMessage(client.getToken(), position));
    }

    /**
     * This method is called when the player wants to use a powerup
     * */
    public void usePowerUp(int position, boolean granade, boolean scope, Colors colors){
        System.out.println(powerUps.get(position).substring(0, powerUps.get(position).length()-2));
        if (powerUps.get(position).substring(0, powerUps.get(position).length()-2).equals("teleporter") && !scope && !granade){
            actionType = "teleporter";
            JOptionPane.showMessageDialog(this, "Choose a square to teleport");
            selectedPowerUp = position;
        } else if (powerUps.get(position).substring(0, powerUps.get(position).length()-2).equals("newton") && !scope && !granade){
            new TargetChooseGui(new ArrayList<>(enemies.keySet()), 1, this, false, false);
            JOptionPane.showMessageDialog(this, "Choose a target to move him");
            selectedPowerUp = position;
        } else if (powerUps.get(position).substring(0, powerUps.get(position).length()-2).equals("tagbackGranade") && granade){
            client.send(new UsePowerUpResponse(client.getToken(), position, client.getToken(), colors, ""));
        } else if (powerUps.get(position).substring(0, powerUps.get(position).length()-2).equals("targettingScope") && scope) {
            client.send(new CanUseScoopResponse(client.getToken(), true, position));
        }
    }

    /**
     * This method is called to display to the player the tag back use request
     * */
    public void canUseVenom(Colors color){
        new UsePowerUpGui(powerUps, this, true, false, color);
    }

    /**
     * This method is called to display to the player the scoop use request
     * */
    public void canUseScoop(){
        new UsePowerUpGui(powerUps, this, false, true, null);
    }

    public void chooseTargetScope(ArrayList<Colors> targets){
        new TargetChooseGui(targets, 1, this, false, true);
    }

    public void sendScopeTarget(Colors target){
        client.send(new ScopeTargetResponse(client.getToken(), target));
    }

    /**
     * This method is used to set myTurn flag when the turn of the player begin
     * */
    public void myTurn(boolean turn){
        myTurn = turn;
    }

    /**
     * This method is used to update damage marks of the other players
     * */
    public void updateOthersBar(ArrayList<Colors> damageBar, Colors player){
        int i = 0;
        for (Colors c: damageBar){
            Graphics2D g = currentOtherPlayerBoards[enemies.get(player)].createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            Image damage = createColorMarker(c, currentOtherPlayerBoards[enemies.get(player)].getWidth(), currentOtherPlayerBoards[enemies.get(player)].getHeight());

            g.drawImage(damage, damagePosition[i] * currentOtherPlayerBoards[enemies.get(player)].getWidth() / 1120, 120 * currentOtherPlayerBoards[enemies.get(player)].getHeight() / 274, null);
            g.dispose();
            i++;
        }
        Image markResized = currentOtherPlayerBoards[enemies.get(player)].getScaledInstance(players[enemies.get(player)].getWidth(), players[enemies.get(player)].getHeight(), Image.SCALE_DEFAULT);
        players[enemies.get(player)].setIcon(new ImageIcon(markResized));
    }

    /**
     * This method is used to create a colored image used as mark for damage and marks in player board
     * */
    private Image createColorMarker(Colors c, int scaleX, int scaleY){
        BufferedImage damage = new BufferedImage(32, 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = damage.createGraphics();
        graphics.setPaint(getPaintingColor(c));
        graphics.fillRect(0, 0, damage.getWidth(), damage.getHeight());
        graphics.dispose();
        return damage.getScaledInstance(32 * scaleX/1120, 40 * scaleY/274, Image.SCALE_DEFAULT);
    }

    /**
     * This method is used to send the chosen weapon for a shot action
     * */
    public void weaponChosen(String choose){
        client.send(new ReceiveTargetSquare(client.getToken(), "shoot", Character.getNumericValue(choose.charAt(0)), Integer.parseInt(choose.substring(2))));
        System.out.println(choose);
    }

    /**
     * This method is used to send the chosen weapon for a shot action
     * */
    public void targetChosen(ArrayList<Colors> chosen){
        client.send(new ShootResponsep(client.getToken(), chosen));
    }

    public void setTargetPowerUp(Colors color){
        targetPowerup = color;
        actionType = "newton";
        JOptionPane.showMessageDialog(this, "Choose a square to move the selected player");
    }

    /**
     * This method add the component listeners to the component of the window
     * */
    private void addComponentListeners(){
        MapGui self = this;

        map.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JLabel label = (JLabel) e.getComponent();
                resizeImage(label, (new ImageIcon(currentMapImage)));
            }
        });

        playerBoards.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (int i= 0; i < 4; i++) {
                    int a = i;
                    new Thread(() -> resizeImage(players[a], new ImageIcon(currentOtherPlayerBoards[a]))).start();
                }
            }
        });

        player.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JLabel label = (JLabel) e.getComponent();
                resizeImage(label, (new ImageIcon(currentPlayerBoard)));
            }
        });


        map.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openWeaponDetail(e.getX(), e.getY(), false);
                if(myTurn) {
                    if ((e.getX() < 285 * map.getWidth()/2545) && (e.getY() > 1810 * map.getHeight()/1928)){
                        //TODO send pass message
                        int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to pass?");
                        if (response == 0) {
                            client.send(new Pass(client.getToken()));
                            myTurn = false;
                            System.out.println("pass");
                        }
                    }
                    //System.out.println(e.getX() + " u " + e.getY());
                    openWeaponDetail(e.getX(), e.getY(), false);
                    if (actionType.equals("move")) {
                        String id = getSquareId(e.getX(), e.getY());
                        if (!id.equals("") && !redCrosses.contains(id)) {
                            client.send(new MoveResponse(client.getToken(), id));
                            System.out.println("move to: " + id);
                            actionType = "";
                            Image mapResized = currentMapImage.getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT);
                            map.setIcon(new ImageIcon(mapResized));
                        }
                    } else if (actionType.equals("room")) {
                        client.send(new ShootResponser(client.getToken(), getSquareId(e.getX(), e.getY())));
                        actionType = "";
                        Image mapResized = currentMapImage.getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT);
                        map.setIcon(new ImageIcon(mapResized));
                    }
                    else if (actionType.equals("square")) {
                        client.send(new ShootResponses(client.getToken(), getSquareId(e.getX(), e.getY())));
                        actionType = "";
                        Image mapResized = currentMapImage.getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT);
                        map.setIcon(new ImageIcon(mapResized));
                    } else if (actionType.equals("movep")) {
                        client.send(new TargetMoveResponse(client.getToken(), getSquareId(e.getX(), e.getY())));
                        actionType = "";
                        Image mapResized = currentMapImage.getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT);
                        map.setIcon(new ImageIcon(mapResized));
                    } else if (actionType.equals("teleporter")) {
                        if (selectedPowerUp < 4) {
                            client.send(new UsePowerUpResponse(client.getToken(), selectedPowerUp, client.getToken(), myColor, getSquareId(e.getX(), e.getY())));
                            selectedPowerUp = 4;
                            actionType = "";
                        }
                    }else if(actionType.equals("newton")){
                        client.send(new UsePowerUpResponse(client.getToken(), selectedPowerUp, client.getToken(), targetPowerup, getSquareId(e.getX(), e.getY())));
                        actionType = "";
                    }
                }
                else
                    JOptionPane.showMessageDialog(self, "Not your turn !!");
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
                if (e.getX() > 890 * player.getWidth()/1120){
                    new PlayerInformationGui(cardsWeapon, ammos, points);
                    System.out.println("info");
                }
                else
                    if (myTurn) {
                        if (1 == actionNumber || 2 == actionNumber){
                            if ((e.getX() < 67 * player.getWidth() / 1120) && ((e.getY() > 60 * player.getHeight() / 274) && (e.getY() < (60 + 32) * player.getHeight() / 274))) {
                                int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to move?");
                                if (response == 0) {
                                    client.send(new ReceiveTargetSquare(client.getToken(), "move"));
                                    System.out.println("move");
                                }
                            } else if ((e.getX() < 67 * player.getWidth() / 1120) && ((e.getY() > 104 * player.getHeight() / 274) && (e.getY() < (104 + 32) * player.getHeight() / 274))) {
                                int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to grab?");
                                if (response == 0) {
                                    client.send(new ReceiveTargetSquare(client.getToken(), "grab"));
                                    System.out.println("grab");
                                }
                            } else if ((e.getX() < 67 * player.getWidth() / 1120) && ((e.getY() > 147 * player.getHeight() / 274) && (e.getY() < (147 + 32) * player.getHeight() / 274))) {
                                int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to shot?");
                                if (response == 0) {
                                    System.out.println("shot");
                                    List<String> cardsName = cardsWeapon.stream().map(CardWeapon::getName).collect(toList());
                                    new WeaponChooseGui(cardsName, self, true);
                                }
                            } else if (((e.getX() > 615 * player.getWidth() / 1120) && (e.getX() < (615 + 75) * player.getWidth() / 1120)) && (e.getY() > 185 * player.getHeight() / 274)) {
                                new UsePowerUpGui(powerUps, self, false, false, null);
                                System.out.println("powerup");
                            } else if (((e.getX() > 20 * player.getWidth() / 1120) && (e.getX() < (20 + 40) * player.getWidth() / 1120)) && ((e.getY() > 195 * player.getHeight() / 274) && (e.getY() < (195 + 55) * player.getHeight() / 274))) {
                                int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to reload?");
                                if (response == 0) {
                                    new ReloadGui(cardsWeapon ,self);
                                    System.out.println("reload");
                                    actionNumber = 3;
                                }
                            }
                        } else if (((e.getX() > 20 * player.getWidth() / 1120) && (e.getX() < (20 + 40) * player.getWidth() / 1120)) && ((e.getY() > 195 * player.getHeight() / 274) && (e.getY() < (195 + 55) * player.getHeight() / 274))) {
                            int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to reload?");
                            if (response == 0) {
                                new ReloadGui(cardsWeapon ,self);
                                System.out.println("reload");
                            }
                        } else if (((e.getX() > 615 * player.getWidth() / 1120) && (e.getX() < (615 + 75) * player.getWidth() / 1120)) && (e.getY() > 185 * player.getHeight() / 274)) {
                            new UsePowerUpGui(powerUps, self, false, false, null);
                            System.out.println("powerup");
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(self, "Not your turn !!");

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

        text.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!text.getText().equals("")) {
                    /*JLabel chatLabel = new JLabel("@"+myColor+": "+text.getText());
                    chatLabel.setHorizontalTextPosition(JLabel.LEFT);
                    chatLabel.setVerticalTextPosition(JLabel.BOTTOM);
                    chatLabel.setOpaque(false);
                    chatArea.add(chatLabel);
                    chatArea.revalidate();*/
                    client.send(new ChatMessage(client.getToken(), "@"+myColor+": "+text.getText()));
                }
                text.setText("");
            }
        });
    }

    /**
     * This methos is used to set the ammo number of the player
     * */
    public void setAmmo(int value, int position){
        ammos[position] = value;
    }

    /**
     * This method is used to display the weapon detail when the user press on its name on the map
     * */
    private boolean openWeaponDetail(int x, int y, boolean grab){
        for (String s: spawnSquareWeapon.keySet()){
            for (int a = 0; a < 3; a++){
                if(!spawnSquareWeapon.get(s)[a].equals("")){
                    if (((x > ViewMap.getxWeapon(s, a) * map.getWidth()/2545) && (x < (ViewMap.getxWeapon(s, a)+ViewMap.getxWeaponIncrement(s))*map.getWidth()/2545))
                            && ((y > map.getHeight()*ViewMap.getyWeapon(s, a)/1928) && (y < (ViewMap.getyWeapon(s, a)+ViewMap.getyWeaponIncrement(s))*map.getHeight()/1928))){
                        new WeaponDetailGui(spawnSquareWeapon.get(s)[a], s, a, grab, this);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void grabWeaponRequest(){
        new WeaponChooseGui(Arrays.asList(spawnSquareWeapon.get(myPosition)), this, false);
    }

    public void sendGrabWeapon(int position){
        client.send(new GrabWeapon(client.getToken(), position));
    }

    /**
     * This metohd returns the square id string from the x and y coordinates
     * */
    private String getSquareId(int x, int y){
        for (int i = 0; i < 12; i++){
            if ( ((x > ViewMap.getXCoordinates(ViewMap.getIds()[i]) * map.getWidth()/2545) &&
                    (x < (ViewMap.getXCoordinates(ViewMap.getIds()[i])+350) * map.getWidth()/2545)) &&
                    ((y > (ViewMap.getYCoordinates(ViewMap.getIds()[i])) * map.getHeight()/1928) &&
                            (y < ((ViewMap.getYCoordinates(ViewMap.getIds()[i])+340) * map.getHeight()/1928))) ) {
                return ViewMap.getIds()[i];
            }
        }
        return "";
    }

    /**
     * This method is used to resize the image to fit the current window size
     * */
    private void resizeImage(JLabel label, ImageIcon imageIcon){
        Dimension size = label.getSize();
        Image resized = imageIcon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
        label.setIcon(new ImageIcon(resized));
    }

    /**
     * This method returns a Color from a Colors value class
     * */
    public static Color getPaintingColor(Colors myColor){
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

    /**
     * This method set cards owned by the user
     * */
    public void setCardsWeapon(ArrayList<CardWeapon> cards){
        this.cardsWeapon = cards;
    }

    /**
     * This method set powerups owned by the user
     * */
    public void setPowerUps(ArrayList<String> powerUps){
        powerUps.stream().forEach(x -> System.out.println("MapGui " + x));
        this.powerUps = powerUps;
    }

    /**
     * This method is used to set the action type used to determine what type of message send to the server
     * */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    /**
     * This method creates a window for the payment of something
     * */
    public void payment(Payment payment){
        new PaymentGui(payment, powerUps, this, client.getToken());
    }

    public void pay(PaymentResponse paymentResponse){
        client.send(paymentResponse);
    }

    /**
     * This method updates the enemy position
     * */
    public void updateEnemyPosition(Colors player, String id){
        othersPosition[enemies.get(player)] = id;
        Graphics2D g = currentOtherPlayerBoards[enemies.get(player)].createGraphics();
        Image number = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "numbers" + File.separatorChar + (ViewMap.getSquareNumber(id)+1) + ".png").getImage();

        g.drawImage(number, 1050 * currentOtherPlayerBoards[enemies.get(player)].getWidth() / 1120, 0, null);
        g.dispose();

        Image playerResized = currentOtherPlayerBoards[enemies.get(player)].getScaledInstance(players[enemies.get(player)].getWidth(), players[enemies.get(player)].getHeight(), Image.SCALE_DEFAULT);
        players[enemies.get(player)].setIcon(new ImageIcon(playerResized));
    }

    public void setMyPosition(String id){
        myPosition = id;
        Graphics2D g = currentPlayerBoard.createGraphics();
        Image number = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "numbers" + File.separatorChar + (ViewMap.getSquareNumber(id)+1) + ".png").getImage();

        g.drawImage(number, 1050 * currentPlayerBoard.getWidth() / 1120, 0, null);
        g.dispose();

        Image playerResized = currentPlayerBoard.getScaledInstance(player.getWidth(), player.getHeight(), Image.SCALE_DEFAULT);
        player.setIcon(new ImageIcon(playerResized));
    }

    /**
     * This method is used only for initialization purpose
     * */
    private void initializeSpawnWeapon(){
        this.spawnSquareWeapon = new HashMap<>();
        spawnSquareWeapon.put("0,2", new String[3]);
        spawnSquareWeapon.put("1,0", new String[3]);
        spawnSquareWeapon.put("2,3", new String[3]);

        for (String s: spawnSquareWeapon.keySet()){
            for (int i = 0; i < 3; i++){
                spawnSquareWeapon.get(s)[i] = "";
            }
        }
    }

    public static int getPlayerPosition(Colors color){
        return enemies.get(color);
    }

    /**
     * This method open a window which show the targetble players
     * */
    public void showTargetblePlayer(List<Colors> players, int max){
        new TargetChooseGui(players, max, this, true, false);
    }

    /**
     * This method is used to update the chat displayed messages
     * */
    public void updateChat(String message){
        JLabel chatLabel = new JLabel(message);
        chatLabel.setHorizontalTextPosition(JLabel.LEFT);
        chatLabel.setVerticalTextPosition(JLabel.BOTTOM);
        chatLabel.setOpaque(false);
        chatArea.add(chatLabel);
        chatArea.revalidate();
    }

    public void setActionNumber(int actionNumber){
        this.actionNumber = actionNumber;
        if (3 == actionNumber)
            JOptionPane.showMessageDialog(this, "You finish your action, you can only reload or use powerup");
    }

    private BufferedImage cloneImage(BufferedImage bi){
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static void main(String[] args) {
        MainGuiView.setUIManager();

        //new MapGui(Colors.GREEN, null);
    }
}