package database.facility;

import java.io.Serializable;
import java.sql.SQLException;

public class BeanPaiement extends BD_Bean implements Serializable {
    public BeanPaiement(String string, String user, String pwd) throws SQLException {
        super(string, user, pwd);
    }
}
