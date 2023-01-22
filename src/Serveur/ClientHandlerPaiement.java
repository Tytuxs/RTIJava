package Serveur;

import Classe.Carte;
import Classe.ReserActCha;
import Classe.SourceTaches;
import ClassesCrypto.RequeteDigest;
import ClassesCrypto.RequeteSignature;
import database.facility.BD_Bean;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class ClientHandlerPaiement extends Thread {
    private final SourceTaches tachesAExecuter;
    private Socket tacheEnCours = null;
    private ObjectInputStream oisReservation = null;
    private ObjectOutputStream oosReservation = null;
    private Socket sCarte;
    private ObjectInputStream oisCarte;
    private ObjectOutputStream oosCarte;
    BD_Bean BP;
    private static String codeProvider = "BC";
    private int ServeurResa = 0;
    private SecretKey secretKey;
    private Cipher cipherSymetrique;

    public ClientHandlerPaiement(SourceTaches tachesAFaire, BD_Bean BP) {
        this.tachesAExecuter = tachesAFaire;
        this.BP = BP;
    }

    public void Stop() throws IOException {
        if(tacheEnCours != null) {
            if(this.oosReservation != null)
                this.oosReservation.close();
            if(this.oisReservation != null)
                this.oisReservation.close();
            this.tacheEnCours.close();
        }
    }

    @Override
    public void run() {
        Security.addProvider(new BouncyCastleProvider());
        String received;
        int connexion = 1;
        while (connexion == 1) {
            try {
                //attends de recevoir un client
                System.out.println("Avant tachesEnCours getTache()");
                tacheEnCours = tachesAExecuter.getTache();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ServeurPaiement : Boucle connexion");
            try {
                //creation des flux
                oisReservation = new ObjectInputStream(tacheEnCours.getInputStream());
                oosReservation = new ObjectOutputStream(tacheEnCours.getOutputStream());
                // RECEPTION DE LA REPONSE DU CLIENT
                received = (String) oisReservation.readObject();
                System.out.println("received = " + received);

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.tacheEnCours + " quitte...");
                    System.out.println("Fermeture connexion.");
                    System.out.println("Connexion fermée");
                    connexion = 0;
                }

                int ok = 0;
                if(received.equals("LOGIN")) {
                    /*Utilisateur user = (Utilisateur) oisReservation.readObject();

                    ResultSet rs = this.BP.Login();
                    while (rs.next()) {
                        String userbd = rs.getString(2);
                        String pwdbd = rs.getString(3);

                        if (user.get_nomUser().equals(userbd) && user.get_password().equals(pwdbd)) {
                            System.out.println("Client trouve");
                            ok = 1;
                            break;
                        }
                    }*/
                    RequeteDigest user = (RequeteDigest) oisReservation.readObject();
                    ResultSet rs = this.BP.Login();
                    while (rs.next()) {
                        String userbd = rs.getString(2);
                        String pwdbd = rs.getString(3);

                        if (user.getUtilisateur().equals(userbd)) {
                            //verif de pwdbd en creant un digest local
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            DataOutputStream bdos = new DataOutputStream(baos);
                            bdos.writeLong(user.getTemps());
                            bdos.writeDouble(user.getNbAlea());

                            MessageDigest md = MessageDigest.getInstance("SHA-1", codeProvider);
                            md.update(user.getUtilisateur().getBytes());
                            md.update(pwdbd.getBytes());
                            md.update(baos.toByteArray());

                            byte[] mdLocal = md.digest();
                            //comparaison digest recu avec digest local
                            if(MessageDigest.isEqual(user.getMdp(),mdLocal)) {
                                ok = 1;
                                ServeurResa = 0;
                            }
                            System.out.println("message recu = " + Arrays.toString(user.getMdp()));
                            System.out.println("message local = " + Arrays.toString(mdLocal));
                            break;
                        }
                    }
                }

                if(received.equals("SERVEURRESA")) {
                    System.out.println("Connexion avec ServeurReservation");
                    ok=1;
                    ServeurResa = 1;
                }
                int continuer = 1;
                if(ok == 1) {
                    oosReservation.writeObject("OK");//C:\Users\olico\Desktop\Bloc 3 2022-2023\RTI\Keystore

                    if(ServeurResa == 0) {
                        //Cryptage asymetrique avec Keystore(contient clé privé et certificat(cle publique))
                        RequeteSignature reqs = (RequeteSignature) oisReservation.readObject();
                        System.out.println("Message reçu = " + reqs.getMessage());

                        byte[] message = reqs.getMessage().getBytes();
                        byte[] signature = reqs.getSignature();

                        System.out.println("\nVérification de la signature");
                        KeyStore ksv = null;
                        ksv = KeyStore.getInstance("PKCS12", codeProvider);
                        ksv.load(new FileInputStream("C:\\Users\\olico\\Desktop\\Bloc 3 2022-2023\\RTI\\Keystore\\carteid_keystore.p12"),
                                "olivier".toCharArray());
                        System.out.println("Recuperation de la clé Privée");
                        PrivateKey key= (PrivateKey) ksv.getKey("serveur","olivier".toCharArray());
                        System.out.println("Recuperation du certificat");
                        X509Certificate certif = (X509Certificate)ksv.getCertificate("clientcert");



                        System.out.println("Recuperation de la cle publique du client");
                        PublicKey clePublique = certif.getPublicKey();

                        System.out.println("*** Cle publique recuperee = "+clePublique.toString());
                        System.out.println("Debut de verification de la signature construite");
                        // confection d'une signature locale
                        Signature sign = Signature.getInstance ("SHA1withRSA", codeProvider);
                        sign.initVerify(clePublique);
                        System.out.println("Hachage du message");
                        sign.update(message);
                        System.out.println("Verification de la signature construite");
                        boolean temp = sign.verify(signature);
                        String reponse;
                        if (temp) {
                            reponse = new String("OK");
                            continuer = 1;
                        }
                        else {
                            reponse = new String("NOK");
                            continuer = 0;
                        }
                        oosReservation.writeObject(reponse);
                        //test envoie clé symétrique avec cryptage asymetrique
                        // Génération des clés publique/privée du serveur
                        /*KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                        keyGen.initialize(2048);
                        KeyPair serverKeys = keyGen.generateKeyPair();

                        // Le serveur reçoit la clé publique du client et envoie sa clé publique en retour
                        PublicKey clientPublicKey = (PublicKey) oisReservation.readObject();
                        oosReservation.writeObject(serverKeys.getPublic());*/

                        // Le serveur déchiffre la clé secrète avec sa clé privée
                        byte[] encryptedKey = (byte[]) oisReservation.readObject();
                        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                        cipher.init(Cipher.DECRYPT_MODE, key);
                        byte[] decryptedKey = cipher.doFinal(encryptedKey);
                        secretKey = new SecretKeySpec(decryptedKey, "AES");
                    }

                    while (continuer == 1) {
                        System.out.println("Boucle continuer");

                        //reception requete cryptee du client
                        cipherSymetrique = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        byte[] requeteByteCryptee = (byte[]) oisReservation.readObject();

                        cipherSymetrique.init(Cipher.DECRYPT_MODE, secretKey);
                        byte[] requeteByte = cipherSymetrique.doFinal(requeteByteCryptee);
                        String requete = new String(requeteByte);

                        //reset a chaque requete pour eviter les erreurs entre les différentes requete d'un meme lance l'une à la suite de l'autre
                        BP.setTable("");
                        BP.setColumns("");
                        BP.setValues("");

                        //ATTENTE DE LA REQUÊTE du SERVEURRESERVATION ou de APPLIPaiement
                        //String requete = (String) oisReservation.readObject();
                        System.out.println("Requete recue : " + requete);
                        switch (requete) {
                            case "PROOMPAY" :
                                String nomClient = (String) oisReservation.readObject();
                                String id = (String) oisReservation.readObject();
                                float paiement = (Float) oisReservation.readObject();
                                String CB = (String) oisReservation.readObject();
                                String CBMDP = (String) oisReservation.readObject();

                                //verification de la carteBancaire
                                InetAddress ip = InetAddress.getByName("localhost");
                                sCarte = new Socket(ip, 8000);
                                System.out.println("sPaiement = " + sCarte);
                                System.out.println("Connexion au ServeurCarte avec le ServeurPaiement");

                                //penser à inverser les flux si erreur de création
                                this.oosCarte = new ObjectOutputStream(this.sCarte.getOutputStream());
                                this.oisCarte = new ObjectInputStream(this.sCarte.getInputStream());

                                Carte carte = new Carte();
                                carte.set_nomClient(nomClient);
                                carte.set_numeroCarte(CB);
                                carte.set_mdp(CBMDP);

                                oosCarte.writeObject("VERIFICATION");
                                if(oisCarte.readObject().equals("OK")) {
                                    oosCarte.writeObject(carte);
                                    String confirmation = (String) oisCarte.readObject();
                                    if (confirmation.equals("OK")) {
                                        BP.setTable("ReserActCha");
                                        BP.setCondition("id = " + id);

                                        ResultSet rs = BP.Select(false);
                                        float dejaPaye = 0;

                                        while (rs.next()) {
                                            dejaPaye = rs.getFloat("dejaPaye");
                                        }

                                        paiement += dejaPaye;

                                        BP.setValues("dejaPaye = " + paiement);
                                        System.out.println("avant Update");
                                        int confir = BP.Update();
                                        System.out.println("apres Update");
                                        if (confir == 1) {
                                            oosReservation.writeObject("OK");
                                        } else {
                                            oosReservation.writeObject("NOK");
                                        }
                                    } else {
                                        oosReservation.writeObject("Erreur carte bancaire");
                                    }
                                }
                                oisCarte.close();
                                oosCarte.close();
                                sCarte.close();
                                break;

                            case "LISTPAY" :
                                BP.setTable("Reseractcha");
                                BP.setCondition("type = 'Chambre' and dejapaye < prixCha");
                                ResultSet resultatLISTPAY = BP.Select(false);
                                while (resultatLISTPAY.next()) {
                                    ReserActCha reservation = new ReserActCha();
                                    reservation.set_id(resultatLISTPAY.getInt("id"));
                                    reservation.set_numChambre(resultatLISTPAY.getInt("numChambre"));
                                    reservation.set_prixCha(resultatLISTPAY.getFloat("prixCha"));
                                    reservation.set_persRef(resultatLISTPAY.getString("PersRef"));
                                    reservation.set_dejaPaye(resultatLISTPAY.getFloat("dejaPaye"));

                                    oosReservation.writeObject(reservation);
                                }
                                oosReservation.writeObject(null);

                                break;

                            case "ROOMPAY" :
                                //verification de la carteBancaire
                                InetAddress IP = InetAddress.getByName("localhost");
                                sCarte = new Socket(IP, 8000);
                                System.out.println("sPaiement = " + sCarte);
                                System.out.println("Connexion au ServeurCarte avec le ServeurPaiement");

                                //penser à inverser les flux si erreur de création
                                this.oosCarte = new ObjectOutputStream(this.sCarte.getOutputStream());
                                this.oisCarte = new ObjectInputStream(this.sCarte.getInputStream());

                                String ID = (String) oisReservation.readObject();
                                Carte verifcarte = (Carte)oisReservation.readObject();
                                float paie = verifcarte.getPaiement();

                                oosCarte.writeObject("VERIFICATION");
                                if(oisCarte.readObject().equals("OK")) {
                                    oosCarte.writeObject(verifcarte);
                                    String confirmation = (String) oisCarte.readObject();
                                    if (confirmation.equals("OK")) {
                                        BP.setTable("ReserActCha");
                                        BP.setCondition("id = " + ID);

                                        ResultSet rs = BP.Select(false);
                                        float dejaPaye = 0;

                                        while (rs.next()) {
                                            dejaPaye = rs.getFloat("dejaPaye");
                                        }

                                        paie += dejaPaye;

                                        BP.setValues("dejaPaye = " + paie);
                                        System.out.println("avant Update");
                                        int confir = BP.Update();
                                        System.out.println("apres Update");
                                        if (confir == 1) {
                                            oosReservation.writeObject("OK");
                                            //envoie num transaction financière crypté
                                            cipherSymetrique = Cipher.getInstance("AES/ECB/PKCS5Padding");
                                            byte[] numCrypte = String.valueOf(Math.random()).getBytes();
                                            System.out.println("num transaction = " + new String(numCrypte));
                                            cipherSymetrique.init(Cipher.ENCRYPT_MODE, secretKey);
                                            byte[] requeteNumCrypte = cipherSymetrique.doFinal(numCrypte);
                                            oosReservation.writeObject(requeteNumCrypte);
                                            //reception d'un HMAC pour confirmer
                                            byte[] hmacb = (byte[]) oisReservation.readObject();

                                            Mac hlocal = Mac.getInstance("HMAC-MD5", codeProvider);
                                            hlocal.init(secretKey);
                                            System.out.println("Hachage du message");
                                            hlocal.update(numCrypte);
                                            hlocal.update(verifcarte.get_nomClient().getBytes());
                                            System.out.println("Verification des HMACS");
                                            byte[] hlocalb = hlocal.doFinal();

                                            if(MessageDigest.isEqual(hmacb,hlocalb)) {
                                                oosReservation.writeObject("OK");
                                            }
                                            else {
                                                oosReservation.writeObject("NOK");
                                            }

                                        } else {
                                            oosReservation.writeObject("NOK");
                                        }
                                    } else {
                                        oosReservation.writeObject("Erreur carte bancaire");
                                    }
                                }
                                oisCarte.close();
                                oosCarte.close();
                                sCarte.close();
                                break;

                            case "LOGOUT" :
                                //oosReservation.writeObject("Au revoir");
                                continuer = 0;
                                break;

                            case "Exit" :
                                //oosReservation.writeObject("Au revoir");
                                continuer = 0;
                                connexion = 0;
                                break;

                            default:
                                //oosReservation.writeObject("ERROR : Invalid input");
                                break;
                        }
                    }
                }
                else {
                    System.out.println("NOK");
                    oosReservation.writeObject("NOK");
                }
            }
            catch (IOException | SQLException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            }
        }

        try
        {
            System.out.println("Fermeture des ressources");
            this.tacheEnCours.close();
            this.oisReservation.close();
            this.oosReservation.close();
            this.oisReservation = null;
            this.oosReservation = null;
            this.tacheEnCours = null;
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
