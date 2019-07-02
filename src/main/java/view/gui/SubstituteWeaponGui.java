package view.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubstituteWeaponGui extends JFrame {
    int choose;

    public SubstituteWeaponGui(List<String> cards, MapGui mapGui){
        System.out.println("dentro choose gui");
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);

        choose = 0;
        for (String card: cards){
            System.out.println(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "weapons" + File.separatorChar + card + ".png"));
            c.gridx = choose;
            c.gridy = 1;
            JLabel weapon = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "weapons" + File.separatorChar + card + ".png"));
            weapon.addMouseListener(new MouseListener() {
                int chosen = choose;
                @Override
                public void mouseClicked(MouseEvent e) {
                    mapGui.substituteWeapon(chosen);
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

            this.add(weapon, c);
            choose++;
        }
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = cards.size();
        this.add(new JLabel("Choose a weapon"), c);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("cyberblade");
        strings.add("railgun");
        strings.add("shotgun");
        new SubstituteWeaponGui(strings, null);
    }
}
