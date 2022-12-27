package Serveur;

import Classe.TachesCarte;
import database.facility.BD_Bean;
import database.facility.BeanPaiement;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ServeurCarte extends Thread {
    private int PORT_CARD;
    private final TachesCarte tachesAFaire;
    private final BD_Bean BC;

    public ServeurCarte(int PORT) throws SQLException {
        setPort(PORT);
        System.out.println("Avant initialisation de TachesCarte");
        tachesAFaire = new TachesCarte();
        System.out.println("Apres initialisation de TachesCarte");
        BC = new BeanPaiement("jdbc:mysql://localhost:3306/bd_holidays","root","pwdmysql");
    }

    public void setPort(int PORT) { this.PORT_CARD = PORT; }
    public int getPort()
    {
        return this.PORT_CARD;
    }

    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(getPort());
            for(int i=0; i<3;i++) {
                ClientHandlerCarte ThrClient = new ClientHandlerCarte(tachesAFaire, BC);
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
