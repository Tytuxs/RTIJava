package Windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public App_DBAccess(String title,Statement statement){
        super();
        InitializeComboBox();

        buttonExectuerRqt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String requete = null;
                String action = comboBox_Action.getSelectedItem().toString();
                String nomTable = combobox_Table.getSelectedItem().toString();
                String nomColonne = textField_Colonne.getText();
                String set = textField_Set.getText();
                String where = textField_Where.getText();
                int nbColonne = 0;

                if(action == "SELECT * FROM") {
                    requete = "SELECT * FROM " + nomTable;

                } else
                if (action == "SELECT COUNT(*) FROM")
                {
                    requete = "SELECT COUNT(*) AS totalCount FROM "+ nomTable +" WHERE "+ where;

                }
                if(action == "UPDATE...SET...WHERE") {
                   requete = "UPDATE " + nomTable + " SET " + set + " WHERE " + where;
                } else

                if(nomTable == "Voyageur") {
                    nomTable = "voyageur";
                    System.out.println("nom Table = " + nomTable);
                }
                else
                if ( nomTable == "Chambre")
                {
                    nomTable = "chambre";
                    System.out.println("nom Table = " + nomTable);
                }
                if(nomTable == "Activite") {
                    nomTable = "activite";
                    System.out.println("nom Table = " + nomTable);
                } else
                if(nomTable == "Reservation") {
                    nomTable = "reservation" ;
                    System.out.println("nom Table = " + nomTable);
                }

                System.out.println("Requête = " + requete);

                try {
                        if (action == "SELECT COUNT(*) FROM"){
                        ResultSet rs = statement.executeQuery(requete);
                        rs.next();
                        long result = rs.getLong("totalCount");
                        System.out.println("totalCount" + result);
                        }
                        else
                        if(action == "UPDATE...SET...WHERE")
                        {
                            statement.executeUpdate(requete);
                            JOptionPane.showMessageDialog(null,"Mise à jour de la BD réussie","Alert",JOptionPane.WARNING_MESSAGE);
                            ResultSet rs = statement.executeQuery(requete);
                        }
                        else if (action == "SELECT * FROM"){
                            ResultSet rs = statement.executeQuery(requete);
                            System.out.println("nomtabel = " + nomTable);
                            if(nomTable == "voyageur") {
                                JTable_AffichageBD_Model.setRowCount(0);
                                JTable_AffichageBD_Model.setColumnCount(5);
                                Vector v = new Vector();
                                v.add("idVoyageur");
                                v.add("nom");
                                v.add("prenom");
                                v.add("DateNaissance");
                                v.add("email");
                                JTable_AffichageBD_Model.addRow(v);
                            }
                            if ( nomTable == "Chambre") {
                                JTable_AffichageBD_Model.setRowCount(0);
                                JTable_AffichageBD_Model.setColumnCount(3);
                                Vector v = new Vector();
                                v.add("nrChambre");
                                v.add("nbOccupants");
                                v.add("prixHTVA");
                                JTable_AffichageBD_Model.addRow(v);
                            }
                            if(nomTable == "activite") {
                                JTable_AffichageBD_Model.setRowCount(0);
                                JTable_AffichageBD_Model.setColumnCount(7);
                                Vector v = new Vector();
                                v.add("id");
                                v.add("type");
                                v.add("nbMaxParticipants");
                                v.add("nbInscrits");
                                v.add("dureeHeure");
                                v.add("prixHTVA");
                                v.add("activitecol");
                                JTable_AffichageBD_Model.addRow(v);
                            }
                            if(nomTable == "reservation") {
                                JTable_AffichageBD_Model.setRowCount(0);
                                JTable_AffichageBD_Model.setColumnCount(5);
                                Vector v = new Vector();
                                v.add("idReservation");
                                v.add("dateDebut");
                                v.add("dateFin");
                                v.add("prixNet");
                                v.add("paye");
                                JTable_AffichageBD_Model.addRow(v);
                            }
                            /*JTable_AffichageBD_Model.setRowCount(0);
                            JTable_AffichageBD_Model.setColumnCount(5);
                            Vector v = new Vector();
                            v.add("idVoyageur");
                            v.add("nom");
                            v.add("prenom");
                            v.add("DateNaissance");
                            v.add("email");
                            JTable_AffichageBD_Model.addRow(v);*/
                            //update();
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