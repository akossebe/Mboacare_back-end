package com.Mboacare.Mboacare.entities;

import com.Mboacare.Mboacare.enums.StatutRendezVous;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @Entity dit a Spring/Hibernate : "cette classe correspond a une table
 * dans la base de donnees". Chaque attribut = une colonne.
 *
 * @Table(name = "rendez_vous") donne un nom explicite a la table
 * (sinon Hibernate utiliserait "RendezVous" tel quel).
 */
@Entity
@Table(name = "rendez_vous")
@Getter          // Lombok genere tous les getters (getId(), getStatut(), ...)
@Setter          // Lombok genere tous les setters (setId(), setStatut(), ...)
@NoArgsConstructor   // Lombok genere un constructeur vide (obligatoire pour JPA)
@AllArgsConstructor  // Lombok genere un constructeur avec tous les champs
@Builder             // Permet d'ecrire RendezVous.builder().motifPrise("...").build()
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // l'id est auto-incremente par la BD
    private Long idRendezVous;

    @Column(nullable = false)
    private LocalDate dateSouhaitee;

    @Column(nullable = false)
    private LocalTime heureSouhaitee;

    @Column(length = 500)
    private String motifPrise;

    @Enumerated(EnumType.STRING) // stocke "EN_ATTENTE" en texte plutot qu'un chiffre -> plus lisible en base
    @Column(nullable = false)
    private StatutRendezVous statut;

    // On ne met PAS de relation JPA (@ManyToOne) vers un objet Utilisateur
    // car l'entite Utilisateur appartient au module de tes collegues.
    // On stocke simplement son identifiant (Long) : c'est une "reference logique".
    @Column(nullable = false)
    private Long idPatient;

    @Column(nullable = false)
    private Long idMedecin;

    // Date de creation automatique de l'enregistrement (utile pour le suivi/tracabilite)
    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist // methode executee automatiquement juste avant l'enregistrement en base
    public void avantCreation() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutRendezVous.EN_ATTENTE;
        }
    }
}
