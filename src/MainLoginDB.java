import Windows.LoginDB;

import javax.swing.*;

public class MainLoginDB {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()->new LoginDB().setVisible(true));
    }
}