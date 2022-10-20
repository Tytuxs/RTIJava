package database.facility;
import java.sql.Statement;


public class MyJDBC
{
    public Statement statement;
    String _chaineConnexion;
    String _nomUtilisateur;
    String _password;

    public MyJDBC(String chaineConnexion, String utilisateur, String password)
    {
        statement = null;
        _chaineConnexion=chaineConnexion;
        _nomUtilisateur=utilisateur;
        _password=password;
    }
    public Statement ConnexionJDBC()
    {
        //int etatConnexion = 0;
        System.out.println(statement);
        return statement;

    }

    public static void Test_Lib_JDBC()
    {

    }

    public static void FermetureConnexionJDBC()
    {


    }
}