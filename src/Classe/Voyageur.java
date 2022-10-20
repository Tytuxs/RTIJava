package Classe;

public class Voyageur {
    private int _numeroClient;
    public int get_numeroClient() {
        return _numeroClient;
    }
    public void set_numeroClient(int numeroClient) {
        this._numeroClient = numeroClient;
    }

    private char _nom;
    public char get_nom() {
        return _nom;
    }
    public void set_nom(char nom) {
        this._nom = nom;
    }

    private char _prenom;
    public char get_prenom() {
        return _prenom;
    }
    public void set_prenom(char prenom) {
        this._prenom = prenom;
    }

    private char _nationalite;
    public char get_nationalite() {
        return _nationalite;
    }
    public void set_nationalite(char nationalite) {
        this._nationalite = nationalite;
    }

    private char _dateDeNaissance;
    public char get_dateDeNaissance() {
        return _dateDeNaissance;
    }
    public void set_dateDeNaissance(char dateDeNaissance) {
        this._dateDeNaissance = dateDeNaissance;
    }


    private char _email;
    public char get_email() {
        return _email;
    }
    public void set_email(char _email) {
        this._email = _email;
    }

    public Voyageur(int numeroClient, char nom, char prenom, char nationalite, char dateDeNaissance, char email) {
        _numeroClient = numeroClient;
        _nom = nom;
        _prenom = prenom;
        _nationalite = nationalite;
        _dateDeNaissance = dateDeNaissance;
        _email = email;
    }
}
