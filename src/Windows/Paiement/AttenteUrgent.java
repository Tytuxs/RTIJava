package Windows.Paiement;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AttenteUrgent extends Thread {

    private Socket s;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public AttenteUrgent(Socket s, ObjectOutputStream oos, ObjectInputStream ois)  {
        this.s=s;
        this.ois = ois;
        this.oos = oos;
    }

    @Override
    public void run() {
        while(true) {
            try {
                String urgent = (String) ois.readObject();
                JOptionPane.showMessageDialog(null, urgent, "Alert", JOptionPane.WARNING_MESSAGE);
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
