package view.gui.actionHandler;

import network.Client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateNewGame implements ActionListener {
    private Client client;
    private JComboBox<String> rmi;
    private JTextField token;
    private JFrame frame;

    public CreateNewGame(Client client, JTextField token, JFrame frame){
        this.client = client;
        this.token = token;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
