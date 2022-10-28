package Windows;

import database.facility.BD_Bean;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;
import java.util.regex.Pattern;

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

    DefaultComboBoxModel comboBoxModel_ActionSurBD = new DefaultComboBoxModel();
    DefaultComboBoxModel comboboxModel_Table = new DefaultComboBoxModel();
    DefaultTableModel JTable_AffichageBD_Model = new DefaultTableModel();

    public App_DBAccess(BD_Bean bean){
        super();
        InitializeComboBox();

        buttonExectuerRqt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(Objects.equals(combobox_Table.getSelectedItem().toString(), "Voyageur"))
                        bean.setMaxColumn(5);
                    if(Objects.equals(combobox_Table.getSelectedItem().toString(), "Chambre"))
                        bean.setMaxColumn(3);
                    if(Objects.equals(combobox_Table.getSelectedItem().toString(), "Activite"))
                        bean.setMaxColumn(6);
                    if(Objects.equals(combobox_Table.getSelectedItem().toString(), "Reservation"))
                        bean.setMaxColumn(5);

                    bean.setColumns(textField_Colonne.getText());
                    bean.setCondition(textField_Where.getText());
                    bean.setTable(combobox_Table.getSelectedItem().toString());
                    bean.setValues(textField_Set.getText());

                    ResultSet rs = null;
                    int update;

                    if(comboBox_Action.getSelectedItem().toString().equals("SELECT * FROM"))
                        rs = bean.Select(false);
                    if(comboBox_Action.getSelectedItem().toString().equals("SELECT COUNT(*) FROM")) {
                        rs = bean.Select(true);
                        bean.setMaxColumn(1);
                    }
                    if(comboBox_Action.getSelectedItem().toString().equals("UPDATE...SET...WHERE")) {
                        update = bean.Update();
                        JOptionPane.showMessageDialog(null,"Mise à jour de la BD réussie","Alert",JOptionPane.WARNING_MESSAGE);
                    }
                    JTable_AffichageBD_Model.setRowCount(0);

                    if(bean.getColumns().equals("")) {
                        JTable_AffichageBD_Model.setColumnCount(bean.getMaxColumn());
                    }
                    else {
                        int compteur = 0;
                        String nomsColonne = bean.getColumns();
                        String[] output = nomsColonne.split(Pattern.quote(","));

                        for (String s : output) {
                            compteur ++;
                            System.out.println(s);
                        }
                        bean.setMaxColumn(compteur);
                        JTable_AffichageBD_Model.setColumnCount(bean.getMaxColumn());
                    }

                    System.out.println("max colonne = " + bean.getMaxColumn());
                    while(rs.next()) {
                        Vector v = new Vector();

                        for(int i=1; i<=bean.getMaxColumn(); i++) {
                            v.add(rs.getString(i));
                        }
                        JTable_AffichageBD_Model.addRow(v);
                    }
                    JTable_AffichageBD.setModel(JTable_AffichageBD_Model);
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

}