package Classe;

import java.io.Serializable;

public class Activite implements Serializable {
    private int id;
    private String type;
    private int nbMaxParticipants;
    private int nbInscrits;
    private int dureeHeure;
    private String date;
    private int nbJours;
    private float prixHTVA;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNbMaxParticipants() {
        return nbMaxParticipants;
    }

    public void setNbMaxParticipants(int nbMaxParticipants) {
        this.nbMaxParticipants = nbMaxParticipants;
    }

    public int getNbInscrits() {
        return nbInscrits;
    }

    public void setNbInscrits(int nbInscrits) {
        this.nbInscrits = nbInscrits;
    }

    public int getDureeHeure() {
        return dureeHeure;
    }

    public void setDureeHeure(int dureeHeure) {
        this.dureeHeure = dureeHeure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNbJours() {
        return nbJours;
    }

    public void setNbJours(int nbJours) {
        this.nbJours = nbJours;
    }

    public float getPrixHTVA() {
        return prixHTVA;
    }

    public void setPrixHTVA(float prixHTVA) {
        this.prixHTVA = prixHTVA;
    }
}
