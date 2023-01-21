package Serveur;

import Classe.TachesBanque;
import database.facility.BD_Bean;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public class ServeurBanque extends Thread {
    private int PORT_BANK;
    private final TachesBanque tachesAFaire;
    private final BD_Bean BC;

    public ServeurBanque(int PORT) throws SQLException {
        setPort(PORT);
        tachesAFaire = new TachesBanque();
        BC = new BD_Bean("jdbc:mysql://localhost:3306/bd_comptes","root","pwdmysql");
    }

    public void setPort(int PORT) { this.PORT_BANK = PORT; }
    public int getPort()
    {
        return this.PORT_BANK;
    }

    @Override
    public void run() {
        /*ServerSocket ss = null;
        try {
            ss = new ServerSocket(getPort());
            for(int i=0; i<3;i++) {
                ClientHandlerCarte ThrClient = new ClientHandlerCarte(tachesAFaire, BC);
                ThrClient.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        while (true)
        {
            Socket s = null;
            try
            {
                assert ss != null;
                s = ss.accept();
                tachesAFaire.recordTache(s);
                System.out.println("Nouveau client connecte : " + s);
            }
            catch (Exception e){
                try {
                    assert s != null;
                    s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }*/
        for(int i=0; i<3;i++) {
            ClientHandlerBanque ThrClient = new ClientHandlerBanque(tachesAFaire, BC);
            ThrClient.start();
        }

        SSLServerSocket SslSSocket = null;
        SSLSocket SslSocket = null;
        try
        {
            // Version non sécurisée
            // SSocket = new ServerSocket(6000);
            // *** Version sécurisée ***
            // 1. Keystore
            KeyStore ServerKs = KeyStore.getInstance("JKS");
            String FICHIER_KEYSTORE = "C:\\Users\\olico\\Desktop\\Bloc 3 2022-2023\\Complément réseau\\keystore\\serveurBanqueKeystore.jks";
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
            SslSSocket = (SSLServerSocket) SslSFac.createServerSocket(10000);

            // Version non sécurisée
            // CSocket = SSocket.accept();
            // *** Version sécurisée ***
            while (true) {
                SslSocket = (SSLSocket) SslSSocket.accept();
                tachesAFaire.recordTache(SslSocket);
                System.out.println("Nouveau client connecte : " + SslSocket);
            }


        }
        catch (IOException e)
        {
            System.err.println("Erreur de fichier E/S ! ? [" + e + "]"); System.exit(1);
        }
        catch (KeyStoreException e)
        {
            System.err.println("Erreur de KeyStore ! ? [" + e + "]"); System.exit(1);
        }
        catch (NoSuchAlgorithmException e)
        {
            System.err.println("Erreur d'algorithme au chargement du KeyStore ! ? [" +e + "]");
            System.exit(1);
        }
        catch (CertificateException e)
        {
            System.err.println("Erreur de certificat au chargement du KeyStore ! ? [" + e + "]");
            System.exit(1);
        }
        catch (KeyManagementException e)
        {
            System.err.println("Erreur pour accéder aux Key managers ! ? [" + e + "]");
            System.exit(1);
        }
        catch (UnrecoverableKeyException e)
        {
            System.err.println("Erreur d'initialisation du KeyManagerFactory ! ? [" + e + "]");
            System.exit(1);
        }
    }
}