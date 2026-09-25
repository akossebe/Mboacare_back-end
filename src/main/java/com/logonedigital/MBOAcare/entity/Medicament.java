package com.logonedigital.MBOAcare.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "MEDICAMENT")


public class Medicament {
    @Id
    @GeneratedValue(generator = "uuid")
    @org.hibernate.annotations.GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false, length = 36)

    private String idMedicament;
    private String nom;
    private String forme;
    private int prix;


    @ManyToOne
    @JsonBackReference
    private Stock stock;


    public Medicament() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getIdMedicament() {
        return idMedicament;
    }

    public void setIdMedicament(String idMedicament) {
        this.idMedicament = idMedicament;
    }

    public String getForme() {
        return forme;
    }

    public void setForme(String forme) {
        this.forme = forme;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }


    public Medicament(String nom, String forme, int prix) {
        this.nom = nom;
        this.forme = forme;
        this.prix = prix;
    }

}
