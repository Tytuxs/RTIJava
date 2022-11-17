package Serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServeurReservation extends Thread {

    static Vector<ClientHandlerReservation> VCHR = new Vector<>();//stocke les clients pour le moment, permettrait plus tard de limiter le nombre de client si on le souhaite
    private int PORT_CHAMBRE;

    public ServeurReservation(int PORT) {
        setPort(PORT);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true)
        {
            Socket s = null;

            try
            {
                assert ss != null;
                s = ss.accept();

                System.out.println("Nouveau client connecte : " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assignement d'un thread pour ce client");

                ClientHandlerReservation ch = new ClientHandlerReservation(s, dis, dos);
                Thread t = new Thread(ch);

                VCHR.add(ch);
                t.start();

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
