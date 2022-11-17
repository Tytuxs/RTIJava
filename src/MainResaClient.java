import Windows.App_ConnexionServer;

import javax.swing.*;

public class MainResaClient {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()->new App_ConnexionServer().setVisible(true));
    }
}