import Windows.Admin.App_ConnexionAdmin;

import javax.swing.*;
import java.io.IOException;

public class MainConnexionAdmin {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()-> {
            try {
                new App_ConnexionAdmin().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
