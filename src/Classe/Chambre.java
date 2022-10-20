package Classe;

public class Chambre {

    private int _numeroChambre;
    private char _equipement;
    private int _nombreOccupants;
    private double _prixHTVA;

    public int get_numeroChambre() {
        return _numeroChambre;
    }
    public void set_numeroChambre(int _numeroChambre) {
        this._numeroChambre = _numeroChambre;
    }


    public char get_equipement() {
        return _equipement;
    }
    public void set_equipement(char _equipement) {
        this._equipement = _equipement;
    }


    public int get_nombreOccupants() {
        return _nombreOccupants;
    }
    public void set_nombreOccupants(int _nombreOccupants) {
        this._nombreOccupants = _nombreOccupants;
    }


    public double get_prixHTVA() {
        return _prixHTVA;
    }
    public void set_prixHTVA(double _prixHTVA) {
        this._prixHTVA = _prixHTVA;
    }

    public Chambre(int _numeroChambre, char _equipement, int _nombreOccupants, double _prixHTVA) {
        this._numeroChambre = _numeroChambre;
        this._equipement = _equipement;
        this._nombreOccupants = _nombreOccupants;
        this._prixHTVA = _prixHTVA;
    }


}
