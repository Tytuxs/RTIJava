package Windows.Admin;

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

public class App_LCLIENTS extends JDialog{
    private JTable table1;
    private JButton buttonQuitter;
    private JPanel panelLCLIENTS;

    DefaultTableModel JTable_Affichage = new DefaultTableModel();

    public App_LCLIENTS(Socket s, ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        oos.writeObject("LCLIENTS");

        JTable_Affichage.setRowCount(0);
        JTable_Affichage.setColumnCount(2);
        Vector V = new Vector<>();
        V.add("numClient");
        V.add("IP");
        JTable_Affichage.addRow(V);

        int compteur = 1;
        while(true) {
            String received = (String) ois.readObject();
            if(received==null)
                break;

            Vector v = new Vector<>();
            v.add("Client" + String.valueOf(compteur));
            v.add(received);
            compteur ++;
            JTable_Affichage.addRow(v);
        }

        table1.setModel(JTable_Affichage);

        buttonQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App_LCLIENTS.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelLCLIENTS);
        this.pack();
    }
}
