package view.gui;

import Model.CardWeapon;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class PlayerInformationGui extends JFrame {

    public PlayerInformationGui(ArrayList<String> cards, int[] ammos){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);

        int i = 0;
        for (String card: cards){
            c.gridx = i;
            c.gridy = 1;
            JLabel weapon = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "weapons" + File.separatorChar + card + ".png"));
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
        this.add(new JLabel("You have: " + ammos[0] + " red ammos, " + ammos[2] + " yellow ammos, " + +ammos[2] + " blue ammos"), c);

        this.pack();
        this.setVisible(true);
    }
}
