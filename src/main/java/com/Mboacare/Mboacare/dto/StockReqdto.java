package com.Mboacare.Mboacare.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StockReqdto {

    @Min(value = 0, message = "la quantité doit être positive")
    private int quantite;

    @NotBlank(message = "veuillez remplir ce champ")
    private String nom;

    @NotBlank(message = "veuillez choisir une pharmacie")
    private String idPharmaci;

    public StockReqdto() {
    }

    public StockReqdto(int quantite, String nom, String idPharmaci) {
        this.quantite = quantite;
        this.nom = nom;
        this.idPharmaci = idPharmaci;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIdPharmaci() {
        return idPharmaci;
    }

    public void setIdPharmaci(String idPharmaci) {
        this.idPharmaci = idPharmaci;
    }
}