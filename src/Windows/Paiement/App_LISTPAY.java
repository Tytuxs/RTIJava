package Windows.Paiement;

import Classe.ReserActCha;
import ClassesCrypto.RequeteSignature;

import javax.crypto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Vector;

public class App_LISTPAY extends JDialog {
    private JTable tableClients;
    private JPanel panelLISTPAY;
    private JButton buttonQuitter;
    private JButton buttonValider;

    private static String codeProvider = "BC";
    private Cipher cipherSymetrique;
    DefaultTableModel JTable_Affichage = new DefaultTableModel();

    public App_LISTPAY(Socket s, ObjectOutputStream oos, ObjectInputStream ois, Socket sUrgence, ObjectOutputStream oosUrgence, ObjectInputStream oisUrgence) throws IOException, ClassNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, SignatureException, CertificateException, KeyStoreException, UnrecoverableKeyException {

        AttenteUrgent threadUrgent = new AttenteUrgent(sUrgence, oosUrgence, oisUrgence);
        threadUrgent.start();

        KeyStore ks = null;
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("C:\\Users\\olico\\Desktop\\Bloc 3 2022-2023\\RTI\\Keystore\\java_keystore.jks"), "olivier".toCharArray());

        //recuperation du certificat du serveur
        X509Certificate certif = (X509Certificate)ks.getCertificate("serveurcert");
        System.out.println("Recuperation de la cle publique");
        PublicKey clePublique = certif.getPublicKey();

        String Message = "Code du jour : CVCCDMMM - bye";
        System.out.println("Message a envoyer au serveur : " + Message);
        byte[] message = Message.getBytes();
        PrivateKey clePrivee;
        System.out.println("Recuperation de la cle privee");
        clePrivee = (PrivateKey) ks.getKey("client", "olivier".toCharArray());
        System.out.println(" *** Cle privee recuperee = " + clePrivee.toString());

        System.out.println("Instanciation de la signature");
        Signature signature = Signature. getInstance("SHA1withRSA",codeProvider);
        System.out.println("Initialisation de la signature");
        signature.initSign(clePrivee);
        System.out.println("Hachage du message");
        signature.update(message);
        System.out.println("Generation des bytes");
        byte[] signatureb = signature.sign();
        System.out.println("Termine : signature construite");
        System.out.println("Signature = " + new String(signatureb));
        System.out.println("Longueur de la signature = " + signatureb.length);
        System.out.println("Envoi du message et de la signature");

        RequeteSignature reqs = new RequeteSignature(Message,signatureb);
        oos.writeObject(reqs);
        String messageRetour = (String) ois.readObject();
        System.out.println("messageRetour = " + messageRetour);

        // Génération des clés publique/privée du client
        /*KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair clientKeys = keyGen.generateKeyPair();

        // Le client envoie sa clé publique au serveur et reçoit celle du serveur
        oos.writeObject(clientKeys.getPublic());
        PublicKey serverPublicKey = (PublicKey) ois.readObject();*/

