package Windows;

import Classe.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class App_ConnexionClient extends JDialog{
    private JPanel ClientPanel;
    private JButton buttonConnexion;
    private JButton buttonQuitter;
    private JTextField textFieldUtilisateur;
    private JTextField textFieldMotDePasse;
    private JLabel labelExplication;

    public App_ConnexionClient() throws IOException {

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
                        App_ConnexionClient.super.dispose();
                    }
                    else {
                        labelExplication.setText("Utilisateur ou mot de passe incorrect");
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
                App_ConnexionClient.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(ClientPanel);
        this.pack();
    }
}
