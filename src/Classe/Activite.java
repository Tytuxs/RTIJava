package Classe;

import java.io.Serializable;

public class Activite  implements Serializable {
    private int _id;
    private char type;
    private int _nombreMaxParticipants;
    private int _nbParticipantsInscrits;
    private char _dateDebut;
    private char _dateFin;
    private char _heureDebut;
    private char _heureFin;
    private double _prixHTVA;

    public int get_id() {
        return _id;
    }
    public void set_id(int id) {
        this._id = id;
    }

    public char getType() {
        return type;
    }
    public void setType(char type) {
        this.type = type;
    }


    public int get_nombreMaxParticipants() {
        return _nombreMaxParticipants;
    }
    public void set_nombreMaxParticipants(int nombreMaxParticipants) {
        this._nombreMaxParticipants = nombreMaxParticipants;
    }



    public int get_nbParticipantsInscrits() {
        return _nbParticipantsInscrits;
    }
    public void set_nbParticipantsInscrits(int nbParticipantsInscrits) {
        this._nbParticipantsInscrits = nbParticipantsInscrits;
    }



    public char get_dateDebut() {
        return _dateDebut;
    }
    public void set_dateDebut(char dateDebut) {
        this._dateDebut = dateDebut;
    }



    public char get_dateFin() {
        return _dateFin;
    }
    public void set_dateFin(char dateFin) {
        this._dateFin = dateFin;
    }



    public char get_heureDebut() {
        return _heureDebut;
    }
    public void set_heureDebut(char heureDebut) {
        this._heureDebut = heureDebut;
    }


    public char get_heureFin() {
        return _heureFin;
    }
    public void set_heureFin(char heureFin) {
        this._heureFin = heureFin;
    }


    public double get_prixHTVA() {
        return _prixHTVA;
    }
    public void set_prixHTVA(double prixHTVA) {
        this._prixHTVA = prixHTVA;
    }

    public Activite(int _id, char type, int _nombreMaxParticipants, int _nbParticipantsInscrits, char _dateDebut, char _dateFin, char _heureDebut, char _heureFin, double _prixHTVA) {
        this._id = _id;
        this.type = type;
        this._nombreMaxParticipants = _nombreMaxParticipants;
        this._nbParticipantsInscrits = _nbParticipantsInscrits;
        this._dateDebut = _dateDebut;
        this._dateFin = _dateFin;
        this._heureDebut = _heureDebut;
        this._heureFin = _heureFin;
        this._prixHTVA = _prixHTVA;
    }


}
