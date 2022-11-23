package Windows.Activite;

import Classe.Activite;
import Classe.ReserActCha;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class App_LISTACT extends JDialog{
    private JPanel panelLISTACT;
    private JButton rechercherLesActivitesButton;
    private JButton rechercherLesParticipantsButton;
    private JTable tableActivites;
    private JTable tableParticipants;
    private JTextField textFieldActivite;
    private JButton quitterButton;

    DefaultTableModel JTable_Activites = new DefaultTableModel();
    DefaultTableModel JTable_Participants = new DefaultTableModel();

    public App_LISTACT(ObjectOutputStream oos, ObjectInputStream ois) {
        JTable_Activites.setRowCount(0);
        JTable_Activites.setColumnCount(6);
        Vector V = new Vector<>();
        V.add("ID Activite");
        V.add("Activite");
        V.add("date");
        V.add("nombre de jours");
        V.add("nombre maximum de place");
        V.add("nombre places prises");
        JTable_Activites.addRow(V);

        JTable_Participants.setRowCount(0);
        JTable_Participants.setColumnCount(4);
        Vector V2 = new Vector<>();
        V2.add("ID Reservation");
        V2.add("Activite");
        V2.add("nombre Inscrits");
        V2.add("Personne référente");
        JTable_Participants.addRow(V2);

        rechercherLesActivitesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!textFieldActivite.equals("")) {
                    try {
                        //envoit type requete
                        oos.writeObject("LISTACT");
                        //envoit du nom de l'activite
                        oos.writeObject(textFieldActivite.getText());
                        //recuperation des donnes a afficher dans le tableau
                        while (true) {
                            Activite activite = (Activite) ois.readObject();
                            if (activite == null)
                                break;
                            else {
                                Vector v = new Vector();
                                v.add(activite.getId());
                                v.add(activite.getType());
                                v.add(activite.getDate());
                                v.add(activite.getNbJours());
                                v.add(activite.getNbMaxParticipants());
                                v.add(activite.getNbInscrits());
                                JTable_Activites.addRow(v);
                            }
                        }
                        tableActivites.setModel(JTable_Activites);
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        rechercherLesParticipantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tableActivites.getSelectedRow() != -1) {
                    try {
                        //envoit de l'id de l'activite selectionne dans la tableActivites
                        oos.writeObject(tableActivites.getValueAt(tableActivites.getSelectedRow(),0));
                        //affichage de tous les inscrits a cette activite
                        while (true) {
                            ReserActCha reservation = (ReserActCha) ois.readObject();
                            if (reservation == null)
                                break;
                            else {
                                Vector v = new Vector();
                                v.add(reservation.get_id());
                                v.add(reservation.get_typeAct());
                                v.add(reservation.get_nbInscrit());
                                v.add(reservation.get_persRef());
                                JTable_Participants.addRow(v);
                            }
                        }
                        tableParticipants.setModel(JTable_Participants);
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        quitterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("Exit");
                    App_LISTACT.super.dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelLISTACT);
        this.pack();
    }
}
