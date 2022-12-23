package Classe;

import java.io.Serializable;

public class ReserActCha implements Serializable {
    private int _id;
    private String _persRef;
    private String _type;
    private int _numChambre;
    private String _typeAct;
    private String _typeCha;
    private int _nbMaxCha;
    private int _nbMaxAct;
    private int _nbInscrit;
    private int _nbNuits;
    private String _date;
    private int _nbJour;
    private float _prixAct;
    private float _prixCha;
    private boolean _paye;
    private String _categorie;
    private int _dureeHeure;
    private int _idAct;
    private float _dejaPaye;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_persRef() {
        return _persRef;
    }

    public void set_persRef(String _persRef) {
        this._persRef = _persRef;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public int get_numChambre() {
        return _numChambre;
    }

    public void set_numChambre(int _numChambre) {
        this._numChambre = _numChambre;
    }

    public String get_typeAct() {
        return _typeAct;
    }

    public void set_typeAct(String _typeAct) {
        this._typeAct = _typeAct;
    }

    public String get_typeCha() {
        return _typeCha;
    }

    public void set_typeCha(String _typeCha) {
        this._typeCha = _typeCha;
    }

    public int get_nbMaxCha() {
        return _nbMaxCha;
    }

    public void set_nbMaxCha(int _nbMaxCha) {
        this._nbMaxCha = _nbMaxCha;
    }

    public int get_nbMaxAct() {
        return _nbMaxAct;
    }

    public void set_nbMaxAct(int _nbMaxAct) {
        this._nbMaxAct = _nbMaxAct;
    }

    public int get_nbInscrit() {
        return _nbInscrit;
    }

    public void set_nbInscrit(int _nbInscrit) {
        this._nbInscrit = _nbInscrit;
    }

    public int get_nbNuits() {
        return _nbNuits;
    }

    public void set_nbNuits(int _nbNuits) {
        this._nbNuits = _nbNuits;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public int get_nbJour() {
        return _nbJour;
    }

    public void set_nbJour(int _nbJour) {
        this._nbJour = _nbJour;
    }

    public float get_prixAct() {
        return _prixAct;
    }

    public void set_prixAct(float _prixAct) {
        this._prixAct = _prixAct;
    }

    public float get_prixCha() {
        return _prixCha;
    }

    public void set_prixCha(float _prixCha) {
        this._prixCha = _prixCha;
    }

    public boolean is_paye() {
        return _paye;
    }

    public void set_paye(boolean _paye) {
        this._paye = _paye;
    }

    public String get_categorie() {
        return _categorie;
    }

    public void set_categorie(String _categorie) {
        this._categorie = _categorie;
    }

    public int get_dureeHeure() {
        return _dureeHeure;
    }

    public void set_dureeHeure(int _dureeHeure) {
        this._dureeHeure = _dureeHeure;
    }

    public int get_idAct() {
        return _idAct;
    }

    public void set_idAct(int _idAct) {
        this._idAct = _idAct;
    }

    public float get_dejaPaye() {
        return _dejaPaye;
    }

    public void set_dejaPaye(float _dejaPaye) {
        this._dejaPaye = _dejaPaye;
    }
}
