package Classe;

import java.net.Socket;

public interface SourceTaches {
    public Socket getTache() throws InterruptedException;
    public boolean existTaches();
    public void recordTache(Socket s);
}
