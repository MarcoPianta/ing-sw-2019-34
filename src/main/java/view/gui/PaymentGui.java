package view.gui;

import network.messages.Payment;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class PaymentGui extends JFrame {

    public PaymentGui(Payment payment){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        String[] color = {"R", "Y", "B"};
        String message = "You have to pay";
        int a = 0;
        for (Integer i: Arrays.asList(payment.getCost())){
            message = message + " " + color[i] + " " + i;
            a++;
        }
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        this.add(new JLabel(message), c);

        if (payment.isPowerUp()){
            String[] paymentString = {"R", "Y", "B"};
            JComboBox<String> paymentBox = new JComboBox<>(paymentString);
        }

        this.setVisible(true);
    }

    public static void main(String[] args) {
        Integer[] costs = {0, 1, 1};
        new PaymentGui(new Payment(000, costs, true));
    }

}
