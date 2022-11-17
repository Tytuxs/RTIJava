package Windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class App_BROOM extends JDialog{

    private JPanel panelBROOM;
    private JComboBox comboBoxCategorie;
    private JComboBox comboBoxTypeChambre;
    private JTextField textFieldDateArrivee;
    private JTextField textFieldNbNuits;
    private JTextField textFieldNomClient;
    private JButton buttonLancerRecherche;
    private JTable tableAffichage;

    DefaultTableModel JTable_Affichage = new DefaultTableModel();
    DefaultComboBoxModel comboBoxModel_Categorie = new DefaultComboBoxModel();
    DefaultComboBoxModel comboBoxModel_TypeChambre = new DefaultComboBoxModel();
    public App_BROOM(Socket s, DataOutputStream dos, DataInputStream dis){

        initializeComboBox();
        buttonLancerRecherche.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //REQUETE BROOM
                JTable_Affichage.setRowCount(0);
                JTable_Affichage.setColumnCount(2);
                Vector V = new Vector<>();
                V.add("Numero Chambre");
                V.add("Prix de la chambre");
                JTable_Affichage.addRow(V);

                try {
                    dos.writeUTF("BROOM");
                    //DEMANDE DES INFOS AU CLIENT
                    String categorie = comboBoxCategorie.getSelectedItem().toString();
                    String typeChambre = comboBoxTypeChambre.getSelectedItem().toString();
                    String nbNuits = textFieldNbNuits.getText();
                    String date = textFieldDateArrivee.getText();
                    String nom = textFieldNomClient.getText();
                    //ENVOI DES INFOS AUX CLIENTS SOUS FORME DE STRING
                    dos.writeUTF(categorie);
                    dos.writeUTF(typeChambre);
                    dos.writeUTF(nbNuits);
                    dos.writeUTF(date);
                    dos.writeUTF(nom);

                    //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
                    String message;
                    while (true) {
                        message = dis.readUTF();
                        if (message.equals("FIN"))
                            break;
                        else {
                            StringTokenizer st = new StringTokenizer(message, ";");
                            while (st.hasMoreTokens()) {
                                System.out.println("numero de la chambre : " + st.nextToken());
                                System.out.println("prix : " + st.nextToken());
                            }
                        }
                    }
                    //DEMANDE AU CLIENT LA CHAMBRE QU'IL SOUHAITE ET AUSSI LE PRIX CAR PAS ENREGISTRE TANT QUE PAS D'INTERFACE
                    System.out.println("numero chambre selectionnee : ");
                    String choix = null;
                    if (!choix.equals("Aucune")) {
                        System.out.println("prix : ");
                        String prix = null;
                        dos.writeUTF(choix + ";" + prix + ";");
                        String confirmation = dis.readUTF();

                        if (confirmation.equals("OK")) {
                            System.out.println("Reservation prix en compte");
                        } else {
                            System.out.println("Erreur Reservation");
                        }
                    } else {
                        dos.writeUTF("Aucune;");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelBROOM);
        this.pack();
    }

    public void initializeComboBox(){

        comboBoxModel_Categorie.addElement("Motel");
        comboBoxModel_Categorie.addElement("Village");
        this.comboBoxCategorie.setModel(comboBoxModel_Categorie);

        comboBoxModel_TypeChambre.addElement("Simple");
        comboBoxModel_TypeChambre.addElement("Double");
        comboBoxModel_TypeChambre.addElement("Familiale");
        this.comboBoxTypeChambre.setModel(comboBoxModel_TypeChambre);
    }
}
