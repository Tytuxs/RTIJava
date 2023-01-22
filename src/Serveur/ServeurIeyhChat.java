package Serveur;

import Classe.TachesChat;
import Classe.TachesPaiement;
import database.facility.BD_Bean;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ServeurIeyhChat extends Thread {
    private int PORT_GROUPE;
    private final TachesChat tachesAFaire;
    private final BD_Bean BC;

    public ServeurIeyhChat(int PORT) throws SQLException {
        this.PORT_GROUPE = PORT;
        tachesAFaire = new TachesChat();
        BC = new BD_Bean("jdbc:mysql://localhost:3306/bd_holidays","root","pwdmysql");
    }


    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(PORT_GROUPE);
            for(int i=0; i<3;i++) {
                ClientHandlerChat ThrClient = new ClientHandlerChat(tachesAFaire, BC);
                ThrClient.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        while (true)
        {
            Socket s = null;
            try
            {
                assert ss != null;
                s = ss.accept();
                tachesAFaire.recordTache(s);
                System.out.println("Nouveau client connecte : " + s);
            }
            catch (Exception e){
                try {
                    assert s != null;
                    s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}


