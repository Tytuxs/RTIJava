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

public class App_SHACT extends JDialog {
    private JPanel panelSHACT;
    private JTable table1;
    private JTextField textFieldNbAInscrire;
    private JButton reserverButton;
    private JButton quitterButton;
    private JTextField textFieldPersRef;

    DefaultTableModel JTable_Affichage = new DefaultTableModel();

    public App_SHACT(ObjectOutputStream oos, ObjectInputStream ois, int nbJour) {
        JTable_Affichage.setRowCount(0);
        JTable_Affichage.setColumnCount(8);
        Vector V = new Vector<>();
        V.add("ID Activite");
        V.add("Activite");
        V.add("date");
        V.add("nombre de jours");
        V.add("duree par jour");
        V.add("nombre maximum de place");
        V.add("nombre places prises");
        V.add("Prix de l'activité");
        JTable_Affichage.addRow(V);

        try {
            oos.writeObject("SHACT");
            oos.writeObject(nbJour);
            //RECEPTION DES ACTIVITES DE COURTES OU LONGUES DUREES

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
                    v.add(activite.getDureeHeure());
                    v.add(activite.getNbMaxParticipants());
                    v.add(activite.getNbInscrits());
                    v.add(activite.getPrixHTVA());
                    JTable_Affichage.addRow(v);
                }
            }
            table1.setModel(JTable_Affichage);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        reserverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(textFieldNbAInscrire.equals("")) {
                        JOptionPane.showMessageDialog(null, "Rentrer le nombre a inscrire", "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        oos.writeObject("CONTINUE");
                        ReserActCha reservation = new ReserActCha();
                        reservation.set_persRef(textFieldPersRef.getText());
                        reservation.set_typeAct(table1.getValueAt(table1.getSelectedRow(),1).toString());
                        reservation.set_date(table1.getValueAt(table1.getSelectedRow(),2).toString());
                        reservation.set_nbJour((Integer) table1.getValueAt(table1.getSelectedRow(),3));
                        reservation.set_dureeHeure((Integer) table1.getValueAt(table1.getSelectedRow(),4));
                        reservation.set_nbMaxAct((Integer) table1.getValueAt(table1.getSelectedRow(),5));
                        reservation.set_nbInscrit(Integer.parseInt(textFieldNbAInscrire.getText()));
                        reservation.set_prixAct((float) table1.getValueAt(table1.getSelectedRow(),7));
                        reservation.set_type("Activite");
                        reservation.set_paye(false);
                        reservation.set_idAct((Integer) table1.getValueAt(table1.getSelectedRow(),0));

                        oos.writeObject(reservation);

                        String confirmation = (String) ois.readObject();
                        if (confirmation.equals("OK")) {
                            JOptionPane.showMessageDialog(null, "Réservation acceptée", "Alert", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur Réservation", "Alert", JOptionPane.WARNING_MESSAGE);
                        }
                        App_SHACT.super.dispose();
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        quitterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("Exit");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelSHACT);
        this.pack();
    }
}
