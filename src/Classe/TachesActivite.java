package Classe;

import java.net.Socket;
import java.util.LinkedList;

public class TachesActivite implements SourceTaches{
    private LinkedList listeTaches;

    public TachesActivite()
    {
        listeTaches = new LinkedList();
    }

    //attends sur wait() pour recevoir le client
    public synchronized Socket getTache() throws InterruptedException
    {
        System.out.println("ACTIVITE : getTache avant wait");
        while (!existTaches())
            wait();
        return (Socket) listeTaches.remove();
    }

    public synchronized boolean existTaches()
    {
        return !listeTaches.isEmpty();
    }

    //ajoute un client à la listeTaches et puis le notify pour débloquer le wait() dans la fonction getTache
    public synchronized void recordTache(Socket s)
    {
        listeTaches.addLast(s);
        System.out.println("ACTIVITE : ListeTaches : tache dans la file");
        notify();
    }
}
