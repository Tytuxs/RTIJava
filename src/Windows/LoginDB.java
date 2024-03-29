package Windows;

import database.facility.BD_Bean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginDB extends JDialog{

    private JPanel mainPanel;
    private JPanel LoginDB;
    private JComboBox comboBox_TypeBD;
    private JTextField JTextField_NomHote;
    private JTextField JTextField_Utilisateur;
    private JTextField JTextField_NumeroPort;
    private JTextField JTextField_Password;
    private JTextField JTextField_NomBD;
    private JButton connexionButton;
    private JButton annulerButton;

    DefaultComboBoxModel comboBoxModel_TypeBD = new DefaultComboBoxModel();

    String typeBD = null;
    String nomHote = null;
    String numeroPort = null;
    String nomBD = null;
    String utilisateur = null;
    String password = null;
    String chaineConnexion= null;

    public LoginDB(){
        //super();
        InitializeComboBox();

        connexionButton.addActionListener(new ActionListener() {
            BD_Bean bean;
            @Override
            public void actionPerformed(ActionEvent e) {
                typeBD = comboBox_TypeBD.getSelectedItem().toString();
                if(typeBD.equals("mysql")) {
                    System.out.println("BD Mysql");
                    nomHote = JTextField_NomHote.getText();
                    numeroPort = JTextField_NumeroPort.getText();
                    nomBD = JTextField_NomBD.getText();
                    utilisateur = JTextField_Utilisateur.getText();
                    password = JTextField_Password.getText();
                    chaineConnexion = "jdbc:" + typeBD + ":" + nomHote + ":" + numeroPort + "/" + nomBD;
                } else if(typeBD.equals("oracle")){
                    //ATTENTION : oracle sans majuscule dans combobox
                    //jdbc:oracle:thin:@//localhost:1521/orcl
                    //chaineConnexion = "jdbc:" + typeBD + ":" + "thin:@" + nomHote + ":" + numeroPort + "/orcl?username=" + utilisateur + "&password=" + password;
                    System.out.println("BD Oracle");
                    nomHote = JTextField_NomHote.getText();
                    numeroPort = JTextField_NumeroPort.getText();

                    utilisateur = JTextField_Utilisateur.getText();
                    password = JTextField_Password.getText();
                    chaineConnexion = "jdbc:" + typeBD + ":" + "thin:@" + nomHote + ":" + numeroPort + "/orcl";
                }
                System.out.println("Chaine de connexion = " + chaineConnexion);
                System.out.println("utilisateur = " + JTextField_Utilisateur.getText());
                System.out.println("password = " + JTextField_Password.getText());

                try {
                    bean = new BD_Bean(chaineConnexion,utilisateur,password);

                    System.out.println("Statmeent try =" + bean.getMyStatement());

                    if (bean.getMyStatement() != null)
                    {
                        System.out.println("Statmeent reussi=" + bean.getMyStatement());
                        //JOptionPane.showMessageDialog(null,"Connexion à la BD réussie","Alert",JOptionPane.WARNING_MESSAGE);
                        App_DBAccess app_dbAccess = new App_DBAccess(bean);
                        app_dbAccess.setVisible(true);
                        LoginDB.super.dispose();
                    }
                    else {
                        System.out.println("Statmeent echec=" + bean.getMyStatement());

                        JOptionPane.showMessageDialog(null,"Échec de la connexion à la BD","Alert",JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });



        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDB.super.dispose();
            }
        });


        this.setMinimumSize(new Dimension(600,600));
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setContentPane(LoginDB);
        this.pack();
    }

    public void InitializeComboBox()
    {
        comboBoxModel_TypeBD.addElement("mysql");
        comboBoxModel_TypeBD.addElement("oracle");
        comboBoxModel_TypeBD.addElement("SqlLite");
        this.comboBox_TypeBD.setModel(comboBoxModel_TypeBD);

    }



}