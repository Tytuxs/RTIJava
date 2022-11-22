import Windows.Activite.App_ConnexionClientActivite;

import javax.swing.*;
import java.io.IOException;

public class MainConnexionClientActivite {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()-> {
            try {
                new App_ConnexionClientActivite().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
