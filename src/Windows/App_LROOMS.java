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

public class App_LROOMS extends JDialog {

    private JPanel LROOMSPanel;
    private JTable table1;
    private JButton buttonQuitter;

    DefaultTableModel JTable_Affichage = new DefaultTableModel();

    public App_LROOMS(Socket s, ObjectOutputStream dos, ObjectInputStream dis) throws IOException, ClassNotFoundException {

        dos.writeObject("LROOMS");
        JTable_Affichage.setRowCount(0);
        JTable_Affichage.setColumnCount(6);
        Vector V = new Vector<>();
        V.add("id Réservation");
        V.add("Numero Chambre");
        V.add("Nom du Client");
        V.add("Date d'arrivée");
        V.add("paye ?");
        V.add("prix restant");
        JTable_Affichage.addRow(V);

        ReserActCha reservation;
        //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
        while (true) {
            reservation = (ReserActCha) dis.readObject();
            if(reservation==null)
                break;
            Vector v = new Vector();
            v.add(reservation.get_id());
            v.add(reservation.get_numChambre());
            v.add(reservation.get_persRef());
            v.add(reservation.get_date());
            System.out.println("prixCha = " + reservation.get_prixCha());
            System.out.println("dejaPaye = " + reservation.get_dejaPaye());
            float restant = reservation.get_prixCha() - reservation.get_dejaPaye();
            System.out.println("restant = " + restant);
            if(restant <= 0) {
                restant=0;
            }
            if(restant==0) {
                v.add("Oui");
            }
            else {
                v.add("Non");
            }

            v.add(restant);
            JTable_Affichage.addRow(v);
        }

        table1.setModel(JTable_Affichage);

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_LROOMS.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(1500, 600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(LROOMSPanel);
        this.pack();
    }
}
