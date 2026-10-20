package com.Mboacare.Mboacare.dto;

public class MedicamentResdto {
    private String idMedicament;
    private String nom;
    private String forme;
    private int prix;
    private String idStock;
    private String stockNom;
    private Integer quantiteStock;
    private String pharmaciNom;
    private String pharmaciVille;

    public MedicamentResdto() {
    }

    public MedicamentResdto(String idMedicament, String nom, String forme, int prix) {
        this.idMedicament = idMedicament;
        this.nom = nom;
        this.forme = forme;
        this.prix = prix;
    }

    public MedicamentResdto(String idMedicament, String nom, String forme, int prix,
                            String idStock, String stockNom, Integer quantiteStock,
                            String pharmaciNom, String pharmaciVille) {
        this.idMedicament = idMedicament;
        this.nom = nom;
        this.forme = forme;
        this.prix = prix;
        this.idStock = idStock;
        this.stockNom = stockNom;
        this.quantiteStock = quantiteStock;
        this.pharmaciNom = pharmaciNom;
        this.pharmaciVille = pharmaciVille;
    }

    public String getIdMedicament() {
        return idMedicament;
    }

    public void setIdMedicament(String idMedicament) {
        this.idMedicament = idMedicament;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getForme() {
        return forme;
    }

    public void setForme(String forme) {
        this.forme = forme;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getIdStock() {
        return idStock;
    }

    public void setIdStock(String idStock) {
        this.idStock = idStock;
    }

    public String getStockNom() {
        return stockNom;
    }

    public void setStockNom(String stockNom) {
        this.stockNom = stockNom;
    }

    public Integer getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Integer quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public String getPharmaciNom() {
        return pharmaciNom;
    }

    public void setPharmaciNom(String pharmaciNom) {
        this.pharmaciNom = pharmaciNom;
    }

    public String getPharmaciVille() {
        return pharmaciVille;
    }

    public void setPharmaciVille(String pharmaciVille) {
        this.pharmaciVille = pharmaciVille;
    }
}