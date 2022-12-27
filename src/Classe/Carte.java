package Classe;

import java.io.Serializable;

public class Carte implements Serializable {
    private String _numeroCarte;
    private String _nomClient;
    private String _mdp;

    public String get_numeroCarte() {
        return _numeroCarte;
    }

    public void set_numeroCarte(String _numeroCarte) {
        this._numeroCarte = _numeroCarte;
    }

    public String get_mdp() {
        return _mdp;
    }

    public void set_mdp(String _mdp) {
        this._mdp = _mdp;
    }

    public String get_nomClient() {
        return _nomClient;
    }

    public void set_nomClient(String _nomClient) {
        this._nomClient = _nomClient;
    }
}
