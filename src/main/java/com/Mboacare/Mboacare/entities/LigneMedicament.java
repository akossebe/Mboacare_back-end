package com.Mboacare.Mboacare.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Embeddable = cette classe n'est PAS une table a part entiere.
 * Elle represente juste "un bloc de colonnes" qu'on va integrer
 * directement dans la table Prescription (via @ElementCollection,
 * voir la classe Prescription).
 *
 * Une prescription contient PLUSIEURS lignes de medicaments,
 * chaque ligne = un medicament + sa posologie + sa duree + sa quantite.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LigneMedicament {

    // Nom du medicament. Pour l'instant on stocke juste le nom (String)
    // car le Module Pharmacie (avec l'entite Medicament) n'est pas encore
    // pret. Quand il le sera, on pourra remplacer ce champ par un
    // idMedicament (Long) qui pointe vers la vraie table Medicament.
    private String nomMedicament;

    // Exemple : "1 comprime matin et soir"
    private String posologie;

    // Exemple : "7 jours"
    private String duree;

    // Quantite totale a delivrer, exemple : 14 (comprimes)
    private Integer quantite;
}
