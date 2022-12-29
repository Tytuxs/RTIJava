import Windows.App_ConnexionServer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;
import java.sql.SQLException;

public class MainServer {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()-> {
            try {
                new App_ConnexionServer().setVisible(true);

                /*Security.addProvider(new BouncyCastleProvider());
                KeyPairGenerator genCles = KeyPairGenerator.getInstance("RSA",
                        "BC");
                System.out.println("Tentative d'initialisation du generateur de cle");
                int se = 512; // par exemple
                genCles.initialize(se, new SecureRandom());
                KeyPair deuxCles = genCles.generateKeyPair();
                PublicKey clePublique = deuxCles.getPublic();
                PrivateKey clePrivee = deuxCles.getPrivate();
                System.out.println(" *** Cle publique generee = " + clePublique);
                System.out.println(" *** Cle privee generee = " + clePrivee);
                // Sérialisation de clés
                System.out.println(" *** Cle publique generee serialisee");
                ObjectOutputStream clePubliqueFich = new ObjectOutputStream(
                        new FileOutputStream("C:\\Users\\olico\\Desktop\\Bloc 3 2022-2023\\RTI\\Keystore\\clePubliqueClient.ser"));
                System.out.println("fichier ouvert");
                clePubliqueFich.writeObject(clePublique);
                System.out.println("cle ecrite");
                clePubliqueFich.close();
                System.out.println(" *** Cle privee generee serialisee");
                ObjectOutputStream clePriveeFich = new ObjectOutputStream(
                        new FileOutputStream("C:\\Users\\olico\\Desktop\\Bloc 3 2022-2023\\RTI\\Keystore\\clePriveeClient.ser"));
                clePriveeFich.writeObject(clePrivee);
                clePriveeFich.close();*/
            } catch (SQLException e) {
                e.printStackTrace();
            } /*catch (NoSuchAlgorithmException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }*/
        });
    }
}