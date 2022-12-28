package Windows.Paiement;

import Classe.Utilisateur;
import ClassesCrypto.RequeteDigest;
import Windows.App_ReservationClient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;

public class App_ConnexionClientPaiement extends JDialog {
    private JPanel clientPaiementPanel;
    private JButton buttonConnexion;
    private JTextField textFieldUtilisateur;
    private JButton buttonQuitter;
    private JTextField textFieldMotDePasse;
    private static String codeProvider = "BC";

    public App_ConnexionClientPaiement() throws IOException {

        Security.addProvider(new BouncyCastleProvider());
        InetAddress ip = InetAddress.getByName("localhost");

        Socket s = new Socket(ip, 7000);
        System.out.println(s);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        Utilisateur utilisateur = new Utilisateur();

        buttonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("LOGIN");
                    /*utilisateur.set_nomUser(textFieldUtilisateur.getText());
                    utilisateur.set_password(textFieldMotDePasse.getText());
                    oos.writeObject(utilisateur);*/
                    long temps = (new Date()).getTime();
                    double nbAlea = Math.random();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream bdos = new DataOutputStream(baos);
                    bdos.writeLong(temps);
                    bdos.writeDouble(nbAlea);

                    MessageDigest md = MessageDigest.getInstance("SHA-1", codeProvider);
                    md.update(textFieldUtilisateur.getText().getBytes());
                    md.update(textFieldMotDePasse.getText().getBytes());
                    md.update(baos.toByteArray());


                    RequeteDigest req = new RequeteDigest(md.digest(),textFieldUtilisateur.getText(),temps,nbAlea);
                    oos.writeObject(req);

                    String reponse = (String) ois.readObject();
                    System.out.println("reponse : ");
                    System.out.println("reponse = " + reponse);
                    if (reponse.equals("OK")) {
                        System.out.println("Connexion OK");
                        App_LISTPAY app_listpay = new App_LISTPAY(s, oos, ois);
                        app_listpay.setVisible(true);
                        App_ConnexionClientPaiement.super.dispose();
                    }

                } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchProviderException ex) {
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
