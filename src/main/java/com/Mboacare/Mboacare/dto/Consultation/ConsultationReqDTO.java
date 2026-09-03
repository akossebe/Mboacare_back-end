package com.Mboacare.Mboacare.dto.Consultation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour CREER une consultation. On part toujours d'un rendez-vous
 * CONFIRME existant (idRendezVous). Les autres informations (patient,
 * medecin) sont retrouvees automatiquement a partir du rendez-vous
 * par le Service, pour eviter les incoherences.
 */
@Getter
@Setter
public class ConsultationReqDTO {

    @NotNull(message = "L'identifiant du rendez-vous est obligatoire")
    private Long idRendezVous;

    private String motif;
}
