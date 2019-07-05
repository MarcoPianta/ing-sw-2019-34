package view.gui;

import Model.CardWeapon;
import Model.Colors;

import java.io.FileNotFoundException;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WeaponChooseGui extends JFrame {
    private ArrayList<JButton> weaponButtons;
    private int choose;
    private String weaponChoose = "";
    private boolean choosed = false;
    private List<CardWeapon> cardWeapon;

    public WeaponChooseGui(List<CardWeapon> cardWeapon, MapGui mapGui, boolean shot){
        super("Weapon choose");
        this.setLayout(new GridBagLayout());
        List<String> cards = cardWeapon.stream().map(CardWeapon::getName).collect(Collectors.toList());
        this.cardWeapon = cardWeapon;

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
            if (!c.equals("back")) {
                button.addMouseListener(new MouseListener() {
                    int buttonChoose = currentChoose;

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        choose = buttonChoose;
                        if (!choosed) {
                            if ((e.getY() > (180) * e.getComponent().getHeight() / 406) && (e.getY() < (180 + 100) * e.getComponent().getHeight() / 406)) {
                                weaponChoose = choose + "," + createActionFromWeapon(choose, 1);//choose + "," + (0 + 1);
                            } else if ((e.getY() > 300 * e.getComponent().getHeight() / 406) && ((e.getX() > 10 * e.getComponent().getWidth() / 240) && (e.getX() < (10 + 105) * e.getComponent().getWidth() / 240))) {
                                weaponChoose = choose + "," + createActionFromWeapon(choose, 2);//choose + "," + (1 + 1);
                            } else if ((e.getY() > 300 * e.getComponent().getHeight() / 406) && ((e.getX() > 125 * e.getComponent().getWidth() / 240) && (e.getX() < (125 + 105) * e.getComponent().getWidth() / 240))) {
                                weaponChoose = choose + "," + createActionFromWeapon(choose, 3);//choose + "," + (2 + 1);
                            }
                            choosed = true;
                            if (shot)
                                JOptionPane.showMessageDialog(button, "You choose weapon: " + weaponChoose.charAt(0) + " effect: " + weaponChoose.charAt(weaponChoose.length()-1));
                            else
                                JOptionPane.showMessageDialog(button, "You choose weapon: " + weaponChoose.charAt(0));
                        } else {
                            if ((e.getY() > (180) * e.getComponent().getHeight() / 406) && (e.getY() < (180 + 100) * e.getComponent().getHeight() / 406)) {
                                weaponChoose = weaponChoose + createActionFromWeapon(choose, 1);
                            } else if ((e.getY() > 300 * e.getComponent().getHeight() / 406) && ((e.getX() > 10 * e.getComponent().getWidth() / 240) && (e.getX() < (10 + 105) * e.getComponent().getWidth() / 240))) {
                                weaponChoose = weaponChoose + createActionFromWeapon(choose, 2);
                            } else if ((e.getY() > 300 * e.getComponent().getHeight() / 406) && ((e.getX() > 125 * e.getComponent().getWidth() / 240) && (e.getX() < (125 + 105) * e.getComponent().getWidth() / 240))) {
                                weaponChoose = weaponChoose + createActionFromWeapon(choose, 3);
                            }
                            if (shot)
                                JOptionPane.showMessageDialog(button, "You choose effect: " + weaponChoose.charAt(2));
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
            }

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
        this.add(choose, constraints);
        choose.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Effetto stringaaaaa: " + weaponChoose);
                if (!weaponChoose.equals("")) {
                    if (shot) {
                        if (!cardWeapon.get(Character.getNumericValue(weaponChoose.charAt(0))).getActionSequences().contains(Integer.parseInt(weaponChoose.substring(2)))) {
                            JOptionPane.showMessageDialog(choose, "You choose an incorrect effect!");
                            choosed = false;
                            weaponChoose = "";
                        }
                        else {
                            mapGui.weaponChosen(weaponChoose);
                            dispose();
                        }
                    }
                    else {
                        mapGui.sendGrabWeapon(Character.getNumericValue(weaponChoose.charAt(0)));
                        dispose();
                    }
                }
                else
                    JOptionPane.showMessageDialog(choose, "You choose nothing!");

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

    private String createActionFromWeapon(int position, int pressedEffect){
        if (cardWeapon.get(position).getActionSequences().size() == 1)
            return String.valueOf(1);
        else if (cardWeapon.get(position).getActionSequences().size() == 2) {
            if (pressedEffect == 1)
                return String.valueOf(1);
            else
                return String.valueOf(2);
        }
        else {
            if (pressedEffect == 1)
                return String.valueOf(1);
            else if (pressedEffect == 2)
                return String.valueOf(2);
            else
                return String.valueOf(3);
        }

    }

    public int getChoose() {
        return choose;
    }

    public static void main(String[] args) {
        ArrayList<CardWeapon> weaponsName = new ArrayList<>();
        try {
            weaponsName.add(new CardWeapon("cyberblade"));
            weaponsName.add(new CardWeapon("electroscyte"));
        }catch (FileNotFoundException e){

        }
        new WeaponChooseGui(weaponsName, null, true);
    }
}
