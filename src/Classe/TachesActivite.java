package Classe;

import java.net.Socket;
import java.util.LinkedList;

public class TachesActivite implements SourceTaches{
    private LinkedList listeTaches;

    public TachesActivite()
    {
        listeTaches = new LinkedList();
    }

    public synchronized Socket getTache() throws InterruptedException
    {
        System.out.println("getTache avant wait");
        while (!existTaches())
            wait();
        return (Socket) listeTaches.remove();
    }

    public synchronized boolean existTaches()
    {
        return !listeTaches.isEmpty();
    }

    public synchronized void recordTache(Socket s)
    {
        listeTaches.addLast(s);
        System.out.println("ListeTaches : tache dans la file");
        notify();
    }
}
