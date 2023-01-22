package Windows.Chat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Vector;

public class ThreadReception extends Thread {
    private String nom;
    private MulticastSocket socketGroupe;
    private JTable tableMessage;
    DefaultTableModel JTable_Message;
    private Vector<String> listeMessage;

    public ThreadReception(String n, MulticastSocket ms, JTable table) {
        nom = n;
        socketGroupe = ms;
        tableMessage = table;
        listeMessage = new Vector<>();
    }

    public void run() {
        boolean enMarche = true;
        while (enMarche) {
            try {
                System.out.println("boucle thrReception");

                JTable_Message = new DefaultTableModel();

                byte[] buf = new byte[1000];
                DatagramPacket dtg = new DatagramPacket(buf, buf.length);
                socketGroupe.receive(dtg);
                System.out.println(tableMessage.getRowCount());
                listeMessage.add(new String (buf));
                JTable_Message.setRowCount(listeMessage.size());
                JTable_Message.setColumnCount(1);

                for(int i=0; i<listeMessage.size();i++) {
                    JTable_Message.removeRow(0);
                    System.out.println(listeMessage.size());
                    Vector v = new Vector<>();
                    v.add(listeMessage.get(i));
                    JTable_Message.addRow(v);
                }

                tableMessage.setModel(JTable_Message);

            } catch (IOException e) {
                System.out.println("Erreur dans le thread :-( :" + e.getMessage());
                enMarche = false; // fin
            }
        }
    }
}
