package Classe;

import java.io.Serializable;

public class Chambre  implements Serializable {
    private int _numeroChambre;
    private String _categorie;
    private String _typeChambre;
    private int _nbOccupants;
    private float _prixHTVA;

    public int get_numeroChambre() {
        return _numeroChambre;
    }

    public void set_numeroChambre(int _numeroChambre) {
        this._numeroChambre = _numeroChambre;
    }

    public String get_categorie() {
        return _categorie;
    }

    public void set_categorie(String _categorie) {
        this._categorie = _categorie;
    }

    public String get_typeChambre() {
        return _typeChambre;
    }

    public void set_typeChambre(String _typeChambre) {
        this._typeChambre = _typeChambre;
    }

    public int get_nbOccupants() {
        return _nbOccupants;
    }

    public void set_nbOccupants(int _nbOccupants) {
        this._nbOccupants = _nbOccupants;
    }

    public float get_prixHTVA() {
        return _prixHTVA;
    }

    public void set_prixHTVA(float _prixHTVA) {
        this._prixHTVA = _prixHTVA;
    }
}
