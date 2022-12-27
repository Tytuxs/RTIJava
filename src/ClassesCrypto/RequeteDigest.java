package ClassesCrypto;

import java.io.Serializable;

public class RequeteDigest implements Serializable {
    private byte[] mdp;
    private String utilisateur;
    private long temps;
    private double nbAlea;

    public RequeteDigest(byte[] mdp, String utilisateur, long temps, double nbAlea) {
        this.mdp = mdp;
        this.utilisateur = utilisateur;
        this.temps = temps;
        this.nbAlea = nbAlea;
    }

    public byte[] getMdp() {
        return mdp;
    }

    public void setMdp(byte[] mdp) {
        this.mdp = mdp;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public long getTemps() {
        return temps;
    }

    public void setTemps(long temps) {
        this.temps = temps;
    }

    public double getNbAlea() {
        return nbAlea;
    }

    public void setNbAlea(double nbAlea) {
        this.nbAlea = nbAlea;
    }
}
