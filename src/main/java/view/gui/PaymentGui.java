package view.gui;

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

/**
 * This class is used to display a window for payment
 */
public class PaymentGui extends JFrame {
    ArrayList<Integer> selected = new ArrayList<>();
    private int scopeSelectedPayment = -1;
    private Integer token;
    private boolean showPowerUps = false;
    private JComboBox<String> ammoChoose;

    public PaymentGui(Payment payment, List<String> powerUps, MapGui mapGui, Integer token){
        this.token = token;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        String[] color = {" ","r", "y", "b"};
        String message = "You have to pay";
        int a = 0;
        for (Integer i: Arrays.asList(payment.getCost())){
            message = message + " " + color[a+1] + " " + i;
            a++;
        }
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(new JLabel(message), c);

        c.gridx = 0;
        c.gridy = 1;
        JLabel label = new JLabel("What you want to use to pay");
        this.add(label, c);

        c.gridx = 0;
        c.gridy = 2;
        JCheckBox powerup = new JCheckBox("Use powerups to pay, otherwise ammos are used");
        this.add(powerup, c);

        int i = 1;
        for (String s: powerUps){
            c.gridx = i;
            c.gridy = 2;
            if (!(payment.getPowerUp() != -1 && s.equals(powerUps.get(payment.getPowerUp())))) {
                JLabel labelPo = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "powerups" + File.separatorChar + s + ".png"));
                int choose = i;
                labelPo.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selected.add(choose - 1);
                        JOptionPane.showMessageDialog(labelPo, "You select " + powerUps.get(choose - 1) + " to pay");
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
                i++;
                this.add(labelPo, c);
            }
        }

        if (payment.getPowerUp() != -1){
            c.gridx = 0;
            c.gridy = 3;
            this.add(new JLabel("Select powerup to pay scope"), c);
            int index = 1;
            for (String s: powerUps) {
                if (!s.equals(powerUps.get(payment.getPowerUp()))) {
                    JLabel scopeP = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "powerups" + File.separatorChar + s + ".png"));
                    c.gridx = index;
                    c.gridy = 3;
                    int chosen = index;
                    scopeP.addMouseListener(new MouseListener() {
                        int choose = chosen;
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            scopeSelectedPayment = choose-1;
                            JOptionPane.showMessageDialog(scopeP, "You select the power up");
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
                    this.add(scopeP, c);
                    index++;
                }

            }
            c.gridx = 0;
            c.gridy = 4;
            JLabel text = new JLabel("Select an ammo tu use if you want: ");
            this.add(text, c);

            c.gridx = 1;
            c.gridy = 4;
            ammoChoose = new JComboBox<>(color);
            this.add(ammoChoose, c);
        }

        c.gridx = 0;
        c.gridy = 5;
        JButton button = new JButton("Confirm");
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (payment.getPowerUp()==-1)
                    mapGui.pay(createMessage(payment, powerup.isSelected(), payment.getPowerUp() != -1, ""));
                else
                    mapGui.pay(createMessage(payment, powerup.isSelected(), payment.getPowerUp() != -1, color[ammoChoose.getSelectedIndex()]));
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

        this.add(button, c);

        this.pack();
        this.setVisible(true);
    }

    private Integer[] createCost(JComboBox jComboBox){
        if (jComboBox.getSelectedIndex() == 1) {
            Integer[] ret = {1, 0, 0};
            return ret;
        } else if (jComboBox.getSelectedIndex() == 2){
            Integer[] ret = {0, 1, 0};
            return ret;
        }else if (jComboBox.getSelectedIndex() == 3) {
            Integer[] ret = {0, 0, 1};
            return ret;
        }else {
            Integer[] ret = {0, 0, 0};
            return ret;
        }
    }

    private String getColor(String powerUp){
        return powerUp.substring(powerUp.length()-1).toLowerCase();
    }

    private PaymentResponse createMessage(Payment payment, boolean usePowerup, boolean scoop, String cost){
        if (usePowerup && !scoop)
            return new PaymentResponse(token, selected, usePowerup, payment.getCost(), scoop);
        else if (!usePowerup && !scoop)
            return new PaymentResponse(token, selected, usePowerup, payment.getCost(), scoop);
        else if (!usePowerup && scoop)
            return new PaymentResponse(token, new ArrayList<>(), usePowerup, payment.getCost(), scoop, cost, scopeSelectedPayment);
        else
            return new PaymentResponse(token, selected, usePowerup, payment.getCost(), scoop, cost, scopeSelectedPayment);
    }

    public static void main(String[] args) {
        Integer[] costs = {0, 1, 1};
        ArrayList<String> powerUps = new ArrayList<>();
        //new PaymentGui(new Payment(000, costs, true), powerUps, null, null);
    }

}
