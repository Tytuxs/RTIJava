package Windows;

import Classe.ReserActCha;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class App_PROOM extends JDialog {

    private JPanel panelPROOM;
    private JButton buttonRechercher;
    private JTable table1;
    private JTextField textFieldNomClient;
    private JButton buttonPayer;
    private JButton buttonQuitter;

    DefaultTableModel JTable_Affichage = new DefaultTableModel();

    /*public App_PROOM(Socket s, DataOutputStream dos, DataInputStream dis) {

        buttonRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String message;
                    dos.writeUTF("PROOM");
                    JTable_Affichage.setRowCount(0);
                    JTable_Affichage.setColumnCount(5);
                    String nomClient = textFieldNomClient.getText();
                    dos.writeUTF(nomClient);

                    //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
                    while (true) {
                        message = dis.readUTF();
                        if(message.equals("FIN"))
                            break;
                        else {
                            StringTokenizer st = new StringTokenizer(message,";");
                            while (st.hasMoreTokens()) {
                                Vector v = new Vector();
                                v.add(st.nextToken());
                                v.add(st.nextToken());
                                v.add(st.nextToken());
                                v.add(st.nextToken());
                                v.add(st.nextToken());
                                JTable_Affichage.addRow(v);
                            }
                        }
                    }

                    dos.writeUTF("OK");
                    System.out.println("id de la reservation : ");
                    String id = (String) table1.getValueAt(table1.getSelectedRow(),0);
                        dos.writeUTF(id);
                        String confirmation = dis.readUTF();
                        if (confirmation.equals("OK")) {
                            JOptionPane.showMessageDialog(null, "Paiement fait", "Alert", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Paiement déjà fait ou Erreur lors du paiement", "Alert", JOptionPane.WARNING_MESSAGE);
                        }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });*/

        /*buttonPayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeUTF("OK");
                    System.out.println("id de la reservation : ");
                    String id = (String) table1.getValueAt(table1.getSelectedRow(), 0);
                    dos.writeUTF(id);
                    String confirmation = dis.readUTF();
                    if (confirmation.equals("OK")) {
                        JOptionPane.showMessageDialog(null, "Paiement fait", "Alert", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Paiement déjà fait ou Erreur lors du paiement", "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                App_PROOM.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600, 600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelPROOM);
        this.pack();
    }*/

    public App_PROOM(Socket s, ObjectOutputStream dos, ObjectInputStream dis) {

        buttonRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ReserActCha reservation;
                    dos.writeObject("PROOM");
                    JTable_Affichage.setRowCount(0);
                    JTable_Affichage.setColumnCount(5);
                    Vector V = new Vector<>();
                    V.add("id");
                    V.add("Numero Chambre");
                    V.add("Prix de la chambre");
                    V.add("Personne référée");
                    V.add("Payé ?");
                    JTable_Affichage.addRow(V);
                    String nomClient = textFieldNomClient.getText();
                    dos.writeObject(nomClient);

                    //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
                    while (true) {
                        reservation = (ReserActCha) dis.readObject();
                        if(reservation==null)
                            break;
                        else {
                            Vector v = new Vector();
                            v.add(reservation.get_id());
                            v.add(reservation.get_numChambre());
                            v.add(reservation.get_prixCha());
                            v.add(reservation.get_persRef());
                            boolean paye = reservation.is_paye();
                            if(!paye) {
                                v.add("Non");
                            }
                            else {
                                v.add("Oui");
                            }
                            JTable_Affichage.addRow(v);
                        }
                    }

                    table1.setModel(JTable_Affichage);
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonPayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dos.writeObject("OK");
                    String id = table1.getValueAt(table1.getSelectedRow(), 0).toString();
                    dos.writeObject(id);
                    String confirmation = (String) dis.readObject();
                    if (confirmation.equals("OK")) {
                        JOptionPane.showMessageDialog(null, "Paiement fait", "Alert", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Paiement déjà fait ou Erreur lors du paiement", "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                App_PROOM.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600, 600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelPROOM);
        this.pack();
    }
}
