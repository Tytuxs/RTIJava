package Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class App_ConnexionClient extends JDialog {
    private JPanel ClientPanel;
    private JButton buttonConnexion;
    private JButton buttonQuitter;
    private JTextField textFieldUtilisateur;
    private JTextField textFieldMotDePasse;
    private JLabel labelExplication;

    public App_ConnexionClient() throws IOException {

        InetAddress ip = InetAddress.getByName("localhost");

        Socket s = new Socket(ip, 5056);

        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        buttonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("LOGIN");
                    dos.writeUTF(textFieldUtilisateur.getText());
                    dos.writeUTF(textFieldMotDePasse.getText());

                    String reponse = dis.readUTF();
                    System.out.println("reponse = " + reponse);
                    if (reponse.equals("OK")) {
                        System.out.println("Connexion OK");
                        App_ReservationClient app_reservation = new App_ReservationClient(s, dos, dis);
                        app_reservation.setVisible(true);
                        App_ConnexionClient.super.dispose();
                    }
                    else {
                        labelExplication.setText("Utilisateur ou mot de passe incorrect");
                    }


                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("Exit");
                    dis.close();
                    dos.close();
                    s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                App_ConnexionClient.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(ClientPanel);
        this.pack();
    }
}
