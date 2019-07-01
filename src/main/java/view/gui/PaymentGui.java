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

public class PaymentGui extends JFrame {
    ArrayList<Integer> selected = new ArrayList<>();
    private String scopeSelected;
    private Integer token;

    public PaymentGui(Payment payment, List<String> powerUps, MapGui mapGui, Integer token){
        this.token = token;
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        String[] color = {"R", "Y", "B"};
        String message = "You have to pay";
        int a = 0;
        for (Integer i: Arrays.asList(payment.getCost())){
            message = message + " " + color[a] + " " + i;
            a++;
        }
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(new JLabel(message), c);

        if (payment.isPowerUp()){
            c.gridx = 0;
            c.gridy = 1;
            this.add(new JLabel("Select a scope"), c);
            int index = 1;
            for (String s: powerUps) {
                JLabel scopeP = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "powerups" + File.separatorChar + s + ".png"));
                c.gridx = index;
                c.gridy = 1;
                scopeP.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        scopeSelected = getColor(s);
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
        c.gridy = 2;
        JLabel label = new JLabel("What you want to use to pay");
        this.add(label, c);

        c.gridx = 0;
        c.gridy = 3;
        JCheckBox powerup = new JCheckBox("Use powerups to pay, otherwise ammos are used");

        c.gridx = 0;
        c.gridy = 4;
        JButton button = new JButton("Confirm");
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mapGui.pay(createMessage(payment, powerup.isSelected(), payment.isPowerUp()));
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

    private void addListener(JCheckBox jCheckBox, PaymentGui paymentGui, List<String> powerUps){
        jCheckBox.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jCheckBox.isSelected()){
                    int i = 1;
                    GridBagConstraints c = new GridBagConstraints();
                    for (String s: powerUps){
                        c.gridx = i;
                        c.gridy = 3;
                        JLabel label = new JLabel(new ImageIcon("." + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "GUI" + File.separatorChar + "powerups" + File.separatorChar + s + ".png"));
                        paymentGui.add(label, c);
                        int choose = i;
                        label.addMouseListener(new MouseListener() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                selected.add(choose);
                                JOptionPane.showMessageDialog(paymentGui, "You select "+ powerUps.get(choose-1)+" to pay");
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
    }

    private String getColor(String powerUp){
        return powerUp.substring(powerUp.length()-1).toLowerCase();
    }

    private PaymentResponse createMessage(Payment payment, boolean usePowerup, boolean scoop){
        if (usePowerup && !scoop)
            return new PaymentResponse(token, selected, usePowerup, payment.getCost(), scoop);
        else if (!usePowerup && !scoop)
            return new PaymentResponse(token, selected, usePowerup, payment.getCost(), scoop);
        else if (!usePowerup && scoop)
            return new PaymentResponse(token, selected, usePowerup, payment.getCost(), scoop, scopeSelected);
        else
            return new PaymentResponse(token, selected, usePowerup, payment.getCost(), scoop, selected.get(0));
    }

    public static void main(String[] args) {
        Integer[] costs = {0, 1, 1};
        ArrayList<String> powerUps = new ArrayList<>();
        new PaymentGui(new Payment(000, costs, true), powerUps, null, null);
    }

}
