package database.facility;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanActivite extends BD_Bean {
    public BeanActivite(String string, String user, String pwd) throws SQLException {
        super(string, user, pwd);
    }

    //pas obligé de redéfinir les methodes

    @Override
    public ResultSet Login() throws SQLException {
        //on récupère tous les utilisateurs, utilise la methode de BD_Bean
        return super.Login();
    }

    @Override
    public int Insert() throws SQLException {
        /*
         * Permet d'ajouter ce que le client a demander comme chambre, utilise la methode de BD_Bean
         */
        return super.Insert();
    }
}
