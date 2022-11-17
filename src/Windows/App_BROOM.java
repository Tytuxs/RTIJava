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
    private JTable table1;

    DefaultTableModel JTable_Affichage = new DefaultTableModel();

    public App_BROOM(Socket s, DataOutputStream dos, DataInputStream dis){

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
                    System.out.println("Motel ou Village");
                    String MouV = comboBoxCategorie.getSelectedItem().toString();
                    System.out.println("Simple, Double ou Familiale(4pers) ?");
                    String typeChambre = comboBoxTypeChambre.getSelectedItem().toString();
                    System.out.println("nombre de nuits : ");
                    String nbNuits = textFieldNbNuits.getText();
                    System.out.println("date d'arrivee(yyyy-MM-dd) : ");
                    String date = textFieldDateArrivee.getText();
                    System.out.println("Votre nom");
                    String nom = textFieldNomClient.getText();
                    //ENVOI DES INFOS AUX CLIENTS SOUS FORME DE STRING
                    dos.writeUTF(MouV);
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
}
