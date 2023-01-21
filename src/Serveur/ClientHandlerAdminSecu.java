package Serveur;

import Classe.Utilisateur;
import database.facility.BD_Bean;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandlerAdminSecu extends Thread {

    SSLServerSocket SslSSocket = null;
    SSLSocket SslSocket = null;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private BD_Bean BA;
    private ArrayList<ClientHandlerUrgence> listThreadClient;
    private ArrayList<Socket> listSocketClient;
    private int PORT_ADMINS;

    public ClientHandlerAdminSecu(int PORTADMINS, ArrayList<ClientHandlerUrgence> liste, BD_Bean BA, ArrayList<Socket> listS) {
        this.PORT_ADMINS = PORTADMINS;
        this.listThreadClient = liste;
        this.BA = BA;
        this.listSocketClient=listS;
    }

    @Override
    public void run() {
        while (true) {
            try {
                KeyStore ServerKs = KeyStore.getInstance("JKS");
                String FICHIER_KEYSTORE = "C:\\Users\\olico\\Desktop\\Bloc 3 2022-2023\\Complément réseau\\keystore\\serveurPaiementKeystore.jks";
                char[] PASSWD_KEYSTORE = "olivier".toCharArray();
                FileInputStream ServerFK = new FileInputStream (FICHIER_KEYSTORE);
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
                SSLServerSocketFactory SslSFac= SslC.getServerSocketFactory();
                // 4. Socket
                SslSSocket = (SSLServerSocket) SslSFac.createServerSocket(PORT_ADMINS);
                SslSocket = (SSLSocket) SslSSocket.accept();
                System.out.println("ADMIN SECU ACCEPTE");
                oos = new ObjectOutputStream(SslSocket.getOutputStream());
                ois = new ObjectInputStream(SslSocket.getInputStream());

                int connexion = 1;
                while (connexion == 1) {
                    String received = (String) ois.readObject();
                    System.out.println("received = " + received);

                    if (received.equals("Exit")) {
                        System.out.println("ADMIN " + this.SslSocket + " quitte...");
                        System.out.println("Fermeture connexion.");
                        System.out.println("Connexion fermée");
                        connexion = 0;
                    }

                    int ok = 0;
                    if (received.equals("LOGINA")) {
                        Utilisateur user = (Utilisateur) ois.readObject();

                        ResultSet rs = this.BA.Login();
                        while (rs.next()) {
                            String userbd = rs.getString(2);
                            String pwdbd = rs.getString(3);

                            if (user.get_nomUser().equals(userbd) && user.get_password().equals(pwdbd)) {
                                System.out.println("Client trouve");
                                ok = 1;
                                break;
                            }
                        }
                    }

                    if (ok == 1) {
                        oos.writeObject("OKADMIN");
                        int continuer = 1;
                        while (continuer == 1) {
                            System.out.println("Boucle continuer");
                            String requete = (String) ois.readObject();
                            System.out.println("requete = " + requete);

                            switch (requete) {
                                case "LCLIENTS":
                                    //oosReservation.writeObject("Au revoir");
                                    ArrayList<Integer> listNumSocketASupp = new ArrayList<Integer>();
                                    for (int i = 0; i < listSocketClient.size(); i++) {
                                        if (listSocketClient.get(i).isClosed())
                                            listNumSocketASupp.add(i);
                                        else {
                                            String ip = listSocketClient.get(i).getInetAddress().getHostAddress();
                                            System.out.println("ip = " + ip);
                                            oos.writeObject(ip);
                                        }
                                    }
                                    oos.writeObject(null);
                                    //on supprime les socket fermees qui n'ont pas ete affichee
                                    for (int i = 0; i < listNumSocketASupp.size(); i++) {
                                        if (listNumSocketASupp.get(i) != null)
                                            listSocketClient.remove(listNumSocketASupp.get(i));
                                    }
                                    break;

                                case "PAUSE":
                                    //oosReservation.writeObject("Au revoir");
                                    for (int i = 0; i < listThreadClient.size(); i++) {
                                        if (listThreadClient.get(i).getConnecte() == 1)
                                            listThreadClient.get(i).sendMessage("Serveur est en pause");

                                        listThreadClient.get(i).interrupt();
                                    }
                                    oos.writeObject("Clients prévenus pour la pause du serveur");
                                    break;

                                case "STOP":
                                    //oosReservation.writeObject("Au revoir");
                                    for (int i = 0; i < listThreadClient.size(); i++) {
                                        listThreadClient.get(i).sendMessage("Serveur est DOWN");
                                        listThreadClient.get(i).interrupt();
                                    }
                                    oos.writeObject("Clients prévenus pouyr l'arrêt du serveur");
                                    break;

                                case "Exit":
                                    //oosReservation.writeObject("Au revoir");
                                    continuer = 0;
                                    connexion = 0;
                                    break;
                            }
                        }
                    } else {
                        System.out.println("NOK");
                        oos.writeObject("NOK");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            System.out.println("Fermeture des ressources");
            try {
                this.SslSSocket.close();
                this.SslSocket.close();
                this.ois.close();
                this.oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
