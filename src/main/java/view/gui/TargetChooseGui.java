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

/**
 * This class is used to display a window for choose a target to shot
 */
public class TargetChooseGui extends JFrame {
    private int maxMove;
    private ArrayList<Colors> chosen;
    private Colors currentColor;

    public TargetChooseGui(List<Colors> players, int max, MapGui mapGui, boolean shoot, boolean scope){
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
                    if (maxMove > 0) {
                        chosen.add(playerChoose);
                        maxMove--;
                        JOptionPane.showMessageDialog(null, "You choose the player, you still can choose " + maxMove + " players");
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

        c.gridx = 0;
        c.gridy = 1;
        JButton confirm = new JButton("Confirm");
        confirm.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (shoot)mapGui.targetChosen(chosen);
                else if (scope) mapGui.sendScopeTarget(chosen.get(0));
                else mapGui.setTargetPowerUp(chosen.get(0));
                dispose();
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
        this.add(confirm, c);

        this.pack();
        this.setVisible(true);
    }
}
