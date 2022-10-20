package Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App_DBAccess extends JDialog {
    private JPanel mainPanel;
    private JLabel Label_Action;
    private JComboBox comboBox_Action;
    private JButton buttonDeconnexion;
    private JButton buttonExectuerRqt;
    private JComboBox combobox_Table;
    private JScrollPane JScrollPane_DonneesTable;

    DefaultComboBoxModel comboBoxModel_ActionSurBD = new DefaultComboBoxModel();
    DefaultComboBoxModel comboboxModel_Table = new DefaultComboBoxModel();


    public App_DBAccess(String title,Statement statement){
        super();
        InitializeComboBox();

        buttonExectuerRqt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String action = comboBox_Action.getSelectedItem().toString();
                String nomTable = combobox_Table.getSelectedItem().toString();
                String requete = "SELECT * FROM " + "voyageur";

                try {
                    ResultSet rs = statement.executeQuery(requete);
                    while (rs.next())
                    {
                        System.out.println(rs.getString("idVoyageur")) ;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonDeconnexion.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }



    public void InitializeComboBox() {
        comboBoxModel_ActionSurBD.addElement("Sélectionner tout");
        comboBoxModel_ActionSurBD.addElement("Sélectionner premier");
        comboBoxModel_ActionSurBD.addElement("Supprimer de la BD");
        this.comboBox_Action.setModel(comboBoxModel_ActionSurBD);

        comboboxModel_Table.addElement("Voyageur");
        comboboxModel_Table.addElement("Chambre");
        comboboxModel_Table.addElement("Activités");
        comboboxModel_Table.addElement("Groupe");
        this.combobox_Table.setModel(comboboxModel_Table);
    }
}
