package Windows.Paiement;

import Classe.Carte;

import javax.crypto.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class App_ROOMPAY extends JDialog {

    private JPanel panelROOMPAY;
    private JButton buttonValider;
    private JButton buttonQuitter;
    private JTextField textFieldSomme;
    private JTextField textFieldCB;
    private JTextField textFieldMDP;
    private JTextField textFieldNomClient;

    public App_ROOMPAY(ObjectInputStream ois, ObjectOutputStream oos, SecretKey secretKey, String id) {

        buttonValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(textFieldMDP.equals("") || textFieldSomme.equals("") || textFieldCB.equals("")) {
                    JOptionPane.showMessageDialog(null, "Remplir les 3 textfields", "Alert", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    try {
                        Cipher cipherSymetrique = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        cipherSymetrique.init(Cipher.ENCRYPT_MODE, secretKey);
                        byte[] plaintext = "ROOMPAY".getBytes();
                        byte[] ciphertext = cipherSymetrique.doFinal(plaintext);
                        oos.writeObject(ciphertext);
                    } catch (InvalidKeyException ex) {
                        ex.printStackTrace();
                    } catch (IllegalBlockSizeException ex) {
                        ex.printStackTrace();
                    } catch (BadPaddingException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (NoSuchPaddingException ex) {
                        ex.printStackTrace();
                    } catch (NoSuchAlgorithmException ex) {
                        ex.printStackTrace();
                    }


                    try {
                        oos.writeObject(id);
                        Carte carte = new Carte();
                        carte.set_mdp(textFieldMDP.getText());
                        carte.set_numeroCarte(textFieldCB.getText());
                        carte.setPaiement(Integer.parseInt(textFieldSomme.getText()));
                        carte.set_nomClient(textFieldNomClient.getText());
                        oos.writeObject(carte);

                        String confirmationPaiement = (String) ois.readObject();
                        System.out.println("Paiement :" + confirmationPaiement);

                        if(confirmationPaiement.equals("OK")) {
                            JOptionPane.showMessageDialog(null, "Paiment effectué", "Alert", JOptionPane.WARNING_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Erreur paiement", "Alert", JOptionPane.WARNING_MESSAGE);
                        }

                        App_ROOMPAY.super.dispose();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_ROOMPAY.super.dispose();
            }
        });


        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelROOMPAY);
        this.pack();
    }
}
