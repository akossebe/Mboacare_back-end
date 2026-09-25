package com.logonedigital.MBOAcare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class PharmaciReqdto {

    @NotEmpty(message = "veuillez remplir ce champ")

    private String nom;
    @Email(message = "cette email est erone")
    @NotEmpty(message = "veuillez remplir ce champ")

    private String email;

    @NotEmpty(message = "veuillez remplir ce champ")

    private String ville ;

    @NotEmpty(message = "veuillez remplir ce champ")

    private String quartier ;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public PharmaciReqdto(String quartier, String ville, String email, String nom) {
        this.quartier = quartier;
        this.ville = ville;
        this.email = email;
        this.nom = nom;
    }

    public PharmaciReqdto() {
    }
}
