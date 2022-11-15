package Serveur;

import java.io.IOException;

public class MainServeur {
    public static synchronized void main(String[] args) throws IOException
    {
        ServeurReservation SC = new ServeurReservation(5056);
        ServeurActivite SR = new ServeurActivite(6000);

        Thread t = new Thread(SC);
        System.out.println("Lancement du ServeurChambre avec un thread");
        t.start();

        Thread t2 = new Thread(SR);
        System.out.println("Lancement du ServeurReservation avec un thread");
        t2.start();
    }
}
