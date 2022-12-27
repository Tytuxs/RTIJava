package ClassesCrypto;

import java.io.Serializable;

public class RequeteDigest implements Serializable {
    private Byte[] mdp;
    private String utilisateur;
    private long temps;
    private double nbAlea;

    public RequeteDigest(Byte[] mdp, String utilisateur, long temps, double nbAlea) {
        this.mdp = mdp;
        this.utilisateur = utilisateur;
        this.temps = temps;
        this.nbAlea = nbAlea;
    }
}
