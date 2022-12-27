package Windows.Paiement;

import Classe.ReserActCha;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

public class App_LISTPAY extends JDialog {
    private JTable tableClients;
    private JPanel panelLISTPAY;
    private JButton buttonQuitter;
    private JButton buttonValider;

    DefaultTableModel JTable_Affichage = new DefaultTableModel();

    public App_LISTPAY(Socket s, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {

        JTable_Affichage.setRowCount(0);
        JTable_Affichage.setColumnCount(5);
        Vector V = new Vector<>();
        V.add("id");
        V.add("Numero Chambre");
        V.add("Prix de la chambre");
        V.add("Personne référée");
        V.add("Prix restant");
        JTable_Affichage.addRow(V);

        oos.writeObject("LISTPAY");

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

            }
        });

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    oos.writeObject("Exit");
                    ois.close();
                    oos.close();
                    s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                App_LISTPAY.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelLISTPAY);
        this.pack();
    }

}
