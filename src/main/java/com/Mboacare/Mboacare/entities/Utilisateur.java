package com.Mboacare.Mboacare.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "utilisateurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    private String ville;
    private String quartier;

    @Column(nullable = false)
    private String motDePasse;

    @Column(nullable = false)
    private String role; // patient, medecin, pharmacien

    // Informations complémentaires
    private String telephone;
    private java.time.LocalDate dateNaissance;
    private String genre;
    private String groupeSanguin;
    private Double poids;
    private Double taille;

    @Column(length = 1000)
    private String allergies;
    @Column(length = 1000)
    private String maladiesChroniques;
    @Column(length = 1000)
    private String traitementsEnCours;

    @Column(columnDefinition = "LONGTEXT")
    private String photoProfil; // Pour stocker l'image en Base64

    // Informations spécifiques au Médecin
    private String numeroOrdre; // Numéro ONMC
    private String specialite;
    private String lieuExercice;
}
