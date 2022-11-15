package Serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServeurActivite extends Thread {

    private int PORT_RESERVATION;
    static Vector<ClientHandlerActivite> VCHR = new Vector<ClientHandlerActivite>();

    public ServeurActivite(int PORT) {
        setPort(PORT);
    }

    public void setPort(int PORT) { this.PORT_RESERVATION = PORT; }
    public int getPort() { return this.PORT_RESERVATION; }

    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            Socket s;

            try {
                assert ss != null;
                s = ss.accept();

                System.out.println("ACTIVITE : A new client is connected : " + s);

                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("ACTIVITE : Assigning new thread for this client");

                ClientHandlerActivite ch = new ClientHandlerActivite(s, dis, dos);
                Thread t = new Thread(ch);

                VCHR.add(ch);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
