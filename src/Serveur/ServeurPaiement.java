package Serveur;

import Classe.TachesPaiement;
import Classe.TachesUrgente;
import database.facility.BD_Bean;
import database.facility.BeanPaiement;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServeurPaiement extends Thread {
    private int PORT_PAY;
    private final TachesPaiement tachesAFaire;
    private final TachesUrgente urgent;
    private final BD_Bean BP;
    private ArrayList<ClientHandlerUrgence> listThreadClient;
    private ArrayList<Socket> listSocketClient;
    private int PORT_ADMIN = 9000;
    private int PORT_URGENCE = 9001;

    public ServeurPaiement(int PORT) throws SQLException {
        setPort(PORT);
        System.out.println("Avant initialisation de TachesPaiement");
        tachesAFaire = new TachesPaiement();
        urgent = new TachesUrgente();
        System.out.println("Apres initialisation de TachesPaiement");
        BP = new BeanPaiement("jdbc:mysql://localhost:3306/bd_holidays","root","pwdmysql");
        listThreadClient = new ArrayList<>();
        listSocketClient = new ArrayList<>();
    }

    public void setPort(int PORT) { this.PORT_PAY = PORT; }
    public int getPort()
    {
        return this.PORT_PAY;
    }

    @Override
    public void run() {
        ServerSocket ss = null;
        ServerSocket ssUrgent = null;
        ServerSocket ssAdmin;
        try {
            ss = new ServerSocket(getPort());
            for(int i=0; i<3;i++) {
                ClientHandlerPaiement ThrClient = new ClientHandlerPaiement(tachesAFaire, BP);
                ThrClient.start();
            }
            ssUrgent = new ServerSocket(PORT_URGENCE);
            for(int i=0; i<3;i++) {
                ClientHandlerUrgence ThrClient = new ClientHandlerUrgence(urgent);
                ThrClient.start();
                listThreadClient.add(ThrClient);
            }

            ssAdmin = new ServerSocket(PORT_ADMIN);
            ClientHandlerAdmin thrAdmin = new ClientHandlerAdmin(ssAdmin, listThreadClient, BP, listSocketClient);
            thrAdmin.start();
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
                assert ssUrgent != null;
                s = ssUrgent.accept();
                urgent.recordTache(s);
                listSocketClient.add(s);
                System.out.println("Nouveau client connecte sur PORT_URGENCE: " + s);
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
