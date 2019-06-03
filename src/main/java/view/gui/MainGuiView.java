package view.gui;

import network.Server.Client;
import view.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MainGuiView extends View {
    private JFrame frame;
    private JLabel imageLabel;
    private JPanel mainPanel;

    public MainGuiView(){
        super();//TODO super(client);
        frame = new JFrame("Adrenaline");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.close();
            }
        });*/
        mainPanel = new JPanel();
        ImageIcon icon = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "homeAdrenaline.png");
        imageLabel = new JLabel(icon);
        mainPanel.add(imageLabel, Component.BOTTOM_ALIGNMENT);

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
        frame.pack();
    }

    public static void main(String[] args) {
        MainGuiView view = new MainGuiView();
        view.frame.setVisible(true);
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

    }

    @Override
    public void showVenomRequest() {

    }

    @Override
    public void showGameSettingsRequest() {

    }

    @Override
    public void endGame(boolean winner) {

    }
}
