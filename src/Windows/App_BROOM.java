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
    private JButton buttonValiderResa;

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
                    //ENVOI DES INFOS AU SERVEUR SOUS FORME DE STRING
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
                            Vector v = new Vector();
                            v.add(st.nextToken());
                            v.add(st.nextToken());
                            JTable_Affichage.addRow(v);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                tableAffichage.setModel(JTable_Affichage);
            }
        });

        buttonValiderResa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //DEMANDE AU CLIENT LA CHAMBRE QU'IL SOUHAITE ET AUSSI LE PRIX CAR PAS ENREGISTRE TANT QUE PAS D'INTERFACE
                    try {
                        if (tableAffichage.getSelectedRow() != -1) {
                            System.out.println("prix : ");
                            String numChambre = (String) tableAffichage.getValueAt(tableAffichage.getSelectedRow(), 0);
                            String prix = (String) tableAffichage.getValueAt(tableAffichage.getSelectedRow(), 1);
                            dos.writeUTF(numChambre + ";" + prix + ";");
                            String confirmation = dis.readUTF();

                            if (confirmation.equals("OK")) {
                                JOptionPane.showMessageDialog(null, "Réservation acceptée", "Alert", JOptionPane.WARNING_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erreur Réservation", "Alert", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                App_BROOM.super.dispose();
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
