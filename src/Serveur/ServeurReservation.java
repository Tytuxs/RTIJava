package Serveur;

import Classe.TachesReservation;
import database.facility.BD_Bean;
import database.facility.BeanReservation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ServeurReservation extends Thread {

    private int PORT_CHAMBRE;
    private final TachesReservation tachesAFaire;
    private final BD_Bean BR;

    public ServeurReservation(int PORT, String typeBD, String IPDB, String portDB, String nameDB) throws SQLException {
        setPort(PORT);
        tachesAFaire = new TachesReservation();
        String urldb = "jdbc:" + typeBD + "://" + IPDB + ":" + portDB + "/" + nameDB;
        System.out.println(urldb);
        BR = new BeanReservation(urldb,"root","pwdmysql");
    }

    public void setPort(int PORT) { this.PORT_CHAMBRE = PORT; }
    public int getPort()
    {
        return this.PORT_CHAMBRE;
    }

    @Override
    public void run() {
        // server is listening on port 5056
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(getPort());
            for(int i=0; i<3;i++) {
                ClientHandlerReservation ThrClient = new ClientHandlerReservation(tachesAFaire, BR);
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
