import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.CertificateException;

public class testClientSSL {
    public static void main(String[] args)
    {
        // Version non sécurisée : Socket cliSock = null;
        // *** Version sécurisée ***
        SSLSocket SslSocket = null;
        ObjectInputStream ois=null; ObjectOutputStream oos=null;
        String ligneDuServeur;
        try
        {
            // Version non sécurisée : cliSock = new Socket("claude", 6000);
            // *** Version sécurisée ***
            // 1. Keystore
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
            ois = new ObjectInputStream (SslSocket.getInputStream());
            oos = new ObjectOutputStream(SslSocket.getOutputStream());

            String reponse = (String) ois.readObject();
            System.out.println("reponse serveur = " + reponse);
            oos.writeObject("Salut");
            /*StringBuffer buf = new StringBuffer(50);
            int c;
            while ( (ligneDuServeur=ois.readLine()) != null )
            {
                System.out.println("Reçu du serveur : " + ligneDuServeur);
                if (ligneDuServeur.indexOf("FIN.")==-1)
                {
                    while((c = System.in.read())!='\n') if (c != '\r') buf.append((char)c);
                    System.out.println("Envoyé au serveur : " + buf.toString());
                    oos.write(buf.toString()+"\n"); oos.flush(); buf.setLength(0);
                }
            }*/
            oos.close(); ois.close(); SslSocket.close(); System.out.println("Client déconnecté");
        }
        catch (UnknownHostException e)
        { System.err.println("Erreur ! Host non trouvé [" + e + "]"); }
        catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e)
        { System.err.println("Erreur ! Pas de connexion ? [" + e + "]");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
