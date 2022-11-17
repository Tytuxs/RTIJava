package Windows;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class App_BROOM extends JDialog{

    private JPanel panelBROOM;
    private JComboBox comboBoxCategorie;
    private JComboBox comboBoxTypeChambre;
    private JTextField textFieldDateArrivee;
    private JTextField textFieldNbNuits;
    private JTextField textFieldNomClient;
    private JButton buttonEnvoyerResa;
    private JTable table1;

    public App_BROOM(Socket s, DataOutputStream dos, DataInputStream dis){




        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(panelBROOM);
        this.pack();
    }
}
