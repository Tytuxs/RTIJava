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

    /*public App_LROOMS(Socket s, DataOutputStream dos, DataInputStream dis) throws IOException {

        dos.writeUTF("LROOMS");
        JTable_Affichage.setRowCount(0);
        JTable_Affichage.setColumnCount(3);
        Vector V = new Vector<>();
        V.add("Numero Chambre");
        V.add("Nom du Client");
        V.add("Date d'arrivée");
        JTable_Affichage.addRow(V);

        String message = dis.readUTF();
        //LECTURE DES MESSAGES RECUS ET BOUCLE JUSQU'AU MESSAGE "FIN"
        while (true) {
            message = dis.readUTF();
            if(message.equals("FIN"))
                break;
            System.out.println(message);
            StringTokenizer st = new StringTokenizer(message, ";");
            String numChambre = st.nextToken();
            String nomClient = st.nextToken();
            String datedeb = st.nextToken();
            Vector v = new Vector();
            v.add(numChambre);
            v.add(nomClient);
            v.add(datedeb);
            JTable_Affichage.addRow(v);
        }

        table1.setModel(JTable_Affichage);

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // closing resources
                App_LROOMS.super.dispose();
            }
        });
        
        this.setMinimumSize(new Dimension(1500, 600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(LROOMSPanel);
        this.pack();
    }*/

    public App_LROOMS(Socket s, ObjectOutputStream dos, ObjectInputStream dis) throws IOException, ClassNotFoundException {

        dos.writeObject("LROOMS");
        JTable_Affichage.setRowCount(0);
        JTable_Affichage.setColumnCount(5);
        Vector V = new Vector<>();
        V.add("id Réservation");
        V.add("Numero Chambre");
        V.add("Nom du Client");
        V.add("Date d'arrivée");
        V.add("paye ?");
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
            boolean paye = reservation.is_paye();
            if(!paye) {
                v.add("Non");
            }
            else {
                v.add("Oui");
            }
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
