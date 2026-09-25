package com.logonedigital.MBOAcare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


public class StockResdto {
    private String idStock;
    private int quantite ;
    private String nom;


    public String getIdStock() {
        return idStock;
    }

    public void setIdStock(String idStock) {
        this.idStock = idStock;
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



    public StockResdto(String idStock, int quantite, String nom) {
        this.idStock = idStock;
        this.quantite = quantite;
        this.nom = nom;
    }

    public StockResdto() {
    }


}
