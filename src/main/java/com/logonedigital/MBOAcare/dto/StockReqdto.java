package com.logonedigital.MBOAcare.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StockReqdto {

    @NotBlank(message = "veuillez remplir ce champ")
    private String nom;

    @Min(value = 0, message = "la quantite doit etre positive")
    private int quantite;

    @NotBlank(message = "veuillez choisir une pharmacie")
    private String idPharmaci;

    public StockReqdto() {
    }

    public StockReqdto(int quantite, String nom, String idPharmaci) {
        this.quantite = quantite;
        this.nom = nom;
        this.idPharmaci = idPharmaci;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public String getIdPharmaci() { return idPharmaci; }
    public void setIdPharmaci(String idPharmaci) { this.idPharmaci = idPharmaci; }
}
