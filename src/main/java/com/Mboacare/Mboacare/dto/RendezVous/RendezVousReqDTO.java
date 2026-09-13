package com.Mboacare.Mboacare.dto.RendezVous;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * "Request" DTO = ce que le CLIENT envoie pour CREER un rendez-vous.
 * On ne demande pas l'id (auto-genere) ni le statut (impose EN_ATTENTE
 * automatiquement au depart) : le client n'a pas a s'en soucier.
 *
 * Les annotations @NotNull, @Future, etc. sont de la VALIDATION :
 * si le client oublie un champ ou envoie une date passee, Spring
 * refuse la requete tout seul (HTTP 400) avant meme d'entrer dans le Service.
 */
@Getter
@Setter
public class RendezVousReqDTO {

    @NotNull(message = "La date souhaitee est obligatoire")
    @Future(message = "La date du rendez-vous doit etre dans le futur")
    private LocalDate dateSouhaitee;

    @NotNull(message = "L'heure souhaitee est obligatoire")
    @Schema(
            description = "Heure souhaitée du rendez-vous",
            example = "09:30:00"
    )
    private LocalTime heureSouhaitee;

    private String motifPrise;

    @NotNull(message = "L'identifiant du patient est obligatoire")
    private Long idPatient;

    @NotNull(message = "L'identifiant du medecin est obligatoire")
    private Long idMedecin;
}
