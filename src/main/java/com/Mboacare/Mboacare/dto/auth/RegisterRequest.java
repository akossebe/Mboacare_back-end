package com.Mboacare.Mboacare.dto.auth;
import lombok.Data;
@Data
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String email;
    private String ville;
    private String quartier;
    private String motDePasse;
    private String role;
    
    // Nouveaux champs
    private String telephone;
    private java.time.LocalDate dateNaissance;
    private String genre;
    private String groupeSanguin;
    private Double poids;
    private Double taille;

    // Nouveaux champs spécifiques au Médecin
    private String numeroOrdre;
    private String specialite;
    private String lieuExercice;
}
