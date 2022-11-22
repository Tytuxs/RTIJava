package database.facility;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanActivite extends BD_Bean {
    public BeanActivite(String string, String user, String pwd) throws SQLException {
        super(string, user, pwd);
    }

    @Override
    public ResultSet Login() throws SQLException {
        return super.Login();
    }

    @Override
    public int Insert() throws SQLException {
        return super.Insert();
    }
}
