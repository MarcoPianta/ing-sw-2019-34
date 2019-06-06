package view.gui;

import network.Client.RMI.RMIClient;
import network.Client.Socket.SocketClient;
import view.View;
import view.gui.Dictionaries.StringDictionary;
import view.gui.actionHandler.CreateNewGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MainGuiView extends View {
    private JFrame frame;
    private JLabel imageLabel;
    private JPanel buttonAndTextPanel;
    private JPanel mainPanel;
    private StringDictionary dictionary;

    public static final double GOLDENRATIO = 1.6180339887; //Used to have a good ratio between width and height
    private static final int INITIALWINDOWHEIGHT = 600;

    public MainGuiView(){
        super();//TODO super(client);

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
                    client = new SocketClient("192.168.0.9", 10000, self);
                else
                    client = new RMIClient(10001, self);
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

        System.out.println(javax.swing.SwingUtilities.isEventDispatchThread());
        MainGuiView view = new MainGuiView();
        view.frame.setVisible(true);
    }

    @Override
    public void showToken() {
        JOptionPane.showMessageDialog(frame, "Your token is : " + client.getToken() );
        new Thread(this::showGameSettingsRequest).start();
    }

    @Override
    public void showReachableSquares(List<String> squares) {

    }

    @Override
    public void showPossibleTarget(List<String> targets) {

    }

    @Override
    public void showPowerUpChooseRespawn() {

    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    @Override
    public void showVenomRequest() {
        int value = JOptionPane.showConfirmDialog(frame, "");
    }

    @Override
    public void showGameSettingsRequest() {
        //frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        frame.setVisible(false);
        frame = new GameSettingsChoose(client);
    }

    @Override
    public void endGame(boolean winner) {
        if (winner)
            JOptionPane.showMessageDialog(frame, "The game is over.\nCongratulation, you won!");
        else
            JOptionPane.showMessageDialog(frame, "The game is over.\nUnfortunately you didn't win");
    }

    private static void setUIManager() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            JFrame.setDefaultLookAndFeelDecorated(false);
            System.out.println("Look and feel not found");
        }
    }
}
