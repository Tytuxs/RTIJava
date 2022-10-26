package database.facility;

import Classe.Voyageur;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class bean_Voyageur {
    //private final Class MyDriver;
    private final Connection MyConnexion;
    private final Statement MyInstruction;

    public bean_Voyageur(String chaine, String user, String pwd) throws ClassNotFoundException, SQLException, SQLException {
        //MyDriver = Class.forName("com.mysql.cj.jdbc.Driver");
        MyConnexion = DriverManager.getConnection(chaine, user, pwd);
        MyInstruction = MyConnexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    }

    public Statement getMyInstruction() {
        return MyInstruction;
    }

    public DefaultTableModel Select(DefaultTableModel modele) throws SQLException
    {
        PreparedStatement statement = MyConnexion.prepareStatement("select * from voyageur;",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Vector v = new Vector();
            v.add(Integer.parseInt(rs.getString("idVoyageur")));
            v.add(rs.getString("nom"));
            v.add(rs.getString("prenom"));
            v.add(rs.getString("dateNaissance"));
            v.add(rs.getString("email"));
            modele.addRow(v);
        }
        return modele;
    }

    public void closeStatement() throws SQLException {
        MyInstruction.close();
    }
}
