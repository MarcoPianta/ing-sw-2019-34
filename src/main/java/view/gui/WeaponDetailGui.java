package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class WeaponDetailGui extends JFrame {
    ImageIcon card;

    public WeaponDetailGui(String weapon, String squareId, int postion, boolean grab, MapGui mapGui){
        super("Weapon detail");
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,5,5,5);
        this.card = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "weapons" + File.separatorChar + weapon + ".png");
        JLabel image = new JLabel(card);
        this.add(image, c);

        if (grab){
            JButton grabButton = new JButton("grab");
            grabButton.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mapGui.sendGrabWeapon(postion);
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
            c.gridy = 1;
            this.add(grabButton, c);
        }

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = image.getSize();
                Image resized = card.getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
                image.setIcon(new ImageIcon(resized));
            }
        });

        this.pack();
        this.setVisible(true);
    }
}