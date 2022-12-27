package Classe;

import java.net.Socket;
import java.util.LinkedList;

public class TachesCarte implements SourceTaches {
    private final LinkedList listeTaches;

    public TachesCarte() {
        listeTaches = new LinkedList();
    }

    @Override
    public synchronized Socket getTache() throws InterruptedException {
        System.out.println("PAIEMENT : getTache avant wait");
        while (!existTaches())
            wait();
        return (Socket) listeTaches.remove();
    }

    @Override
    public synchronized boolean existTaches() {
        return !listeTaches.isEmpty();
    }

    @Override
    public synchronized void recordTache(Socket s) {
        listeTaches.add(s);
        System.out.println("PAIEMENT : ListeTaches : tache dans la file");
        notify();
    }
}