package Windows.Chat;

import Classe.Chat;
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

public class App_ConnexionChat extends JDialog {

    private JPanel panelChat;
    private JButton buttonConnexion;
    private JButton buttonQuitter;
    private JTextField textFieldUtilisateur;
    private JTextField textFieldMDP;

    public App_ConnexionChat() throws IOException {

        InetAddress ip = InetAddress.getByName("localhost");

        Socket s = new Socket(ip, 11000);
        System.out.println(s);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        Utilisateur utilisateur = new Utilisateur();

        buttonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("LOGIN_GROUP");
                    utilisateur.set_nomUser(textFieldUtilisateur.getText());
                    utilisateur.set_password(textFieldMDP.getText());
                    oos.writeObject(utilisateur);

                    String reponse = (String) ois.readObject();
                    System.out.println("reponse : ");
                    System.out.println("reponse = " + reponse);
                    if (reponse.equals("OK")) {
                        System.out.println("Connexion OK");
                        Chat chat = (Chat) ois.readObject();
                        s.close();
                        ois.close();
                        oos.close();
                        App_MenuChat app_menuChat = new App_MenuChat(chat);
                        app_menuChat.setVisible(true);
                        App_ConnexionChat.super.dispose();
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
                App_ConnexionChat.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelChat);
        this.pack();
    }
}
