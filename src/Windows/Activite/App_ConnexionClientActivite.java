package Windows.Activite;

import Classe.Utilisateur;
import Windows.App_ConnexionClient;
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

public class App_ConnexionClientActivite extends JDialog {
    private JPanel ClientActivitePanel;
    private JTextField textFieldUtilisateur;
    private JTextField textFieldMDP;
    private JButton buttonConnexion;
    private JButton buttonQuitter;
    private JLabel labelExplication;

    public App_ConnexionClientActivite() throws IOException {

        InetAddress ip = InetAddress.getByName("localhost");

        Socket s = new Socket(ip, 6000);
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
                    utilisateur.set_password(textFieldMDP.getText());
                    oos.writeObject(utilisateur);

                    String reponse = (String) ois.readObject();
                    System.out.println("reponse : ");
                    System.out.println("reponse = " + reponse);
                    if (reponse.equals("OK")) {
                        System.out.println("Connexion OK");
                        App_ActiviteClient app_activiteClient = new App_ActiviteClient(s,oos,ois);
                        app_activiteClient.setVisible(true);
                        App_ConnexionClientActivite.super.dispose();
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
                App_ConnexionClientActivite.super.dispose();
            }
        });


        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(ClientActivitePanel);
        this.pack();
    }
}
