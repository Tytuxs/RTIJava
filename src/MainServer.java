import Windows.App_ConnexionServer;

import javax.swing.*;

public class MainServer {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()->new App_ConnexionServer().setVisible(true));
    }
}