package Windows.Chat;

import Classe.Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class App_MenuChat extends JDialog {
    private JPanel panelMenuChat;
    private JButton buttonRejoindre;
    private JButton buttonQuitter;
    private JButton buttonEnvoyer;
    private JTextField textFieldMessage;
    private JTextField textFieldUtiliateur;
    private JTable tableMessage;

    private InetAddress adresseGroupe;
    private MulticastSocket multicastSocket;
    private ThreadReception thrReception;

    public App_MenuChat(Chat chat) throws IOException {
        buttonRejoindre.setEnabled(true);
        buttonEnvoyer.setEnabled(false);
        buttonQuitter.setEnabled(false);

        buttonRejoindre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println(chat.getPORT_CHAT());
                    System.out.println(chat.getIp());
                    adresseGroupe = InetAddress.getByName(chat.getIp());
                    multicastSocket = new MulticastSocket(chat.getPORT_CHAT());
                    System.out.println(adresseGroupe);
                    System.out.println(multicastSocket);
                    multicastSocket.joinGroup(adresseGroupe);
                    thrReception = new ThreadReception(textFieldUtiliateur.getText(),multicastSocket,tableMessage);
                    thrReception.start();

                    String message = textFieldUtiliateur.getText() + " rejoint le groupe";
                    DatagramPacket dtg = new DatagramPacket(message.getBytes(),message.length(),adresseGroupe,chat.getPORT_CHAT());
                    multicastSocket.send(dtg);
                    buttonRejoindre.setEnabled(false);
                    buttonEnvoyer.setEnabled(true);
                    buttonQuitter.setEnabled(true);

                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = textFieldUtiliateur.getText() + " quitte le groupe";
                DatagramPacket dtg = new DatagramPacket(msg.getBytes(), msg.length(), adresseGroupe, chat.getPORT_CHAT());
                try
                {
                    multicastSocket.send(dtg);
                    thrReception.stop();
                    buttonRejoindre.setEnabled(true);
                    buttonEnvoyer.setEnabled(false);
                    buttonQuitter.setEnabled(false);
                    multicastSocket.leaveGroup(adresseGroupe);
                    System.out.println("Après leaveGroup");
                    multicastSocket.close();
                    System.out.println("Après close");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonEnvoyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = textFieldUtiliateur.getText() + "> " + textFieldMessage.getText();
                DatagramPacket dtg = new DatagramPacket(msg.getBytes(), msg.length(), adresseGroupe, chat.getPORT_CHAT());
                try {
                    multicastSocket.send(dtg);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelMenuChat);
        this.pack();
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

