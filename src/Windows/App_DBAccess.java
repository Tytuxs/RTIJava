package Windows;

import database.facility.bean_Voyageur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;

public class App_DBAccess extends JDialog {
    private JPanel mainPanel;
    private JComboBox comboBox_Action;
    private JButton buttonDeconnexion;
    private JButton buttonExectuerRqt;
    private JComboBox combobox_Table;
    private JTextField textField_Colonne;
    private JTextField textField_Set;
    private JTextField textField_Where;
    private JLabel JLabel_Colonne;
    private JTable JTable_AffichageBD;

    //private JScrollPane JScrollPane_DonneesTable;

    DefaultComboBoxModel comboBoxModel_ActionSurBD = new DefaultComboBoxModel();
    DefaultComboBoxModel comboboxModel_Table = new DefaultComboBoxModel();

    DefaultTableModel JTable_AffichageBD_Model = new DefaultTableModel();

    Vector vector_AffichageBD = new Vector();

    public App_DBAccess(bean_Voyageur bean){
        super();
        InitializeComboBox();

        buttonExectuerRqt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(Objects.equals(combobox_Table.getSelectedItem().toString(), "Voyageur")) {
                        ResultSet rs = bean.Select();
                        JTable_AffichageBD_Model.setRowCount(0);
                        JTable_AffichageBD_Model.setColumnCount(5);
                        Vector v = new Vector();
                        v.add("idVoyageur");
                        v.add("nom");
                        v.add("prenom");
                        v.add("DateNaissance");
                        v.add("email");
                        JTable_AffichageBD_Model.addRow(v);
                        while (rs.next()) {
                            Vector vectorBD = new Vector();

                            vectorBD.add(rs.getObject("idVoyageur"));
                            vectorBD.add(rs.getObject("nom"));
                            vectorBD.add(rs.getObject("prenom"));
                            vectorBD.add(rs.getObject("dateNaissance"));
                            vectorBD.add(rs.getObject("email"));

                            JTable_AffichageBD_Model.addRow(vectorBD);

                            System.out.print("idVoyageur: " + rs.getObject("idVoyageur"));
                            System.out.print(", nom: " + rs.getString("nom"));
                            System.out.print(", prenom: " + rs.getString("prenom"));
                            System.out.print(", date Naissance: " + rs.getDate("dateNaissance"));
                            System.out.println(", email: " + rs.getString("email"));
                        }

                        JTable_AffichageBD.setModel(JTable_AffichageBD_Model);
                    }
                }
                catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });

        buttonDeconnexion.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    bean.closeStatement();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                App_DBAccess.super.dispose();
            }
        });

        this.setMinimumSize(new Dimension(1500,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
    }



    public void InitializeComboBox() {
        comboBoxModel_ActionSurBD.addElement("SELECT * FROM");
        comboBoxModel_ActionSurBD.addElement("SELECT COUNT(*) FROM");
        comboBoxModel_ActionSurBD.addElement("UPDATE...SET...WHERE");

        this.comboBox_Action.setModel(comboBoxModel_ActionSurBD);

        comboboxModel_Table.addElement("Voyageur");
        comboboxModel_Table.addElement("Chambre");
        comboboxModel_Table.addElement("Activite");
        comboboxModel_Table.addElement("Reservation");
        this.combobox_Table.setModel(comboboxModel_Table);
    }

    public void update()
    {
        JTable_AffichageBD.setModel(JTable_AffichageBD_Model);
        JTable_AffichageBD_Model.addColumn("id Voyageur");
        JTable_AffichageBD_Model.addColumn("Nom");
        JTable_AffichageBD_Model.addColumn("Prenom");
        JTable_AffichageBD_Model.addColumn("Date Naissance");
        JTable_AffichageBD_Model.addColumn("Email");
        JTable_AffichageBD.setModel(JTable_AffichageBD_Model);

    }
}