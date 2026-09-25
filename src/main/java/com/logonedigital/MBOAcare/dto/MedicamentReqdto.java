package com.logonedigital.MBOAcare.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MedicamentReqdto {


    @NotNull(message = "veuillez remplir ce champ")

    private String nom;
    @NotEmpty(message = "veuillez remplir ce champ")

    private String forme;

    @NotNull(message = "veuillez remplir ce champ")
    @Min(0)
    private int prix;


    public String getForme() {
        return forme;
    }

    public void setForme(String forme) {
        this.forme = forme;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }



    public MedicamentReqdto(String nom, String forme, int prix) {
        this.nom = nom;
        this.forme = forme;
        this.prix = prix;

    }

    public MedicamentReqdto() {
    }
}
