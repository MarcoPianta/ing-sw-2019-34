package view.gui;

import Model.CardAmmo;
import Model.CardPowerUp;
import Model.CardWeapon;
import Model.Colors;
import network.Client.RMI.RMIClient;
import network.Client.Socket.SocketClient;
import network.Client.Client;
import network.messages.Payment;

import view.View;
import view.gui.actionHandler.CreateNewGame;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGuiView extends View {
    private JFrame frame;
    private MapGui mapGui;
    private JLabel imageLabel;
    private JPanel buttonAndTextPanel;
    private JPanel mainPanel;
    private boolean gameStarted;

    public static final double GOLDENRATIO = 1.31846473; //Used to have a good ratio between width and height
    private static final int INITIALWINDOWHEIGHT = 600;
    private final String RULESWEBSITE = "https://czechgames.com/files/rules/adrenaline-rules-en.pdf";

    public MainGuiView(){
        super();

        int panelWidth = new Double(INITIALWINDOWHEIGHT *GOLDENRATIO).intValue();

        frame = new JFrame("Adrenaline");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(panelWidth, INITIALWINDOWHEIGHT);

        frame.setMinimumSize(new Dimension(panelWidth, INITIALWINDOWHEIGHT));
        /*frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.close();
            }
        });*/
        mainPanel = new JPanel(new BorderLayout());

        ImageIcon icon = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "homeAdrenaline.png");
        imageLabel = new JLabel(icon);

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        imageLabel.setLayout(flowLayout);
        JButton rules = new JButton("Press to read rules");
        rules.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(RULESWEBSITE));
                }catch (URISyntaxException| IOException i){
                    JOptionPane.showMessageDialog(frame, "Cannot show rules");
                }
            }
        });
        imageLabel.add(rules);

        mainPanel.add(imageLabel, BorderLayout.CENTER);

        buttonAndTextPanel = new JPanel(new BorderLayout());

        JTextField text = new JTextField("000000", 25);
        buttonAndTextPanel.add(text, BorderLayout.LINE_START);

        String[] connectionString = {"Socket", "RMI"};
        JComboBox<String> socketRMI = new JComboBox<>(connectionString);
        buttonAndTextPanel.add(socketRMI, BorderLayout.CENTER);

        JButton button = new JButton("Start a new Game");
        button.setSize(buttonAndTextPanel.getWidth()*10/100, buttonAndTextPanel.getHeight());
        button.addActionListener(new CreateNewGame(client, text , frame) );
        MainGuiView self = this;
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (socketRMI.getSelectedIndex() == 0)
                    client = new SocketClient("192.168.0.2", 10000, self);
                else
                    client = new RMIClient("192.168.0.2",10001, self);
                //showGameSettingsRequest();
                //JOptionPane.showMessageDialog(frame, "Connection request sent, waiting for server");
            }
        });
        buttonAndTextPanel.add(button, BorderLayout.LINE_END);

        mainPanel.add(buttonAndTextPanel, BorderLayout.PAGE_END);

        mainPanel.addComponentListener(new ComponentListener() {
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

        frame.add(mainPanel);
        //frame.pack();
    }

    public static void main(String[] args) {
        setUIManager();

        MainGuiView view = new MainGuiView();
        view.frame.setVisible(true);
    }

    public void main() {
        this.frame.setVisible(true);
    }

    @Override
    public void setDamageBar(List<Colors> damageBar) {
        mapGui.addDamage(damageBar);
    }

    @Override
    public void showToken() {
        JOptionPane.showMessageDialog(frame, "Your token is : " + client.getToken() );
        //new Thread(this::showGameSettingsRequest).start();
    }

    @Override
    public void setWeapons(ArrayList<CardWeapon> weapons) {
        mapGui.setCardsWeapon(weapons);
    }

    @Override
    public void setPowerUps(ArrayList<CardPowerUp> powerUps) {
        ArrayList<String> powerUpsName = new ArrayList<>();
        for (CardPowerUp p: powerUps){
            powerUpsName.add(p.getName());
        }
        mapGui.setPowerUps(powerUpsName);
    }

    @Override
    public void addPowerup(CardPowerUp powerUp) {
        mapGui.addPowerUp(powerUp.getName());
    }

    @Override
    public void fillSpawn(String squareID, int position, String weaponName) {
        mapGui.addWeaponToMap(squareID, position,weaponName);
    }

    @Override
    public void fillSquare(String squareID, CardAmmo ammo) {
        mapGui.addAmmoToMap(squareID, ammo);
    }

    @Override
    public void setBlueAmmo(int blueAmmo) {
        mapGui.setAmmo(blueAmmo, 2);
    }

    @Override
    public void setRedAmmo(int redAmmo) {
        mapGui.setAmmo(redAmmo, 0);
    }

    @Override
    public void setYellowAmmo(int yellowAmmo) {
        mapGui.setAmmo(yellowAmmo, 1);
    }

    @Override
    public void chatMessage(String message) {
        mapGui.updateChat(message);
    }

    @Override
    public void setMyTurn(boolean myTurn) {
        mapGui.myTurn(myTurn);
        if (myTurn)
            JOptionPane.showMessageDialog(mapGui, "It's your turn");
        else
            JOptionPane.showMessageDialog(mapGui, "Your turn is ended");
    }

    @Override
    public void showReachableSquares(List<String> squares) {
        mapGui.setActionType("move");
        List<String> ids = Arrays.asList(ViewMap.getIds());
        ids.removeAll(squares);
        mapGui.addRedCross(ids);
    }

    @Override
    public void showPossibleTarget(List<Colors> targets, int max) {
        mapGui.showTargetblePlayer(targets, max);
    }

    @Override
    public void showPowerUpChooseRespawn() {

    }

    @Override
    public void showMessage(String message) {
        if (frame != null)
            JOptionPane.showMessageDialog(frame, message);
        else
            JOptionPane.showMessageDialog(mapGui, message);
    }

    public void addAmmoToMap(String id, CardAmmo card){
        mapGui.addAmmoToMap(id, card);
    }

    @Override
    public void showVenomRequest(Colors playerColor) {
        int value = JOptionPane.showConfirmDialog(mapGui, playerColor + "Hurt you, do you want to use tag back granade?");
        if (value == 0)
            mapGui.canUseVenom();
    }

    //TODO probably the name will be modified
    public void showScoopRequest(){
        int value = JOptionPane.showConfirmDialog(mapGui, "Do you want to use scoop power up?");
        if (value == 0)
            mapGui.canUseVenom();
    }

    @Override
    public void showGameSettingsRequest() {
        //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        frame.setVisible(false);
        frame = new GameSettingsChoose(client);
    }

    @Override
    public void startGame(String map) {
        frame.setVisible(false);
        frame = null;
        mapGui = new MapGui(client.getPlayerColor(), client, map);
        gameStarted = true;
    }

    @Override
    public void startTurn() {
        setMyTurn(true);
    }

    @Override
    public void endGame(boolean winner) {
        if (winner)
            JOptionPane.showMessageDialog(frame, "The game is over.\nCongratulation, you won!");
        else
            JOptionPane.showMessageDialog(frame, "The game is over.\nUnfortunately you didn't win");
    }

    public void setClient(Client client){
        this.client = client;
    }

    @Override
    public void updateEnemiesDamageBar(ArrayList<Colors> damageBar, Colors player) {
        mapGui.updateOthersBar(damageBar, player);
    }

    public void showPossibleRooms(List<String> targets){
        mapGui.setActionType("room");
        List<String> ids = Arrays.asList(ViewMap.getIds());
        ids.removeAll(targets);
        mapGui.addRedCross(ids);
    } //For shot action

    public void showPossibleSquares(List<String> targets){
        mapGui.setActionType("square");
        List<String> ids = Arrays.asList(ViewMap.getIds());
        ids.removeAll(targets);
        mapGui.addRedCross(ids);
    }

    public void showTargetMove(List<String> targets){
        mapGui.setActionType("movep");
        List<String> ids = Arrays.asList(ViewMap.getIds());
        ids.removeAll(targets);
        mapGui.addRedCross(ids);
    } //When need to be shown target which have to be moved for a weapon effect

    @Override
    public void updateEnemyPosition(Colors player, String position) {
        mapGui.updateEnemyPosition(player, position);
    }

    @Override
    public void payment(Payment message){
        mapGui.payment(message);
    }

    public static void setUIManager() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            JFrame.setDefaultLookAndFeelDecorated(false);
            System.out.println("Look and feel not found");
        }
        //Code to solve focus border bug of swing
        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("CheckBox.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        UIManager.put("ComboBox.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
    }
}
