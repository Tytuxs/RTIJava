package Classe;

public class Voyageur {
    private int _numeroClient;
    public int get_numeroClient() {
        return _numeroClient;
    }
    public void set_numeroClient(int numeroClient) {
        this._numeroClient = numeroClient;
    }

    private String _nom;
    public String get_nom() {
        return _nom;
    }
    public void set_nom(String nom) {
        this._nom = nom;
    }

    private String _prenom;
    public String get_prenom() {
        return _prenom;
    }
    public void set_prenom(String prenom) {
        this._prenom = prenom;
    }

    private String _nationalite;
    public String get_nationalite() {
        return _nationalite;
    }
    public void set_nationalite(String nationalite) {
        this._nationalite = nationalite;
    }

    private String _dateDeNaissance;
    public String get_dateDeNaissance() {
        return _dateDeNaissance;
    }
    public void set_dateDeNaissance(String dateDeNaissance) {
        this._dateDeNaissance = dateDeNaissance;
    }


    private String _email;
    public String get_email() {
        return _email;
    }
    public void set_email(String _email) {
        this._email = _email;
    }

    public Voyageur(int numeroClient, String nom, String prenom, String nationalite, String dateDeNaissance, String email) {
        _numeroClient = numeroClient;
        _nom = nom;
        _prenom = prenom;
        _nationalite = nationalite;
        _dateDeNaissance = dateDeNaissance;
        _email = email;
    }
    public Voyageur(int numeroClient, String nom, String prenom, String dateDeNaissance, String email) {
        _numeroClient = numeroClient;
        _nom = nom;
        _prenom = prenom;
        _dateDeNaissance = dateDeNaissance;
        _email = email;
    }
}