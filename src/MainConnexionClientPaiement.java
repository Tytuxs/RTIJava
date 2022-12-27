import Windows.Paiement.App_ConnexionClientPaiement;

import javax.swing.*;
import java.io.IOException;

public class MainConnexionClientPaiement {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()-> {
            try {
                new App_ConnexionClientPaiement().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
