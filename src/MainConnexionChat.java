import Windows.Admin.App_ConnexionAdmin;
import Windows.Chat.App_ConnexionChat;

import javax.swing.*;
import java.io.IOException;

public class MainConnexionChat {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(()-> {
            try {
                new App_ConnexionChat().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

