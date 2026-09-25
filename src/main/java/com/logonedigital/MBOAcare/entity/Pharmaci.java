package com.logonedigital.MBOAcare.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "PHARMACIE")


public class Pharmaci {
    @Id
    @GeneratedValue(generator = "uuid")
    @org.hibernate.annotations.GenericGenerator(
            name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false, length = 36)
    private String idPharmaci;


    private String nom;
    private LocalDate dateCreation;
    private String ville;
    private String quartier;
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "pharmaci",
            cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Stock> stock = new ArrayList<>();



    public Pharmaci() {
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }
    public List<Stock> getStock() {
        return stock;
    }

    public void setStock(List<Stock> stock) {
        this.stock = stock;

    }

    public Pharmaci(String nom, String ville, String quartier, String email) {
        this.nom = nom;
        this.ville = ville;
        this.quartier = quartier;
        this.email = email;
        this.dateCreation = LocalDate.now();

        }
    }




