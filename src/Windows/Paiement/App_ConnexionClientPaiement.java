package Windows.Paiement;

import Classe.Utilisateur;
import Windows.App_ReservationClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;

public class App_ConnexionClientPaiement extends JDialog {
    private JPanel clientPaiementPanel;
    private JButton buttonConnexion;
    private JTextField textFieldUtilisateur;
    private JButton buttonQuitter;
    private JTextField textFieldMotDePasse;
    private static String codeProvider = "BC";

    public App_ConnexionClientPaiement() throws IOException {


        InetAddress ip = InetAddress.getByName("localhost");

        Socket s = new Socket(ip, 5056);
        System.out.println(s);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        Utilisateur utilisateur = new Utilisateur();

        buttonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MessageDigest md;
                    oos.writeObject("LOGIN");
                    utilisateur.set_nomUser(textFieldUtilisateur.getText());
                    utilisateur.set_password(textFieldMotDePasse.getText());
                    oos.writeObject(utilisateur);

                    String reponse = (String) ois.readObject();
                    System.out.println("reponse : ");
                    System.out.println("reponse = " + reponse);
                    if (reponse.equals("OK")) {
                        System.out.println("Connexion OK");
                        App_ReservationClient app_reservation = new App_ReservationClient(s, oos, ois);
                        app_reservation.setVisible(true);
                        App_ConnexionClientPaiement.super.dispose();
                    }


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
                App_ConnexionClientPaiement.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(clientPaiementPanel);
        this.pack();
    }
}
