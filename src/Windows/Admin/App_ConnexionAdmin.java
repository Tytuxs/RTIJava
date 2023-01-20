package Windows.Admin;

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

public class App_ConnexionAdmin extends JDialog {
    private JPanel panel1;
    private JButton buttonConnexion;
    private JButton buttonQuitter;
    private JTextField textFieldUtilisateur;
    private JTextField textFieldMotDePasse;

    public App_ConnexionAdmin() throws IOException {

        InetAddress ip = InetAddress.getByName("localhost");

        Socket s = new Socket(ip, 9000);
        System.out.println(s);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        Utilisateur utilisateur = new Utilisateur();

        buttonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("LOGINA");
                    utilisateur.set_nomUser(textFieldUtilisateur.getText());
                    utilisateur.set_password(textFieldMotDePasse.getText());
                    oos.writeObject(utilisateur);

                    String reponse = (String) ois.readObject();
                    System.out.println("reponse : ");
                    System.out.println("reponse = " + reponse);
                    if (reponse.equals("OKADMIN")) {
                        System.out.println("Connexion OKADMIN");
                        App_ActionAdmin app_actionAdmin = new App_ActionAdmin(s,oos,ois);
                        app_actionAdmin.setVisible(true);
                        App_ConnexionAdmin.super.dispose();
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
                App_ConnexionAdmin.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panel1);
        this.pack();
    }
}
