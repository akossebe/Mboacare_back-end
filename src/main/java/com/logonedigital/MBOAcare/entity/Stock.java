package com.logonedigital.MBOAcare.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;



    @Entity
    @Table(name = "STOCK")

    public class Stock {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        @Column(name = "id_stock", updatable = false, nullable = false)
        private String idStock;
        private int quantite;
        private String nom;

        @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<Medicament> medicaments = new ArrayList<>();

        @ManyToOne
        @JoinColumn(name = "id_pharmaci", nullable = true)
        private Pharmaci pharmaci;

        public Stock(int quantite, String nom) {
            this.quantite = quantite;
            this.nom = nom;

        }

    public Stock(String idStock, int quantite, String nom) {
        this.idStock = idStock;
        this.quantite = quantite;
        this.nom = nom;
    }

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

    public Pharmaci getPharmaci() { return pharmaci; }

        public void setPharmaci(Pharmaci pharmaci) { this.pharmaci = pharmaci; }

        public List<Medicament> getMedicaments() { return medicaments; }

        public void setMedicaments(List<Medicament> medicaments) { this.medicaments = medicaments; }





        public Stock() {
        }

    }
