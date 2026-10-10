package com.logonedigital.MBOAcare.dto;

public class StockResdto {
    private String idStock;
    private int quantite;
    private String nom;
    private String idPharmaci;
    private String pharmaciNom;

    public StockResdto() {
    }

    public StockResdto(String idStock, int quantite, String nom) {
        this.idStock = idStock;
        this.quantite = quantite;
        this.nom = nom;
    }

    public StockResdto(String idStock, int quantite, String nom, String idPharmaci, String pharmaciNom) {
        this.idStock = idStock;
        this.quantite = quantite;
        this.nom = nom;
        this.idPharmaci = idPharmaci;
        this.pharmaciNom = pharmaciNom;
    }

    public String getIdStock() { return idStock; }
    public void setIdStock(String idStock) { this.idStock = idStock; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getIdPharmaci() { return idPharmaci; }
    public void setIdPharmaci(String idPharmaci) { this.idPharmaci = idPharmaci; }

    public String getPharmaciNom() { return pharmaciNom; }
    public void setPharmaciNom(String pharmaciNom) { this.pharmaciNom = pharmaciNom; }
}
