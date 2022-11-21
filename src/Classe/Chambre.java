package Classe;

import java.io.Serializable;

public class Chambre  implements Serializable {
    /*`numeroChambre` int NOT NULL,
            `categorie` varchar(45) NOT NULL,
  `typeChambre` varchar(45) NOT NULL,
  `nbOccupants` int NOT NULL,
            `prixHTVA` decimal(6,2) NOT NULL,*/

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

    /*private char _equipement;
    public char get_equipement() {
        return _equipement;
    }
    public void set_equipement(char _equipement) {
        this._equipement = _equipement;
    }*/


    /*public Chambre(int _numeroChambre, char _equipement, int _nombreOccupants, double _prixHTVA) {
        this._numeroChambre = _numeroChambre;
        this._nbOccupants = _nombreOccupants;
        this._prixHTVA = _prixHTVA;
    }*/


}
