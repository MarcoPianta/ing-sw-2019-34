package view.gui;

import Model.CardWeapon;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class PlayerInformationGui extends JFrame {

    public PlayerInformationGui(ArrayList<CardWeapon> cards){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int i = 0;
        for (CardWeapon card: cards){
            c.gridx = i;
            c.gridy = 0;
            JLabel weapon = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "weapons" + File.separatorChar + card.getName() + ".png"));
            this.add(weapon, c);
            i++;
        }

        this.setVisible(true);
    }
}
