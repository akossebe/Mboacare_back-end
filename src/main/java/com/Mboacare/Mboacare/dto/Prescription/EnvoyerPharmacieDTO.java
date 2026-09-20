package com.Mboacare.Mboacare.dto.Prescription;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EnvoyerPharmacieDTO {
    @NotNull(message = "L'identifiant de la pharmacie est obligatoire")
    private Long idPharmacie;
}
