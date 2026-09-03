package com.Mboacare.Mboacare.entities;

import com.Mboacare.Mboacare.enums.StatutPrescription;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrescription;

    @Column(nullable = false)
    private LocalDate dateEmission;

    // @ElementCollection permet de stocker une LISTE d'objets @Embeddable
    // (nos LigneMedicament) dans une table separee "prescription_lignes",
    // automatiquement liee a la prescription. Pas besoin de creer une
    // entite/table a part pour ca.
    @ElementCollection
    @CollectionTable(
            name = "prescription_lignes",
            joinColumns = @JoinColumn(name = "id_prescription")
    )
    @Builder.Default
    private List<LigneMedicament> lignesMedicaments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPrescription statut;


    // Reference vers la consultation d'origine (une consultation cloturee
    // ne peut avoir qu'UNE seule prescription active).
    @Column(nullable = false, unique = true)
    private Long idConsultation;

    @Column(nullable = false)
    private Long idMedecin;

    // Cette pharmacie n'est connue qu'au moment ou le patient choisit
    // ou envoyer sa prescription (via la geolocalisation) -> nullable au depart.
    @Column(nullable = true)
    private Long idPharmacie;

    @PrePersist
    public void avantCreation() {
        if (this.dateEmission == null) {
            this.dateEmission = LocalDate.now();
        }
        if (this.statut == null) {
            this.statut = StatutPrescription.EMISE;
        }
    }

}
