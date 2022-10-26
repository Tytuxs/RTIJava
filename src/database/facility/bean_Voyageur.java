package database.facility;

import java.sql.*;

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

    public ResultSet Select() throws SQLException
    {
        PreparedStatement pStmt = MyConnexion.prepareStatement("select * from voyageur;",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
        return pStmt.executeQuery();
    }

    public void closeStatement() throws SQLException {
        MyInstruction.close();
    }
}
