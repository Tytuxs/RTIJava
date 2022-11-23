package database.facility;

import java.sql.*;


//ici pour les synchronized ???
public class BD_Bean {
    private Connection MyConnexion;
    private Statement MyStatement;
    private String MyTable;
    private String MyCondition;
    private String MyColumns;
    private String MyValues;
    private int MaxColumn;

    //CONSTRUCTOR
    public BD_Bean(String string, String user, String pwd) throws SQLException
    {
        System.out.println("getconnextion = " + DriverManager.getConnection(string, user, pwd));
        this.MyConnexion = DriverManager.getConnection(string, user, pwd);
        System.out.println("myconnexion = " + MyConnexion);
        this.MyStatement = MyConnexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

        setTable("");
        setCondition("");
        setColumns("");
    }


    //SET
    public void setMaxColumn(int max)
    {
        this.MaxColumn = max;
    }

    public void setTable(String table)
    {
        this.MyTable = table;
    }

    public void setCondition(String Cond)
    {
        this.MyCondition = Cond;
    }

    public void setColumns(String col)
    {
        this.MyColumns = col;
    }

    public void setValues(String values)
    {
        this.MyValues = values;
    }

    //GET
    public Connection getConnection()
    {
        return this.MyConnexion;
    }

    public String getTable()
    {
        return this.MyTable;
    }

    public String getCondition()
    {
        return this.MyCondition;
    }

    public String getColumns()
    {
        return this.MyColumns;
    }

    public String getValues()
    {
        return this.MyValues;
    }

    public Statement getMyStatement() {
        return this.MyStatement;
    }

    public int getMaxColumn()
    {
        return this.MaxColumn;
    }

    //FUNCTIONS
    public ResultSet Select(boolean count) throws SQLException
    {
        String colonnes = "*";
        String query = "Select <columns> from <tables>";

        if(!getColumns().equals(""))
        {
            colonnes = getColumns();
        }
        if(count)
        {
            colonnes = "Count(*) as total";
        }
        if(!getCondition().equals("")) {
            query = query + " where <cond>";
        }

        query = query + ";";

        String SQL = query.replaceAll("<columns>", colonnes).replaceAll("<tables>", getTable()).replaceAll("<cond>",getCondition());
        PreparedStatement pStmt = this.getConnection().prepareStatement(SQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Requête : "+SQL);

        return pStmt.executeQuery();
    }

    public int Update() throws SQLException
    {
        String query = "Update <tables> set <values>";

        if(!getCondition().equals("")) {
            query = query + " where <cond>";
        }

        String SQL = query.replaceAll("<values>", getValues()).replaceAll("<tables>", getTable()).replaceAll("<cond>",getCondition());
        PreparedStatement pStmt = this.getConnection().prepareStatement(SQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Requête : "+SQL);

        return pStmt.executeUpdate();
    }

    public ResultSet Login() throws SQLException {
        String query = "Select * from user";

        PreparedStatement pStmt = this.getConnection().prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        return pStmt.executeQuery();
    }

    public int delete() throws SQLException {
        String query = "delete from <tables> where <cond>";

        String SQL = query.replaceAll("<tables>", getTable()).replaceAll("<cond>",getCondition());
        PreparedStatement pStmt = this.getConnection().prepareStatement(SQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("Requête : "+SQL);

        return pStmt.executeUpdate();
    }

    public ResultSet RequestLROOMS(String date) throws SQLException {
        return null;
    }

    public ResultSet RequestBROOM(String categorie, String typeChambre, String dateDeb, String dateFin) throws SQLException {
        return null;
    }

    public int Insert() throws SQLException
    {
        String query = "Insert into <tables> ";

        if(!this.getColumns().equals("")) {
            query = query + "(<columns>) ";
        }

        query = query + "values (<valeurs>)";

        String SQL = query.replaceAll("<tables>", getTable()).replaceAll("<columns>", getColumns()).replaceAll("<valeurs>", getValues());
        PreparedStatement pStmt = this.getConnection().prepareCall(SQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        return pStmt.executeUpdate();
    }

    public void closeStatement() throws SQLException {
        this.MyStatement.close();
    }
}
