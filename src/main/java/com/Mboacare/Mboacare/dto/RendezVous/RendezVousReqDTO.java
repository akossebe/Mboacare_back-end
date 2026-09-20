package com.Mboacare.Mboacare.dto.RendezVous;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
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
