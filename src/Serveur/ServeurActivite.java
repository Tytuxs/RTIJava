package Serveur;

import Classe.TachesActivite;
import database.facility.BD_Bean;
import database.facility.BeanActivite;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ServeurActivite extends Thread {

    private int PORT_ACTIVITE;
    private final TachesActivite tachesAFaire;
    private final BD_Bean BA;

    public ServeurActivite(int PORT) throws SQLException {
        setPort(PORT);
        tachesAFaire = new TachesActivite();
        BA = new BeanActivite("jdbc:mysql://localhost:3306/bd_holidays","root","pwdmysql");
    }

    public void setPort(int PORT) { this.PORT_ACTIVITE = PORT; }
    public int getPort() { return this.PORT_ACTIVITE; }

    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(getPort());
            for(int i=0; i<3; i++) {
                ClientHandlerActivite ThrClient = new ClientHandlerActivite(tachesAFaire, BA);
                ThrClient.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            Socket s = null;

            try {
                assert ss != null;
                s = ss.accept();
                tachesAFaire.recordTache(s);
                System.out.println("ACTIVITE : Nouveau client connecte : " + s);

            } catch (Exception e) {
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
