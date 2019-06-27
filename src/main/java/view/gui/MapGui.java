package view.gui;

import Model.CardWeapon;
import Model.Colors;
import network.Client.Client;
import network.messages.Payment;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MapGui extends JFrame{
    private static final double RATIO = 1.320020;
    private Client client;
    private String actionType = "move"; //can assume values move indicating a move action. For shot action it assumes values: room, square. Or "null" if no action is monitored right now
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
    private BufferedImage[] currentOtherPlayerBoards = new BufferedImage[4];
    private static HashMap<Colors, Integer> enemies; //The string is the color name of the player
    private List<String> redCrosses;
    private String[] othersPosition = new String[4];
    private HashMap<String, String[]> spawnSquareWeapon;
    private boolean myTurn;
    private ArrayList<Colors> marks;
    private ArrayList<String> cardsWeapon;
    private ArrayList<String> powerUps;

    public MapGui(Colors myColor, Client client){
        super("Adrenaline");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.myColor = myColor;
        this.enemies = new HashMap<>();
        this.setLayout(new GridBagLayout());
        this.client = client;
        this.redCrosses = new ArrayList<>();
        this.marks = new ArrayList<>();
        this.cardsWeapon = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        initializeSpawnWeapon();

        playerBoard = new File("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "playerBoards" + File.separatorChar + myColor.getAbbreviation() + ".png");
        redCross = new File("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "redCross.png");
        mapImage = new File("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map1.png");
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

    public void addRedCross(List<String> id){
        redCrosses = id;
        for (String s: id){
            Graphics2D g = currentMapImage.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            BufferedImage cross = currentRedCross;
            Image crosResized = cross.getScaledInstance(350 * currentMapImage.getWidth() / 2545, 340 * currentMapImage.getHeight() / 1928, Image.SCALE_DEFAULT);
            g.drawImage(crosResized, ViewMap.getXCoordinates(s) * currentMapImage.getWidth() / 2545, (ViewMap.getYCoordinates(s) * currentMapImage.getHeight() / 1928), null);
            g.dispose();

            Image mapResized = currentMapImage.getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT);
            map.setIcon(new ImageIcon(mapResized));
        }
    }

    public void addDamage(List<Colors> damageBar){
        int i = 0;
        for (Colors c: damageBar){
            Image imageColor = createColorMarker(c, currentPlayerBoard.getWidth(), currentPlayerBoard.getHeight());

            Graphics2D g = currentPlayerBoard.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g.drawImage(imageColor, damagePosition[i] * currentPlayerBoard.getWidth() / 1120, 120 * currentPlayerBoard.getHeight() / 274, null);
            g.dispose();
            Image playerBoardResized = currentPlayerBoard.getScaledInstance(player.getWidth(), player.getHeight(), Image.SCALE_DEFAULT);
            player.setIcon(new ImageIcon(playerBoardResized));
            i++;
        }
    }

    public void addMarks(Colors mark){
        marks.add(mark);
        int i = 0;
        Image imageColor = createColorMarker(mark, currentPlayerBoard.getWidth(), currentPlayerBoard.getHeight());

        Graphics2D g = currentPlayerBoard.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g.drawImage(imageColor, marksPosition[i] * currentPlayerBoard.getWidth() / 1120, 1, null);
        g.dispose();
        Image playerBoardResized = currentPlayerBoard.getScaledInstance(player.getWidth(), player.getHeight(), Image.SCALE_DEFAULT);
        player.setIcon(new ImageIcon(playerBoardResized));
        i++;
    }

    public void addPowerUp(String powerUp){
        powerUps.add(powerUp);
    }

    public void substituteWeapon(int position){
        //TODO send grabWeaponResponse
        System.out.println(position);
    }

    public void usePowerUp(int position, boolean granade){
        //client.send(new UsePowerUp(client.getToken(), position, ));
    }

    public void canUseVenom(){
        new UsePowerUpGui(powerUps, this, true);
    }

    public void myTurn(boolean turn){
        myTurn = turn;
    }

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

    private Image createColorMarker(Colors c, int scaleX, int scaleY){
        BufferedImage damage = new BufferedImage(32, 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = damage.createGraphics();
        graphics.setPaint(getPaintingColor(c));
        graphics.fillRect(0, 0, damage.getWidth(), damage.getHeight());
        graphics.dispose();
        return damage.getScaledInstance(32 * scaleX/1120, 40 * scaleY/274, Image.SCALE_DEFAULT);
    }

    public void weaponChosen(String choose){
        //client.send(new ReceiveTargetSquare(client.getToken(), "shoot", Character.getNumericValue(choose.charAt(0), Character.getNumericValue(choose.charAt(2)))));
        System.out.println(choose);
    }

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
                //System.out.println(e.getX() + " u " + e.getY());
                openWeaponDetail(e.getX(), e.getY(), true);
                if (actionType.equals("move")){
                    String id = getSquareId(e.getX(), e.getY());
                    if (!id.equals("") && !redCrosses.contains(id)) {
                        //client.send(new MoveResponse(client.getToken(), id));
                        System.out.println("move to: " + id);
                    }
                }
                else if (actionType.equals("room")){
                    //client.send(new ShotResponse());
                }
                else if (actionType.equals("square")){
                    //client.send(new ShotResponse());
                }
                updateEnemyPosition(Colors.BLUE, "0,0");
                addWeaponToMap("1,0", 0, "cyberblade");
                addWeaponToMap("1,0", 1, "granadeLauncher");
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
                    //client.send(new ReceiveTargetSquare(client.getToken(), "move"));
                    //TODO delete following code
                    ArrayList<Colors> damagesList = new ArrayList<>();
                    damagesList.add(Colors.BLUE);
                    damagesList.add(Colors.GREEN);
                    addDamage(damagesList);
                    addMarks(Colors.BLUE);
                    //till here
                    int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to move?");
                    if (response == 0)
                        System.out.println("move");
                }
                else if ( (e.getX() < 67 * player.getWidth()/1120) && ((e.getY() > 104 * player.getHeight()/274) && (e.getY() < (104+32) * player.getHeight()/274))){
                    //client.send(new ReceiveTargetSquare(client.getToken(), "grab"));
                    //TODO delete following code
                    ArrayList<Colors> damagesList = new ArrayList<>();
                    damagesList.add(Colors.BLUE);
                    damagesList.add(Colors.GREEN);
                    updateOthersBar(damagesList, Colors.BLUE);
                    ArrayList<String> ids = new ArrayList<>();
                    ids.add("0,0");
                    ids.add("0,2");
                    ids.add("1,3");
                    ids.add("2,1");
                    addRedCross(ids);
                    //till here
                    int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to grab?");
                    if (response == 0)
                        System.out.println("grab");
                }
                else if ( (e.getX() < 67 * player.getWidth()/1120) && ((e.getY() > 147 * player.getHeight()/274) && (e.getY() < (147+32) * player.getHeight()/274))){
                    int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to shot?");
                    if (response == 0) {
                        System.out.println("shot");
                        //TODO delete following code
                        ArrayList<String> weaponsName = new ArrayList<>();
                        weaponsName.add("cyberblade");
                        weaponsName.add("electroscyte");
                        //till here
                        new WeaponChooseGui(weaponsName, self);
                    }
                }
                else if ( ((e.getX() > 20 * player.getWidth()/1120) && (e.getX() < (20+40) * player.getWidth()/1120)) && ((e.getY() > 195 * player.getHeight()/274) && (e.getY() < (195+55) * player.getHeight()/274))){
                    //TODO send pass message
                    int response = JOptionPane.showConfirmDialog(self, "Are you sure you want to pass?");
                    if (response == 0)
                        System.out.println("pass");
                }
                else if (e.getX() > 890 * player.getWidth()/1120){
                    ammos[0] = 0;
                    ammos[1] = 0;
                    ammos[2] = 1;
                    cardsWeapon.add("cyberblade");
                    cardsWeapon.add("electroscyte");
                    new PlayerInformationGui(cardsWeapon, ammos);
                    new SubstituteWeaponGui(cardsWeapon, self);
                    System.out.println("info");
                }
                else if ( ((e.getX() > 615 * player.getWidth()/1120) && (e.getX() < (615+75) * player.getWidth()/1120)) && (e.getY() > 185 * player.getHeight()/274)) {
                    new UsePowerUpGui(powerUps, self, false);
                    System.out.println("powerup");
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

        text.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!text.getText().equals("")) {
                    JLabel chatLabel = new JLabel("@"+myColor+": "+text.getText());
                    chatLabel.setHorizontalTextPosition(JLabel.LEFT);
                    chatLabel.setVerticalTextPosition(JLabel.BOTTOM);
                    chatLabel.setOpaque(false);
                    chatArea.add(chatLabel);
                    chatArea.revalidate();
                    //client.send(new ChatMessage(client.getToken(), "@"+myColor+": "+text.getText()));
                }
                text.setText("");
            }
        });
    }

    public void setAmmo(int value, int position){

    }

    private void openWeaponDetail(int x, int y, boolean grab){
        for (String s: spawnSquareWeapon.keySet()){
            for (int a = 0; a < 3; a++){
                if(!spawnSquareWeapon.get(s)[a].equals("")){
                    if (((x > ViewMap.getxWeapon(s, a) * map.getWidth()/2545) && (x < (ViewMap.getxWeapon(s, a)+ViewMap.getxWeaponIncrement(s))*map.getWidth()/2545))
                            && ((y > map.getHeight()*ViewMap.getyWeapon(s, a)/1928) && (y < (ViewMap.getyWeapon(s, a)+ViewMap.getyWeaponIncrement(s))*map.getHeight()/1928))){
                        new WeaponDetailGui(spawnSquareWeapon.get(s)[a], s, a, grab);
                    }
                }
            }
        }
    }

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

    private void resizeImage(JLabel label, ImageIcon imageIcon){
        Dimension size = label.getSize();
        Image resized = imageIcon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
        label.setIcon(new ImageIcon(resized));
    }

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

    public void setCardsWeapon(ArrayList<CardWeapon> cards){
        for (CardWeapon c: cards) {
            this.cardsWeapon.add(c.getName());
        }
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void payment(Payment payment){
        new PaymentGui(payment);
    }

    public void updateEnemyPosition(Colors player, String id){
        othersPosition[enemies.get(player)] = id;
        Graphics2D g = currentOtherPlayerBoards[enemies.get(player)].createGraphics();
        Image number = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "numbers" + File.separatorChar + (ViewMap.getSquareNumber(id)+1) + ".png").getImage();

        g.drawImage(number, 1050 * currentOtherPlayerBoards[enemies.get(player)].getWidth() / 1120, 0, null);
        g.dispose();

        Image playerResized = currentOtherPlayerBoards[enemies.get(player)].getScaledInstance(players[enemies.get(player)].getWidth(), players[enemies.get(player)].getHeight(), Image.SCALE_DEFAULT);
        players[enemies.get(player)].setIcon(new ImageIcon(playerResized));
    }

    public void addWeaponToMap(String id, int position, String weapon){
        double rotationRequired;
        int xOffset;
        int yOffset;
        spawnSquareWeapon.get(id)[position] = weapon;
        if (Character.getNumericValue(id.charAt(0)) == 0){
            rotationRequired = 1.0472; //60 degree in radiant
            xOffset = 90; yOffset = 150;
        }
        else {
            rotationRequired = 0.5236; //30 degree in radiant
            xOffset = 65; yOffset = 100;
        }

        BufferedImage text = new BufferedImage(400, 240, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = text.createGraphics();
        g2d.setPaint(Color.WHITE);
        Font font = new Font("Arial", Font.PLAIN, 50);
        g2d.setFont(font);
        g2d.drawString(weapon, 0, text.getHeight()/2);
        g2d.dispose();
        double locationX = 0;
        double locationY = text.getHeight();
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        Graphics2D g = currentMapImage.createGraphics();
        g.drawImage(op.filter(text, null), ViewMap.getxWeapon(id, position)-xOffset, ViewMap.getyWeapon(id, position)-yOffset, null);
        g.dispose();

        Image mapResized = currentMapImage.getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT);
        map.setIcon(new ImageIcon(mapResized));
    }

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

    public void updateChat(String message){
        JLabel chatLabel = new JLabel(message);
        chatLabel.setHorizontalTextPosition(JLabel.LEFT);
        chatLabel.setVerticalTextPosition(JLabel.BOTTOM);
        chatLabel.setOpaque(false);
        chatArea.add(chatLabel);
        chatArea.revalidate();
    }

    public static void main(String[] args) {
        MainGuiView.setUIManager();

        new MapGui(Colors.GREEN, null);
    }
}