        // Le client génère une clé secrète aléatoire et la chiffre avec la clé publique du serveur
        KeyGenerator keyGen2 = KeyGenerator.getInstance("AES");
        keyGen2.init(256);
        SecretKey secretKey = keyGen2.generateKey();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, clePublique);
        byte[] encryptedKey = cipher.doFinal(secretKey.getEncoded());

        // Le client envoie la clé secrète chiffrée au serveur
        oos.writeObject(encryptedKey);

        //envoie requete LISTPAY
        cipherSymetrique = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipherSymetrique.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] plaintext = "LISTPAY".getBytes();
        byte[] ciphertext = cipherSymetrique.doFinal(plaintext);
        oos.writeObject(ciphertext);


        JTable_Affichage.setRowCount(0);
        JTable_Affichage.setColumnCount(5);
        Vector V = new Vector<>();
        V.add("id");
        V.add("Numero Chambre");
        V.add("Prix de la chambre");
        V.add("Personne référée");
        V.add("Prix restant");
        JTable_Affichage.addRow(V);

        while (true) {
            ReserActCha reservation = (ReserActCha) ois.readObject();
            if(reservation==null)
                break;
            else {
                Vector v = new Vector();
                v.add(reservation.get_id());
                v.add(reservation.get_numChambre());
                v.add(reservation.get_prixCha());
                v.add(reservation.get_persRef());
                float restant = reservation.get_prixCha() - reservation.get_dejaPaye();
                if(restant <= 0) {
                    restant=0;
                }
                v.add(restant);
                JTable_Affichage.addRow(v);
            }
        }

        tableClients.setModel(JTable_Affichage);

        buttonValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (s.isClosed()) {
                    JOptionPane.showMessageDialog(null, "Erreur de connexion au serveur Paiement", "Alert", JOptionPane.WARNING_MESSAGE);
                    App_LISTPAY.super.dispose();
                } else {
                    System.out.println("element selectionne = " + tableClients.getSelectedRow());
                    if (tableClients.getSelectedRow() == -1) {
                        JOptionPane.showMessageDialog(null, "Selectionner une ligne du tableau", "Alert", JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            App_ROOMPAY app_roompay = new App_ROOMPAY(ois, oos, secretKey, tableClients.getValueAt(tableClients.getSelectedRow(), 0).toString());
                            app_roompay.setModal(true);
                            app_roompay.setVisible(true);
                            System.out.println("OKOKOKOKOKOKOKOKOK");

                            //envoie requete LISTPAY pour mettre a jour la table
                            cipherSymetrique = Cipher.getInstance("AES/ECB/PKCS5Padding");
                            cipherSymetrique.init(Cipher.ENCRYPT_MODE, secretKey);
                            byte[] plaintext = "LISTPAY".getBytes();
                            byte[] ciphertext = cipherSymetrique.doFinal(plaintext);
                            oos.writeObject(ciphertext);


                            JTable_Affichage.setRowCount(0);
                            JTable_Affichage.setColumnCount(5);
                            Vector V = new Vector<>();
                            V.add("id");
                            V.add("Numero Chambre");
                            V.add("Prix de la chambre");
                            V.add("Personne référée");
                            V.add("Prix restant");
                            JTable_Affichage.addRow(V);

                            while (true) {
                                ReserActCha reservation = (ReserActCha) ois.readObject();
                                if (reservation == null)
                                    break;
                                else {
                                    Vector v = new Vector();
                                    v.add(reservation.get_id());
                                    v.add(reservation.get_numChambre());
                                    v.add(reservation.get_prixCha());
                                    v.add(reservation.get_persRef());
                                    float restant = reservation.get_prixCha() - reservation.get_dejaPaye();
                                    if (restant <= 0) {
                                        restant = 0;
                                    }
                                    v.add(restant);
                                    JTable_Affichage.addRow(v);
                                }
                            }

                            tableClients.setModel(JTable_Affichage);
                        } catch (NoSuchAlgorithmException ex) {
                            ex.printStackTrace();
                        } catch (NoSuchPaddingException ex) {
                            ex.printStackTrace();
                        } catch (IllegalBlockSizeException ex) {
                            ex.printStackTrace();
                        } catch (BadPaddingException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (InvalidKeyException ex) {
                            ex.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }
        });

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (s.isClosed()) {
                    JOptionPane.showMessageDialog(null, "Erreur de connexion au serveur Paiement", "Alert", JOptionPane.WARNING_MESSAGE);
                    App_LISTPAY.super.dispose();
                } else {
                    try {
                        cipherSymetrique.init(Cipher.ENCRYPT_MODE, secretKey);
                        byte[] plaintext = "Exit".getBytes();
                        byte[] ciphertext = cipherSymetrique.doFinal(plaintext);
                        oos.writeObject(ciphertext);
                        ois.close();
                        oos.close();
                        s.close();
                        oosUrgence.close();
                        oisUrgence.close();
                        sUrgence.close();
                        App_LISTPAY.super.dispose();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (IllegalBlockSizeException ex) {
                        ex.printStackTrace();
                    } catch (BadPaddingException ex) {
                        ex.printStackTrace();
                    } catch (InvalidKeyException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelLISTPAY);
        this.pack();
    }

}
