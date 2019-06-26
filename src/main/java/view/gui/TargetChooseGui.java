package view.gui;

import Model.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TargetChooseGui extends JFrame {

    public TargetChooseGui(List<Colors> players){
        super("Choose a target");
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int i = 0;
        for (Colors color: players) {
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
                int playerChoose = currentChoose;
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println(playerChoose);
                    //TODO send the choose playerChoose
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

        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        ArrayList<Colors> targetsList = new ArrayList<>();
        targetsList.add(Colors.BLUE);
        targetsList.add(Colors.GREEN);
        targetsList.add(Colors.YELLOW);

        new TargetChooseGui(targetsList);
    }
}
