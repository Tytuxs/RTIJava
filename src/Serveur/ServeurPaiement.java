package Serveur;

import Classe.TachesPaiement;
import database.facility.BD_Bean;
import database.facility.BeanPaiement;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class ServeurPaiement extends Thread {
    private int PORT_PAY;
    private final TachesPaiement tachesAFaire;
    private final BD_Bean BP;

    public ServeurPaiement(int PORT) throws SQLException {
        setPort(PORT);
        System.out.println("Avant initialisation de TachesPaiement");
        tachesAFaire = new TachesPaiement();
        System.out.println("Apres initialisation de TachesPaiement");
        BP = new BeanPaiement("jdbc:mysql://localhost:3306/bd_holidays","root","pwdmysql");
    }

    public void setPort(int PORT) { this.PORT_PAY = PORT; }
    public int getPort()
    {
        return this.PORT_PAY;
    }

    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(getPort());
            for(int i=0; i<3;i++) {
                ClientHandlerPaiement ThrClient = new ClientHandlerPaiement(tachesAFaire, BP);
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
