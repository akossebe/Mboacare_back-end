package com.Mboacare.Mboacare.dto.Consultation;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ConsultationReqDTO {
    @NotNull(message = "L'identifiant du rendez-vous est obligatoire")
    private Long idRendezVous;
    private String motif;
}
