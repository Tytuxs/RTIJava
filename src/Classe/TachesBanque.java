package Classe;

import java.net.Socket;
import java.util.LinkedList;

public class TachesBanque implements SourceTaches {
    private final LinkedList listeTaches;

    public TachesBanque() {
        listeTaches = new LinkedList();
    }

    @Override
    public synchronized Socket getTache() throws InterruptedException {
        System.out.println("BANQUE : getTache avant wait");
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
        System.out.println("BANQUE : ListeTaches : tache dans la file");
        notify();
    }
}
