package com.logonedigital.MBOAcare.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class MedicamentReqdto {

    @NotBlank(message = "veuillez remplir ce champ")
    private String nom;

    @NotBlank(message = "veuillez remplir ce champ")
    private String forme;

    @Min(value = 0, message = "le prix doit etre positif")
    private int prix;

    @NotBlank(message = "veuillez choisir un stock")
    private String idStock;

    public MedicamentReqdto() {
    }

    public MedicamentReqdto(String nom, String forme, int prix) {
        this.nom = nom;
        this.forme = forme;
        this.prix = prix;
    }

    public MedicamentReqdto(String nom, String forme, int prix, String idStock) {
        this.nom = nom;
        this.forme = forme;
        this.prix = prix;
        this.idStock = idStock;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getForme() { return forme; }
    public void setForme(String forme) { this.forme = forme; }

    public int getPrix() { return prix; }
    public void setPrix(int prix) { this.prix = prix; }

    public String getIdStock() { return idStock; }
    public void setIdStock(String idStock) { this.idStock = idStock; }
}
