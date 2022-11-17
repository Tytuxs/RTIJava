package Windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class App_LROOMS extends JDialog {

    private JPanel LROOMSPanel;
    private JTable table1;

    DefaultTableModel JTable_Affichage = new DefaultTableModel();

    public App_LROOMS(Socket s, DataOutputStream dos, DataInputStream dis) throws IOException {

        dos.writeUTF("LROOMS");
        JTable_Affichage.setRowCount(0);
        JTable_Affichage.setColumnCount(2);
        Vector V = new Vector<>();
        V.add("Numero Chambre");
        V.add("Nom du Client");
        JTable_Affichage.addRow(V);

        String message = dis.readUTF();
        while (true) {
            message = dis.readUTF();
            if(message.equals("FIN"))
                break;
            System.out.println(message);
            StringTokenizer st = new StringTokenizer(message, ";");
            String numChambre = st.nextToken();
            String nomClient = st.nextToken();
            Vector v = new Vector();
            v.add(numChambre);
            v.add(nomClient);
            JTable_Affichage.addRow(v);
        }

        table1.setModel(JTable_Affichage);
        
        this.setMinimumSize(new Dimension(1500, 600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(LROOMSPanel);
        this.pack();
    }
}
