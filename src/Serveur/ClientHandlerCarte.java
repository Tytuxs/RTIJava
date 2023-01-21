package Serveur;

import Classe.Carte;
import Classe.SourceTaches;
import database.facility.BD_Bean;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandlerCarte extends Thread {
    private final SourceTaches tachesAExecuter;
    private Socket tacheEnCours;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    BD_Bean BC;

    public ClientHandlerCarte(SourceTaches tachesAFaire, BD_Bean BC) {
        this.tachesAExecuter = tachesAFaire;
        this.BC = BC;
    }

    @Override
    public void run() {
        String received;
        int connexion = 1;
        while (connexion == 1) {
            BC.setTable("");
            BC.setColumns("");
            BC.setValues("");
            try {
                //attends de recevoir un client
                System.out.println("Avant tachesEnCours getTache()");
                tacheEnCours = tachesAExecuter.getTache();


                //creation des flux
                ois = new ObjectInputStream(tacheEnCours.getInputStream());
                oos = new ObjectOutputStream(tacheEnCours.getOutputStream());
                // RECEPTION DE LA REPONSE DU CLIENT
                received = (String) ois.readObject();
                System.out.println("received = " + received);

                if (received.equals("Exit")) {
                    System.out.println("Client " + this.tacheEnCours + " quitte...");
                    System.out.println("Fermeture connexion.");
                    connexion = 0;
                }

                if(received.equals("VERIFICATION")) {
                    oos.writeObject("OK");
                    Carte verifCarte = (Carte) ois.readObject();
                    System.out.println("nomClient = " + verifCarte.get_nomClient());
                    System.out.println("numeroCarte = " + verifCarte.get_numeroCarte());
                    System.out.println("mdp = " + verifCarte.get_mdp());

                    BC.setTable("Carte");
                    BC.setCondition("nomClient = '" + verifCarte.get_nomClient() + "' and " + "mdp = '" + verifCarte.get_mdp() + "' and numeroCarte = '" + verifCarte.get_numeroCarte() + "'");
                    ResultSet rs = BC.Select(false);
                    int compteur = 0;
                    while (rs.next()) {
                        compteur++;
                    }

                    if (compteur == 1) {
                        //contacter serveurBanque en SSL/TLS
                        SSLSocket SslSocket = null;
                        ObjectInputStream oisBanque=null; ObjectOutputStream oosBanque=null;
                        KeyStore ServerKs = KeyStore.getInstance("JKS");
                        String FICHIER_KEYSTORE = "C:\\Users\\olico\\Desktop\\Bloc 3 2022-2023\\Complément réseau\\keystore\\clientBanqueKeystore.jks";
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
                        SslSocket = (SSLSocket) SslSFac.createSocket(InetAddress.getLocalHost(),10000);
                        oosBanque = new ObjectOutputStream(SslSocket.getOutputStream());
                        oisBanque = new ObjectInputStream (SslSocket.getInputStream());

                        oosBanque.writeObject("VERIFICATION MDP");
                        oosBanque.writeObject(verifCarte);

                        String confirmation = (String) oisBanque.readObject();
                        //reponse au clienthandlerpaiement du serveur Paiement
                        if(confirmation.equals("OK")) {
                            oos.writeObject("OK");
                        }
                        else {
                            oos.writeObject("NOK");
                        }
                    }
                    else {
                        oos.writeObject("NOK");
                    }
                }
                else {
                    oos.writeObject("NOK");
                }
                oos.close();
                ois.close();
                tacheEnCours.close();
            } catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {
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
        }
    }
}
