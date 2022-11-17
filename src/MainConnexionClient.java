import Windows.App_ConnexionClient;

import javax.swing.*;
import java.io.IOException;

public class MainConnexionClient {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()-> {
            try {
                new App_ConnexionClient().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}