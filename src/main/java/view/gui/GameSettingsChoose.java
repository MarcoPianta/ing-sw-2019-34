package view.gui;

import network.Client.Client;
import network.messages.GameSettingsResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import static view.gui.MainGuiView.GOLDENRATIO;

public class GameSettingsChoose extends JFrame{
    private Client client;
    private JPanel mainPanel;
    private JLabel map;
    private JComboBox<String> maps;
    JCheckBox use8Skulls;
    private String currentMap;
    private static final Object lock = new Object();

    private static final int INITIALWINDOWHEIGHT = 600;

    public GameSettingsChoose(Client client){
        super("Game settings");
        this.client = client;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());

        JPanel comboAndText = new JPanel(new BorderLayout());
        String[] mapsName = {"Map 1", "Map 2", "Map 3", "Map 4"};
        maps = new JComboBox<>(mapsName);
        currentMap = "map1";
        comboAndText.add(maps, BorderLayout.LINE_START);

        JLabel explanation = new JLabel("Choose your game settings");
        comboAndText.add(explanation, BorderLayout.CENTER);

        use8Skulls = new JCheckBox("Use 8 skulls (if not selected 5 skulls are used)");
        comboAndText.add(use8Skulls, BorderLayout.LINE_END);

        mainPanel.add(comboAndText, BorderLayout.PAGE_START);

        ImageIcon mapImage = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + "map1.png");
        map = new JLabel(mapImage);
        mainPanel.add(map, BorderLayout.CENTER);

        JPanel bottomPage = new JPanel(new BorderLayout());
        JButton confirm = new JButton("Continue");
        bottomPage.add(confirm, BorderLayout.LINE_END);
        mainPanel.add(bottomPage, BorderLayout.PAGE_END);

        confirm.addActionListener(e -> {
            client.send(new GameSettingsResponse(client.getToken(), getSkullsNumber(), maps.getSelectedIndex()+1));
        });
        mainPanel.addComponentListener(new ResizeMaps());
        maps.addActionListener(new ChangeMap());

        int panelWidth = new Double(INITIALWINDOWHEIGHT *GOLDENRATIO).intValue();

        add(mainPanel);
        setMinimumSize(new Dimension(panelWidth, INITIALWINDOWHEIGHT));
        setSize(panelWidth, INITIALWINDOWHEIGHT);
        setVisible(true);
    }

    class ChangeMap implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                currentMap = "map" + (maps.getSelectedIndex()+1);

                setImageMap();
            }).start();
        }
    }

    class ResizeMaps implements ComponentListener{

        @Override
        public void componentResized(ComponentEvent e) {
            new Thread(GameSettingsChoose.this::setImageMap).start();
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
    }

    private void setImageMap(){
        synchronized (lock) {
            ImageIcon im = new ImageIcon(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "mappe" + File.separatorChar + currentMap + ".png").getImage().getScaledInstance(map.getWidth(), map.getHeight(), Image.SCALE_DEFAULT));
            map.setIcon(im);
        }
    }

    private int getSkullsNumber(){
        if (use8Skulls.isSelected())
            return 8;
        else
            return 5;
    }
}