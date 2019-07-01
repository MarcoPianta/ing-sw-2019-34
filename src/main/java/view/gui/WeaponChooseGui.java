package view.gui;

import Model.CardWeapon;
import Model.Colors;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class WeaponChooseGui extends JFrame {
    private ArrayList<JButton> weaponButtons;
    private int choose;
    String weaponChoose;
    boolean choosed = false;

    public WeaponChooseGui(List<String> cards, MapGui mapGui, boolean shot){
        super("Weapon choose");
        this.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        weaponButtons = new ArrayList<>();

        int index = 0;
        for (String c: cards) {
            constraints.gridx = index;
            constraints.gridy = 1;
            constraints.weightx = 0.5;
            constraints.weighty = 0.3;
            constraints.insets = new Insets(5, 5, 5, 5);
            ImageIcon card = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "weapons" + File.separatorChar + c + ".png");
            JButton button = new JButton(card);
            this.add(button, constraints);
            weaponButtons.add(button);
            button.setName(c);

            int currentChoose = index;
            button.addMouseListener(new MouseListener() {
                int buttonChoose = currentChoose;
                @Override
                public void mouseClicked(MouseEvent e) {
                    choose = buttonChoose;
                    if (!choosed) {
                        if ((e.getY() > (180) * e.getComponent().getHeight() / 406) && (e.getY() < (180 + 100) * e.getComponent().getHeight() / 406)) {
                            weaponChoose = choose + "," + (0+1);
                        } else if ((e.getY() > 300 * e.getComponent().getHeight() / 406) && ((e.getX() > 10 * e.getComponent().getWidth() / 240) && (e.getX() < (10 + 105) * e.getComponent().getWidth() / 240))) {
                            weaponChoose = choose + "," + (1+1);
                        } else if ((e.getY() > 300 * e.getComponent().getHeight() / 406) && ((e.getX() > 125 * e.getComponent().getWidth() / 240) && (e.getX() < (125 + 105) * e.getComponent().getWidth() / 240))) {
                            weaponChoose = choose + "," + (2+1);
                        }
                        choosed = true;
                    }
                    else{
                        if ((e.getY() > (180) * e.getComponent().getHeight() / 406) && (e.getY() < (180 + 100) * e.getComponent().getHeight() / 406)) {
                            weaponChoose = weaponChoose + (0+1);
                        } else if ((e.getY() > 300 * e.getComponent().getHeight() / 406) && ((e.getX() > 10 * e.getComponent().getWidth() / 240) && (e.getX() < (10 + 105) * e.getComponent().getWidth() / 240))) {
                            weaponChoose = weaponChoose + (1+1);
                        } else if ((e.getY() > 300 * e.getComponent().getHeight() / 406) && ((e.getX() > 125 * e.getComponent().getWidth() / 240) && (e.getX() < (125 + 105) * e.getComponent().getWidth() / 240))) {
                            weaponChoose = weaponChoose + (2+1);
                        }
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

            index++;
        }

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = index;
        constraints.insets = new Insets(5, 5, 5, 5);
        JLabel info = new JLabel("Choose one weapon");
        this.add(info, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = index;
        constraints.insets = new Insets(5, 5, 5, 5);
        JButton choose = new JButton("Choose");
        this.add(info, constraints);
        choose.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (shot)
                    mapGui.weaponChosen(weaponChoose);
                else
                    mapGui.sendGrabWeapon(Character.getNumericValue(weaponChoose.charAt(0)));
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

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (JButton b: weaponButtons) {
                    new Thread(() -> {
                        Dimension size = b.getSize();
                        Image resized = new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "weapons" + File.separatorChar + b.getName() + ".png").getImage().getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
                        b.setIcon(new ImageIcon(resized));
                    }).start();
                }
            }
        });

        this.pack();
        this.setVisible(true);

    }

    public int getChoose() {
        return choose;
    }

    public static void main(String[] args) {
        ArrayList<String> weaponsName = new ArrayList<>();
        weaponsName.add("cyberblade");
        weaponsName.add("electroscyte");
        //new WeaponChooseGui(weaponsName);
    }
}
