package com.Mboacare.Mboacare.dto.RendezVous;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/** DTO specifique a l'action "reporter" un rendez-vous : nouvelle date/heure uniquement. */
@Getter
@Setter
public class ReporterRendezVousDTO {

    @NotNull(message = "La nouvelle date est obligatoire")
    private LocalDate nouvelleDateSouhaitee;

    @NotNull(message = "La nouvelle heure est obligatoire")
    private LocalTime nouvelleHeureSouhaitee;
}
