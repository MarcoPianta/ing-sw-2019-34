package view.gui;

import Model.CardWeapon;

import java.util.List;
import javax.swing.*;
import java.awt.*;

/**
 * This class is used to display a window for player's infos
 */
public class PlayerInformationGui extends JFrame {

    public PlayerInformationGui(List<CardWeapon> cards, int[] ammos, int points){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);

        int i = 0;
        for (CardWeapon cardWeapon: cards){
            String card = cardWeapon.getName();
            c.gridx = i;
            c.gridy = 1;
            JLabel weapon = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("GUI/weapons/" + card + ".png")));
            this.add(weapon, c);
            i++;
        }
        if (!cards.isEmpty()) {
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = cards.size();
            this.add(new JLabel("Your weapons are: "), c);
        }

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = cards.size();
        this.add(new JLabel("You have: " + ammos[0] + " red ammos, " + ammos[1] + " yellow ammos, " + +ammos[2] + " blue ammos"), c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = cards.size();
        this.add(new JLabel("Your points are: " + points), c);

        this.pack();
        this.setVisible(true);
    }
}
