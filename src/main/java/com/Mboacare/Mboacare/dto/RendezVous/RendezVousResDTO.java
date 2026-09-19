package com.Mboacare.Mboacare.dto.RendezVous;


import com.Mboacare.Mboacare.enums.StatutRendezVous;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class RendezVousResDTO {
    private Long idRendezVous;
    private LocalDate dateSouhaitee;
    private LocalTime heureSouhaitee;
    private String motifPrise;
    private StatutRendezVous statut;
    private Long idPatient;
    private Long idMedecin;
    private LocalDateTime dateCreation;
}