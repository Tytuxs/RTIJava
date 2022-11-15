package database.facility;

import java.sql.*;
import java.util.ArrayList;

public class BeanReservation extends BD_Bean {

    private final ArrayList<String> MyConditions;
    private final ArrayList<String> MyConditionsNameField;

    public BeanReservation(String string, String user, String pwd) throws SQLException
    {
        super(string,user,pwd);
        MyConditions = new ArrayList<String>();
        MyConditionsNameField = new ArrayList<String>();
    }

    public ResultSet Login() throws SQLException{
        String query = "Select * from user";

        PreparedStatement pStmt = this.getConnection().prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        return pStmt.executeQuery();
    }

    public ResultSet RequestLROOMS(boolean date) throws SQLException
    {
        String colonnes = "*";
        String query = "Select <columns> from <tables>";

        //A MODIFIER

        if(!getColumns().equals(""))
        {
            colonnes = getColumns();
        }

        query = query + ";";

        String SQL = query.replaceAll("<columns>", colonnes).replaceAll("<tables>", getTable()).replaceAll("<Cond>",getCondition());
        PreparedStatement pStmt = this.getConnection().prepareStatement(SQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Requête : "+SQL);

        return pStmt.executeQuery();
    }
}
