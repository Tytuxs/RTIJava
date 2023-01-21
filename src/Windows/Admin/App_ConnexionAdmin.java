package Windows.Admin;

import Classe.Utilisateur;

import javax.net.ssl.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

public class App_ConnexionAdmin extends JDialog {
    private JPanel panel1;
    private JButton buttonConnexion;
    private JButton buttonQuitter;
    private JTextField textFieldUtilisateur;
    private JTextField textFieldMotDePasse;
    private JButton buttonConnexionSSL;
    Socket s = null;
    SSLSocket SslSocket = null;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public App_ConnexionAdmin() throws IOException {

        InetAddress ip = InetAddress.getByName("localhost");
        Utilisateur utilisateur = new Utilisateur();

        buttonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    s = new Socket(ip, 9000);
                    System.out.println(s);
                    oos = new ObjectOutputStream(s.getOutputStream());
                    ois = new ObjectInputStream(s.getInputStream());
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

        buttonConnexionSSL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    KeyStore ServerKs = KeyStore.getInstance("JKS");
                    String FICHIER_KEYSTORE = "C:\\Users\\olico\\Desktop\\Bloc 3 2022-2023\\Complément réseau\\keystore\\clientAdminSecuKeystore.jks";
                    char[] PASSWD_KEYSTORE = "olivier".toCharArray();
                    FileInputStream ServerFK = new FileInputStream(FICHIER_KEYSTORE);
                    ServerKs.load(ServerFK, PASSWD_KEYSTORE);
                    // 2. Contexte
                    SSLContext SslC = SSLContext.getInstance("TLSv1.2");
                    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                    char[] PASSWD_KEY = "olivier".toCharArray();
                    kmf.init(ServerKs, PASSWD_KEY);
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                    tmf.init(ServerKs);
                    SslC.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                    // 3. Factory
                    SSLSocketFactory SslSFac= SslC.getSocketFactory();
                    // 4. Socket
                    SslSocket = (SSLSocket) SslSFac.createSocket(ip,9010);
                    oos = new ObjectOutputStream(SslSocket.getOutputStream());
                    ois = new ObjectInputStream (SslSocket.getInputStream());

                    oos.writeObject("LOGINA");
                    utilisateur.set_nomUser(textFieldUtilisateur.getText());
                    utilisateur.set_password(textFieldMotDePasse.getText());
                    oos.writeObject(utilisateur);

                    String reponse = (String) ois.readObject();
                    System.out.println("reponse : ");
                    System.out.println("reponse = " + reponse);
                    if (reponse.equals("OKADMIN")) {
                        System.out.println("Connexion OKADMIN");
                        App_ActionAdmin app_actionAdmin = new App_ActionAdmin(SslSocket,oos,ois);
                        app_actionAdmin.setVisible(true);
                        App_ConnexionAdmin.super.dispose();
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (UnrecoverableKeyException ex) {
                    ex.printStackTrace();
                } catch (CertificateException ex) {
                    ex.printStackTrace();
                } catch (KeyStoreException ex) {
                    ex.printStackTrace();
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                } catch (KeyManagementException ex) {
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
                    if(s != null)
                        s.close();
                    if(SslSocket != null)
                        SslSocket.close();
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
