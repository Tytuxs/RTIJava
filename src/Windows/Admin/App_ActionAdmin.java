package Windows.Admin;

import Classe.ReserActCha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class App_ActionAdmin extends JDialog {
    private JButton buttonStop;
    private JPanel panel1;
    private JButton buttonListClient;
    private JButton buttonPause;
    private JButton buttonQuitter;

    public App_ActionAdmin(Socket s, ObjectOutputStream oos, ObjectInputStream ois) {

        buttonListClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    App_LCLIENTS app_lclients = new App_LCLIENTS(s, oos, ois);
                    app_lclients.setVisible(true);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });

        buttonPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("PAUSE");
                    String reponse = (String) ois.readObject();
                    JOptionPane.showMessageDialog(null, reponse, "Alert", JOptionPane.WARNING_MESSAGE);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("STOP");
                    String reponse = (String) ois.readObject();
                    JOptionPane.showMessageDialog(null, reponse, "Alert", JOptionPane.WARNING_MESSAGE);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("Exit");
                    ois.close();
                    oos.close();
                    s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                App_ActionAdmin.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panel1);
        this.pack();
    }
}
