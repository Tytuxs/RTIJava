package Classe;

import java.net.Socket;

public interface SourceTaches {
    //création des méthodes de l'interface qui seront utilisées par les classes
    public Socket getTache() throws InterruptedException;
    public boolean existTaches();
    public void recordTache(Socket s);
}
