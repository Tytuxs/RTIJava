import Windows.App_ConnexionServer;

import javax.swing.*;
import java.sql.SQLException;

public class MainServer {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()-> {
            try {
                new App_ConnexionServer().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}