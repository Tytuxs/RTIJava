package Classe;

import java.io.Serializable;

public class Groupe implements Serializable {

    private char _adresse;
    private char _codePostal;
    private char _commune;

    public char get_adresse() {
        return _adresse;
    }
    public void set_adresse(char adresse) {
        this._adresse = adresse;
    }


    public char get_codePostal() {
        return _codePostal;
    }
    public void set_codePostal(char codePostal) {
        this._codePostal = codePostal;
    }


    public char get_commune() {
        return _commune;
    }
    public void set_commune(char commune) {
        this._commune = commune;
    }

    public Groupe(char _adresse, char _codePostal, char _commune) {
        this._adresse = _adresse;
        this._codePostal = _codePostal;
        this._commune = _commune;
    }

}
