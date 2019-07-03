package view.gui;

import Model.CardWeapon;
import network.messages.Payment;
import network.messages.PaymentResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReloadGui extends JFrame {
    private int i = 0;

    public ReloadGui(List<CardWeapon> cards, MapGui mapGui) {
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        this.add(new JLabel("Choose weapon to reload"), c);
        for (CardWeapon cardWeapon: cards){
            if (!cardWeapon.isCharge()){
                c.gridx = i;
                c.gridy = 1;

                JLabel label = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "weapons" + File.separatorChar + cardWeapon.getName() + ".png"));
                label.addMouseListener(new MouseListener() {
                    int choose = i;
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mapGui.sendReload(choose);
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
                this.add(label, c);

                i++;
            }
        }
        this.pack();
        this.setVisible(true);
    }

}
