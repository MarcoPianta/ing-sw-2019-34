package view.gui;

import Model.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TargetChooseGui extends JFrame {
    private int maxMove;
    private ArrayList<Colors> chosen;
    private Colors currentColor;

    public TargetChooseGui(List<Colors> players, int max, MapGui mapGui, boolean shoot){
        super("Choose a target");
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.maxMove = max;
        this.chosen = new ArrayList<>();

        int i = 0;
        for (Colors color: players) {
            this.currentColor = color;

            c.gridx = i;
            c.gridy = 0;
            c.insets = new Insets(5,5,5,5);

            BufferedImage target = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = target.createGraphics();
            graphics.setPaint(MapGui.getPaintingColor(color));
            graphics.fillRect(0, 0, target.getWidth(), target.getHeight());
            graphics.dispose();

            int currentChoose = i;
            JLabel player = new JLabel(new ImageIcon(target.getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
            player.addMouseListener(new MouseListener() {
                Colors playerChoose = currentColor;
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(playerChoose);
                    if (maxMove > 0) {
                        chosen.add(playerChoose);
                        maxMove--;
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
            this.add(player, c);
            i++;
        }

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (shoot)mapGui.targetChosen(chosen);
                else mapGui.setTargetPowerUp(chosen.get(0));
            }
        });

        this.pack();
        this.setVisible(true);
    }
}
